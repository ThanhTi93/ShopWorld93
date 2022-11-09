package com.thanhti.shop.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable{
	
	private Long orderId;
	private String name;
	private String phone;
	private String note;
	private Date orderDate;
	private String address;
	private short status;
	private boolean statusCheckout;
//	@Id
//	private Serializable group;
	private double tongTien;
	
}
