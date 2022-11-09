package com.thanhti.shop.controller.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {

	@Autowired
	HttpSession session;
	
	@RequestMapping("logout")
	public String logout() {
		
		session.removeAttribute("account");
		return "redirect:/login";
	}
}
