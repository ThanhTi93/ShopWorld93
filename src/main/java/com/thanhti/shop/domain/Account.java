package com.thanhti.shop.domain;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account implements Serializable{

	@Id
	@Column(length = 30)
	private String username;
	
	@Column(length = 60)
	private String password;
	
	@Column(columnDefinition = "nvarchar(50) not null")
	private String name;
	
	@Column(columnDefinition = "nvarchar(100) not null")
	private String email;
	
	@Column(length = 15, nullable = false)
	private String phone;
	
	@Column(nullable = false)
	private boolean role;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Order> orders;
	
	
}
