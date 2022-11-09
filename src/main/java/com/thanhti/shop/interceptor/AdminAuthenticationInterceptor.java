package com.thanhti.shop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.thanhti.shop.domain.Account;









@Component
public class AdminAuthenticationInterceptor implements HandlerInterceptor {
	private boolean check;

	
@Autowired
HttpSession session;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		System.out.println("pre handle of request: " +request.getRequestURI());
		if (session.getAttribute("username") != null) {
			check = true;
		}
		
		String uri = request.getRequestURI();
		Account user = (Account) session.getAttribute("account");
		System.out.println(uri);
		String error = "";
		if (user == null) {
			error = "Vui lòng đăng nhập!";
		}
		else if (!user.isRole() && uri.contains("/admin/")) {
			error = "Vui lòng đăng nhập với vai trò Admin";
			System.out.println(user.isRole());
		}
		if (error.length() > 0) {
			error = "Vui lòng đăng nhập với vai trò Admin";
			session.setAttribute("redirect-uri", uri);
			session.setAttribute("error", error);
			response.sendRedirect("/login");
			check = false;
		}
			
		
		return check;
	}

}
