package com.web.userService.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	
	private Long customerId;
	private String customerName;
	private String emailId;
	private Long contactNumber;
	private String vendorName;
	private List<Long> rechargeIds;
	private String rechargeStatus;
	private boolean active;
	private List<RechargePlans> rechargePlansList;

}

