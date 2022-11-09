package com.thanhti.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.thanhti.shop.interceptor.AdminAuthenticationInterceptor;



@Configuration
public class AuthenticationInterceptorConfig implements WebMvcConfigurer{

	@Autowired
	private AdminAuthenticationInterceptor   adminauthenticationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminauthenticationInterceptor)
		.addPathPatterns("/admin/**");
	}
	
	
}
