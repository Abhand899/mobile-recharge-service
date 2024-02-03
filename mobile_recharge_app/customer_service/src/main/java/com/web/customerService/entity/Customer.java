package com.web.customerService.entity;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
