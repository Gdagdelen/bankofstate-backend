package com.bank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
//@AllArgsConstructor		this will create constructor with all fields
//@NoArgsConstructor		this will create constructor with no field
public class Recipient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private Integer phone;
	private String bankName;
	private String bankNumber;

	public Recipient() {
	}

	public Recipient(String name, String email, Integer phone, 
					String bankName, String bankNumber) {
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.bankName = bankName;
		this.bankNumber = bankNumber;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

}
