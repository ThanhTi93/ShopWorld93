package com.thanhti.shop.controller.admin;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.thanhti.shop.domain.Account;
import com.thanhti.shop.model.AdminLoginDTO;
import com.thanhti.shop.service.AccountService;



@Controller
public class AdminLoginController {

	@Autowired
	private AccountService  accountService;
	
	@Autowired
	HttpSession session;
	
	@GetMapping("login")
	public String login(ModelMap model) {
		
		model.addAttribute("account", new AdminLoginDTO());
		return "/admin/accounts/login";
	}
	@PostMapping("login")
	public ModelAndView login(ModelMap model,
			@Valid @ModelAttribute("account") AdminLoginDTO dto,
			BindingResult result) {
		if(result.hasErrors()) {
			return  new ModelAndView("/admin/accounts/login", model);
		}
		Account account = accountService.login(dto.getUsername(), dto.getPassword());
		if(account == null) {
			model.addAttribute("message", "Mật khẩu hoặc tên đăng nhập không đúng.");
			return  new ModelAndView("/admin/accounts/login", model);
		}
		session.setAttribute("account", account);
		session.setAttribute("username", account.getUsername());
		session.setAttribute("checkLogin", true);
		String ruri = (String) session.getAttribute("redirect-uri");		
		
		if (ruri != null) {
			session.removeAttribute("redirect-uri");
			session.removeAttribute("errorLogin");
			if (account.isRole()) {
				return new ModelAndView("redirect:/admin/categories");
			}
			return new ModelAndView("redirect:" +ruri);
		}
		return new ModelAndView("forward:/admin/categories", model);
	}
}
