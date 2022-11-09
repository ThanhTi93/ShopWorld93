package com.thanhti.shop;

import com.thanhti.shop.domain.Category;

public class CategoryRawExport {

	private Long categoryId;
	private String name ;
	
	public CategoryRawExport(Category category ) {
		this.categoryId = category.getId();
		this.name = category.getName();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
