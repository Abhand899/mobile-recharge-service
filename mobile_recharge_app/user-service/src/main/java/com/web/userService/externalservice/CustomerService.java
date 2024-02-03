package com.web.userService.externalservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.userService.entity.Customer;

@FeignClient(name="CUSTOMER-SERVICE")
public interface CustomerService {

	@PostMapping("/customer")
	public ResponseEntity<String> addCustomerDetails(@RequestBody Customer customer);
	
	@GetMapping("/customer/allCustomerInfo/{userId}")
	public ResponseEntity<Object> getAllCustomerDetails(@PathVariable("userId") Long userId);
	
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<Object> getCustomerDetailsById(@PathVariable("customerId") Long customerId);
	
	@GetMapping("/customer/{userId}/subscribedUsers")
	public ResponseEntity<String> getTotalNoOfSubscribedUserByVendorName(@PathVariable Long userId, @RequestParam String vendorName);
	
	@GetMapping("/customer/{customerId}/cancellingRechargePlan/{rechargeId}")
	public ResponseEntity<String> cancellingCustomerRechargePlans(@PathVariable("customerId") Long customerId,@PathVariable("rechargeId") Long rechargeId);
	
	@DeleteMapping("/customer/{customerId}")
	public ResponseEntity<String> deleteCustomerDetails(@PathVariable Long customerId);
	
	@PutMapping("/customer/{customerId}")
	public ResponseEntity<Object> updateCustomerDetails(@PathVariable Long customerId,@RequestBody Customer customer);
	
	@PutMapping("/customer/{customerId}/subscribePlan/{rechargeId}")
	public ResponseEntity<String> subscribePlan(@PathVariable Long customerId, @PathVariable Long rechargeId);
}