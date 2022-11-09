package com.thanhti.shop.controller.admin;



import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.thanhti.shop.domain.Account;
import com.thanhti.shop.model.AccountDTO;
import com.thanhti.shop.service.AccountService;










@Controller
@RequestMapping("admin")
public class ProfileController {

	@Autowired
	AccountService accountService;

	@Autowired
	private HttpSession  session;
	
	@GetMapping("profile")
	public String profile(Model model) {
	String user = (String) session.getAttribute("username");
	Optional<Account> opt = accountService.findById(user);
		if (user == null) {
			return "redirect:/login";
		}
	
		AccountDTO dto = new AccountDTO();
		Account entity = opt.get();
		BeanUtils.copyProperties(entity, dto);
		
		
		model.addAttribute("profile", dto);
		
		return "admin/profile/edit";
		
	}
	@PostMapping("profile")
	public ModelAndView update(ModelMap model,
			@Valid @ModelAttribute("profile") AccountDTO dto, 
			BindingResult result) throws IOException {
		
		if (result.hasErrors()) {
			return new ModelAndView("admin/profile/edit", model);
		}
		
		System.out.println("Post: " +dto.isRole());
		Account entity = new Account();
		BeanUtils.copyProperties(dto, entity);
		
		accountService.save(entity);
		
		model.addAttribute("message", "Thông tin đã được cập nhật");
		
		return new ModelAndView("admin/profile/edit", model);
	}
	
	
}
