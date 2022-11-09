package com.thanhti.shop.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
	@NotEmpty
	@Length(min = 6)
	private String username;
	@NotEmpty
	@Length(min = 6)
	private String password;
	@NotEmpty
	@Length(min = 5)
	private String name;
	@NotEmpty
	@Email
	private String email;
	@NotEmpty
	private String phone;
	
	private boolean role;
	
	private boolean checkEdit = false;
}
