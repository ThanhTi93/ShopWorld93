package com.thanhti.shop.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long orderId;
	@Temporal(TemporalType.DATE)
	@Column(name = "order_date", nullable = false)
	private Date orderDate;
	
	@Column(columnDefinition = "nvarchar(30) not null")
	private String name;
	
	@Column(columnDefinition = "varchar(15) not null")
	private String phone;
	
	@Column(columnDefinition = "nvarchar(200) not null")
	private String note;
	
	@Column(nullable = false)
	private short status;
	
	@Column(columnDefinition = "nvarchar(30) not null")
	private String address;
	
	@Column(nullable = false)
	private double totalPrice;
	
	@Column(nullable = false, name = "status_checkout")
	private boolean statusCheckout;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Account account;	
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetails;
}
