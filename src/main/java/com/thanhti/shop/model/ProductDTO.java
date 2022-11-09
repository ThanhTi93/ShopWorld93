package com.thanhti.shop.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable{

	private Long productId;
	private String name;
	private int quantity;
	private double oldPrice;
	private double newPrice;
	private String image;
	
	private MultipartFile imageFile;
	
	private String description;
	private String note;
	private double discount;
	private Date enteredDate;
	private Long categoryId;
	
	private Boolean isEdit = false ;
}
