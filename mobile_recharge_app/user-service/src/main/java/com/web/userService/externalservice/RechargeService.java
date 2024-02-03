package com.web.userService.externalservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.web.userService.entity.RechargePlans;

@FeignClient(name="RECHARGE-SERVICE")
public interface RechargeService {
	
	@PostMapping("/recharge-service/recharge/{userId}")
	public ResponseEntity<Object> addRechargePlanDetails(@PathVariable("userId") Long userId,@RequestBody RechargePlans rechargePlans);
	
	@GetMapping("/recharge-service/recharge/{rechargeId}")
	public ResponseEntity<Object> getRechargePlanDetails(@PathVariable Long rechargeId);
	
	@GetMapping("/recharge-service/recharge/viewAllPlans")
	public ResponseEntity<Object> getAllRechargePlansDetails();
	
	@PutMapping("/recharge-service/recharge/{userId}/update/{rechargeId}")
	public ResponseEntity<Object> updatePlans(@PathVariable("userId") Long userId,@PathVariable("rechargeId") Long rechargeId,@RequestBody RechargePlans rechargePlans);
	
	@DeleteMapping("/recharge-service/recharge/{userId}/delete/{rechargeId}")
	public ResponseEntity<String> deletePlanByRechargeId(@PathVariable("userId") Long userId,@PathVariable Long rechargeId);
	
	@GetMapping("/recharge-service/recharge/activePlans")
	public ResponseEntity<Object> getAllActivePlans();
}
