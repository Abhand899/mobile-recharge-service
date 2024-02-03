package com.web.userService.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RechargePlans {

	private Long rechargeId;
	private String vendorName;
	private String description;
	private Long amount;
	private String status;
	private int subscriberCount;
	
}
