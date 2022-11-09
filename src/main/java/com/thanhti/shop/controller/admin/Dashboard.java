package com.thanhti.shop.controller.admin;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thanhti.shop.domain.Order;
import com.thanhti.shop.model.AccountDTO;
import com.thanhti.shop.repository.OrderRepository;

@Controller
public class Dashboard {
	
	@Autowired
	OrderRepository orderRepository;

	@GetMapping("admin/statistical")
	public String add(Model model) {
		
		double[] totalPricePerMonth = new double[12];
        int[] totalOrderPerMonth = new int[12];
        List<Order> allOrders = (List<Order>) orderRepository.findAll();
        for (Order order : allOrders) {
            int month = order.getOrderDate().getMonth();
            totalPricePerMonth[month] = totalPricePerMonth[month] + order.getTotalPrice();//todo: replace 12 by order total price
            totalOrderPerMonth[month] = totalOrderPerMonth[month] + 1;
            System.out.println(totalPricePerMonth[month]);
            System.out.println(totalOrderPerMonth[month]);
            
        }
        // convert from arrays to string to adapt with chart data format
        String totalOrderPrice = Arrays.toString(totalPricePerMonth).substring(1, Arrays.toString(totalPricePerMonth).length() - 1);
        String totalOrderNumber = Arrays.toString(totalOrderPerMonth).substring(1, Arrays.toString(totalOrderPerMonth).length() - 1);
        System.out.println(totalOrderNumber);
        System.out.println(totalOrderPrice);
        // for bar & pie chart
        model.addAttribute("total_order_price", totalOrderPrice);
        model.addAttribute("total_order_number", totalOrderNumber);
//		
//		model.addAttribute("total_order_price", "65, 59, 80, 81, 56, 55, 40, 65, 59, 80, 81, 56");
//		model.addAttribute("total_order_number", "4, 59, 6, 81, 56, 4, 7, 65, 59, 80, 6, 56");
		
		return "admin/dashboard/dashboard";
	}
}
