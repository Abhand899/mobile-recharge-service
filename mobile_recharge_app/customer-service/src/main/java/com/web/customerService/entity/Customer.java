package com.web.customerService.entity;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="customerid")
	private Long customerId;
	
	@Column(name="name")
	private String customerName;
	
	@Column(name="emailid")
	private String emailId;
	
	@Column(name="contactnumber")
	private Long contactNumber;
	
	@Column(name="vendorname")
	private String vendorName;
	
	@Column(name="rechargeIds")
	private List<Long> rechargeIds;

	@Column(name="rechargestatus")
	private String rechargeStatus;
	
	@Column(name="active")
	private boolean active;
	
	@Transient
	private List<RechargePlans> rechargePlansList;
}
