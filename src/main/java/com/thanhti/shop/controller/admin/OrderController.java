package com.thanhti.shop.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import org.springframework.web.servlet.ModelAndView;


import com.thanhti.shop.domain.Order;
import com.thanhti.shop.domain.OrderDetail;
import com.thanhti.shop.model.OrderDTO;
import com.thanhti.shop.service.OrderDetailService;
import com.thanhti.shop.service.OrderService;






@Controller
@RequestMapping("admin/order")
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderDetailService orderDetailService;
	
	@RequestMapping("")
	public String search(ModelMap model,
			@RequestParam(name = "name1", required = false) String name1,		
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		
		int currentPage = page.orElse(1);
		Integer pageSize = size.orElse(5);
		
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("orderId"));
		Page<Order> resultPage = null;
		
		if (StringUtils.hasText(name1)) {
			resultPage = orderService.findByNameContaining(name1, pageable);
			model.addAttribute("name", name1);
		}else {
			resultPage = orderService.findAll(pageable);
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
		
		model.addAttribute("orderPage", resultPage);
		
		return "admin/orders/list";
	}

	@GetMapping("edit/{orderId}")
	public ModelAndView edit(ModelMap model,@PathVariable("orderId") Long orderId) {
		
		Optional<Order> opt = orderService.findById(orderId);		
		OrderDTO dto = new OrderDTO();
		List<OrderDetail> listOrderDetail = orderDetailService.findByOrderId(orderId);
		if(opt.isPresent()) {
			Order entity = opt.get();
			
			BeanUtils.copyProperties(entity, dto);			
			
			Double total = listOrderDetail.stream()
					.mapToDouble(tt -> tt.getUnitPrice() * tt.getQuantity()).sum();
			model.addAttribute("order", dto);
			model.addAttribute("listOrderDetails", listOrderDetail);
			model.addAttribute("total", total);
		}
		
		return new ModelAndView("admin/orders/addOrEdit", model);
	}
	
	@PostMapping("update")
	public ModelAndView saveOrUpdate( ModelMap model,
			@Valid @ModelAttribute("order") OrderDTO dto, BindingResult result) {
		
		if(result.hasErrors()) {
			return new ModelAndView("admin/orders/addOrEdit", model);
		}
		dto.setOrderDate(new Date());
		Order entity = new Order();
		BeanUtils.copyProperties(dto, entity);
		
		orderService.save(entity);
		
		model.addAttribute("message", "Order is save !");		
		
		return new ModelAndView("forward:/admin/order", model);
	}
	
	@GetMapping("delete/{orderId}")
	public String delete(@PathVariable("orderId") Long orderId, 
			Model model) {
		orderService.deleteById(orderId);
		model.addAttribute("message", "Xóa thành công");
		
		return "forward:/admin/order";
	}
}
