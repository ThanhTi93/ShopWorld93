package com.thanhti.shop.controller.admin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

import com.thanhti.shop.CSVHelperProduct;
import com.thanhti.shop.CSVProductService;
import com.thanhti.shop.ProductRawExport;
import com.thanhti.shop.domain.Category;
import com.thanhti.shop.domain.Product;
import com.thanhti.shop.model.CategoryDTO;
import com.thanhti.shop.model.ProductDTO;
import com.thanhti.shop.service.CategoryService;
import com.thanhti.shop.service.ProductService;
import com.thanhti.shop.service.StorageService;

@Controller
@RequestMapping("admin/products")
public class ProductController {

	@Autowired
	ProductService   productService;
	
	@Autowired
	CategoryService  categoryService;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
    CSVProductService csvProductService;
	
	@ModelAttribute("categories")
	public List<CategoryDTO> getCategories(){
		return categoryService.findAll().stream().map(item->{
			CategoryDTO dto = new CategoryDTO();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).toList();
	}
	@GetMapping("add")
	public String add(Model model) {
		ProductDTO dto = new ProductDTO();
		dto.setIsEdit(false);
		model.addAttribute("product", dto);
		return"admin/products/addOrEdit";
	}
	
	@GetMapping("edit/{productId}")
	public ModelAndView edit(ModelMap model, @PathVariable("productId") Long productId) {
		
		Optional<Product> opt = productService.findById(productId);		
		ProductDTO dto = new ProductDTO();
		
		if(opt.isPresent()) {
			Product entity = opt.get();
			
			BeanUtils.copyProperties(entity, dto);
			
			dto.setCategoryId(entity.getCategory().getId());
			dto.setIsEdit(true);
			
			model.addAttribute("product", dto);			
			return new ModelAndView("admin/products/addOrEdit", model);
		}
		model.addAttribute("message", "Product is not existed");
		
		return new ModelAndView("forward:/admin/products", model);
	}
	
	@GetMapping("images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename){
		Resource file = storageService.loadAsResource(filename);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate( ModelMap model,
			@Valid @ModelAttribute("product") ProductDTO dto, BindingResult result) {
		
		if(result.hasErrors()) {
			return new ModelAndView("admin/products/addOrEdit",model);
		}
		dto.setEnteredDate(new Date());
		Product entity = new Product();
		BeanUtils.copyProperties(dto, entity);
		
		
		Category category = new Category();
		category.setId(dto.getCategoryId());
		entity.setCategory(category);
		
		if(!dto.getImageFile().isEmpty()) {
			UUID uuid = UUID.randomUUID();
			String uuString = uuid.toString();
			
			entity.setImage(storageService.getStoredFilename(dto.getImageFile(), uuString));			
			storageService.store(dto.getImageFile(), entity.getImage());		
		}
		
		productService.save(entity);
		
		model.addAttribute("message", "Product is save !");		
		
		return new ModelAndView("forward:/admin/products", model);
	}
	@RequestMapping("")
	public String search(ModelMap model,
			@RequestParam(name = "name1", required = false) String name1,		
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		
		int currentPage = page.orElse(1);
		Integer pageSize = size.orElse(5);
		
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Direction.DESC,"productId"));
		Page<Product> resultPage = null;
		
		if (StringUtils.hasText(name1)) {
			resultPage = productService.findByNameContainingOrDescriptionContaining(name1, name1, pageable);
			model.addAttribute("name", name1);
		}else {
			resultPage = productService.findAll(pageable);
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
		
		model.addAttribute("productPage", resultPage);
		
		return "admin/products/list";
	}
	
	@GetMapping("delete/{productId}")
	public ModelAndView delete(ModelMap model, @PathVariable("productId") Long productId) throws IOException {							
		
		Optional<Product> opt = productService.findById(productId);
		if(opt.isPresent()) {
			if(!StringUtils.isEmpty(opt.get().getImage())) {
				storageService.delete(opt.get().getImage());
			}
			
			productService.delete(opt.get());
			
			model.addAttribute("message", "Product is deleted!");
		}else {
			model.addAttribute("message", "Product is not Found !");
		}			
		
		return new ModelAndView("forward:/admin/products", model);
	}
	
    @GetMapping("/export-csv-product")
    public void exportCSVfile(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=books_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Product> productEntityList = (List<Product>) productService.findAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID","Name", "Category_ID", "Quantity", "Old Price", "New Price", "Image","Qescription","Note","Discount","Entered Date"};
        String[] nameMapping = {"productId", "name", "categoryId", "quantity", "oldPrice", "newPrice", "image","description","note","discount","enteredDate"};

        csvWriter.writeHeader(csvHeader);

        for (Product productEntity : productEntityList) {
            try {
                ProductRawExport data = new ProductRawExport(productEntity);
                csvWriter.write(data, nameMapping);
            } catch (Exception e) {
                System.out.println("Skip this record/data");
                continue;
            }
        }

        csvWriter.close();
    }
    @PostMapping("import-csv-product")
    public String importCSVfile(@RequestParam("file") MultipartFile file) {
        if (CSVHelperProduct.hasCSVFormat(file)) {
            try {
            	csvProductService.saveProductData(file);
            	return "forward:/admin/categories";
            } catch (Exception e) {
            	System.out.print(e);
                return "The CSV file has NOT been imported";
            }
        }
        return "Please upload a proper CSV file!";
    }
	
}
