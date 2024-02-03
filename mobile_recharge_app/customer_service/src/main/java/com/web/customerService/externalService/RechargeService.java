package com.web.customerService.externalService;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.customerService.entity.RechargePlans;

@FeignClient(name="RECHARGE-SERVICE")
public interface RechargeService {
	
	@GetMapping("/recharge-service/recharge/{rechargeId}")
	public ResponseEntity<Object> getRechargePlanDetails(@PathVariable Long rechargeId);
	
	@GetMapping("/recharge-service/recharge/viewAllPlans")
	public ResponseEntity<Object> getAllRechargePlansDetails();
	
	@GetMapping("/recharge-service/recharge/activePlans")
	public ResponseEntity<Object> getAllActivePlans();
	
	@GetMapping("/vendorName/{rechargeId}")
	public ResponseEntity<Object> getVendorNameByRechargeId(@PathVariable Long rechargeId);
	
	@GetMapping("/recharge-service/recharge/")
	public ResponseEntity<List<RechargePlans>> getRechargePlansListByIds(@RequestParam("rechargeIds") List<Long> rechargeIds);
	
	@PutMapping("/recharge-service/recharge/cancelRechargePlanByCustomer/{rechargeId}")
	public ResponseEntity<String> cancelRechargePlanByCustomer(@PathVariable Long rechargeId);
	
	@PutMapping("/recharge-service/recharge/subscribePlan/{rechargeId}")
	public ResponseEntity<String> updateRechargePlanByCustomerOps(@PathVariable Long rechargeId) ;
}