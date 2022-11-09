package com.thanhti.shop.controller.admin;



import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


import com.thanhti.shop.CSVHelper;
import com.thanhti.shop.CSVService;
import com.thanhti.shop.CategoryRawExport;
import com.thanhti.shop.domain.Category;
import com.thanhti.shop.model.CategoryDTO;
import com.thanhti.shop.service.CategoryService;




@Controller
@RequestMapping("admin/categories")
public class CategoryController {

	@Autowired
	CategoryService  categoryService;
	
	@Autowired
    CSVService csvService;
	
	@GetMapping("add")
	public String add(Model model) {
		
		model.addAttribute("category", new CategoryDTO());
		return"admin/categories/newOrEdit";
	}
	
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate( ModelMap model,
			@Valid @ModelAttribute("category") CategoryDTO dto, BindingResult result) {
		
		if(result.hasErrors()) {
			return new ModelAndView("admin/categories/newOrEdit",model);
		}
		Category entity = new Category();
		BeanUtils.copyProperties(dto, entity);
		
		categoryService.save(entity);
		
		model.addAttribute("message", "Category is save !");		
		
		return new ModelAndView("forward:/admin/categories", model);
	}

	@RequestMapping("")
	public String search(ModelMap model,
			@RequestParam(name = "name1", required = false) String name1,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		
		int currentPage = page.orElse(1);
		Integer pageSize = size.orElse(5);
		
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("id"));
		Page<Category> resultPage = null;
		
		
		if (StringUtils.hasText(name1)) {
			resultPage = categoryService.findByNameContaining(name1, pageable);
			model.addAttribute("name", name1);
		}else {
			resultPage = categoryService.findAll(pageable);
		}
		
		int totalPages = resultPage.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages); 
			
			if (totalPages > 5) {
				if (end == totalPages){
					start = end - 5;
				}else if (start == 1) {
					end = start + 5;
				}
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
					.boxed()
					.collect(Collectors.toList());
			
			model.addAttribute("pageNumbers", pageNumbers);
		}
		
		model.addAttribute("categoryPage", resultPage);
		
		return "admin/categories/list";
	}
	
	@GetMapping("edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Long id) {
		
		Optional<Category> opt = categoryService.findById(id);		
		CategoryDTO dto = new CategoryDTO();
		
		if(opt.isPresent()) {
			Category entity = opt.get();
			
			BeanUtils.copyProperties(entity, dto);
			dto.setIsEdit(true);
			
			model.addAttribute("category", dto);			
			return new ModelAndView("admin/categories/newOrEdit", model);
		}
		model.addAttribute("message", "Category is not existed");
		
		return new ModelAndView("forward:/admin/categories", model);
	}

	@GetMapping("delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long id) {							
		
		categoryService.deleteById(id);
		
		model.addAttribute("message", "Category is deleted !");
		
		return new ModelAndView("forward:/admin/categories", model);
	}
	
    @PostMapping("import-csv")
    public String importCSVfile(@RequestParam("file") MultipartFile file) {
        if (CSVHelper.hasCSVFormat(file)) {
            try {
            	csvService.saveCategoryData(file);
            	return "forward:/admin/categories";
            } catch (Exception e) {
            	System.out.print(e);
                return "The CSV file has NOT been imported";
            }
        }
        return "Please upload a proper CSV file!";
    }
    @GetMapping("/export-csv")
    public void exportCSVfile(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=books_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Category> categoryEntityList = (List<Category>) categoryService.findAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Category_ID", "name" };
        String[] nameMapping = {"categoryId","name"};

        csvWriter.writeHeader(csvHeader);

        for (Category bookEntity : categoryEntityList) {
            try {
                CategoryRawExport data = new CategoryRawExport(bookEntity);
                csvWriter.write(data, nameMapping);
            } catch (Exception e) {
                System.out.println("Skip this record/data");
                continue;
            }
        }

        csvWriter.close();
    }

}
