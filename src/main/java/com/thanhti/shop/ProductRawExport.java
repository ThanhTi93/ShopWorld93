package com.thanhti.shop;

import java.text.SimpleDateFormat;


import com.thanhti.shop.domain.Product;

public class ProductRawExport {

	private Long productId;
	private String name;
	private int quantity;
	private double oldPrice;
	private double newPrice;
	private String image;		
	private String description;
	private String note;
	private double discount;
	private String enteredDate;
	private Long categoryId;
	
    public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(double oldPrice) {
		this.oldPrice = oldPrice;
	}

	public double getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(double newPrice) {
		this.newPrice = newPrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getEnteredDate() {
		return enteredDate;
	}

	public void setEnteredDate(String enteredDate) {
		this.enteredDate = enteredDate;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public ProductRawExport(Product productEntity) {
        this.productId = productEntity.getProductId();
        this.name = productEntity.getName();
        this.quantity = productEntity.getQuantity();
        this.categoryId = productEntity.getCategory().getId();
        this.image = productEntity.getImage();
        this.oldPrice = productEntity.getOldPrice();
        this.newPrice = productEntity.getNewPrice();
        this.description = productEntity.getDescription();
        this.note = productEntity.getNote();
        this.discount = productEntity.getDiscount();       
        this.enteredDate =  new SimpleDateFormat("yyyy-MM-dd").format(productEntity.getEnteredDate());
    }
}
