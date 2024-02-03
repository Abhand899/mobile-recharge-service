package com.web.rechargeService.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="recharge_plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RechargePlans {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="rechargeid")
	private Long rechargeId;
	
	@Column(name="vendorname")
	private String vendorName;
	
	@Column(name="description")
	private String description;
	
	@Column(name="amount")
	private Long amount;
	
	@Column(name="status")
	private String status;
	
	@Column(name="subscriber_count")
	private int subscriberCount;
	
	
}
