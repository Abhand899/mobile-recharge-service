package com.web.customerService.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.customerService.entity.Customer;
import com.web.customerService.service.CustomerServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerServiceImpl customerServiceImpl;
	
	@PostMapping
	public ResponseEntity<String> addCustomerDetails(@RequestBody Customer customer){
		Customer customerSavedRecord = customerServiceImpl.createCustomer(customer);
		if(customerSavedRecord==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" record not created!!");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("CusotmerId : "+customerSavedRecord.getCustomerId()+" saved successfully!!");
		
	}
	
	@GetMapping("/allCustomerInfo/{userId}")
	public ResponseEntity<Object> getAllCustomerDetails(@PathVariable("userId") Long userId){
		Object response = customerServiceImpl.getAllCustomerDetails(userId);
		if(response==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" No Customer Details Are there.");
		}
		else if(response.equals("UNAUTHORIZED"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" UsedId : "+userId+" is not allowed to view all customerRecords");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	public ResponseEntity<Object> rechargeServiceGetFallback(Long customerId, Exception ex){
		return new ResponseEntity<>("Fallback is executed becuase Recharge services is down :"+ ex.getMessage(),HttpStatus.OK);
		
	}
	
	@GetMapping("/{customerId}")
	@CircuitBreaker(name="rechargeServiceGet", fallbackMethod = "rechargeServiceGetFallback")
	public ResponseEntity<Object> getCustomerDetailsById(@PathVariable Long customerId){
		Customer customerRecord = customerServiceImpl.findCustomerById(customerId);
		if(customerRecord==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" record not found for customerId :  "+customerId);
		}
		return ResponseEntity.status(HttpStatus.OK).body(customerRecord);
		
	}
	
	@PutMapping("/{customerId}")
	public ResponseEntity<Object> updateCustomerDetails(@PathVariable Long customerId,@RequestBody Customer customer){
		Customer updatedCustomerRecord = customerServiceImpl.updateCustomer(customer, customerId);
		if(updatedCustomerRecord==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CustomerId : "+customerId+" record not updated!!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(updatedCustomerRecord);
		
	}
	
	@GetMapping("/welcomeInfo")
	public ResponseEntity<String> welcomeInfo(){
		return ResponseEntity.status(HttpStatus.OK).body("Welcome to Customer Service!!");
	}
	
	@DeleteMapping("/{customerId}")
	public ResponseEntity<String> deleteCustomerDetails(@PathVariable Long customerId){
		Customer customer = customerServiceImpl.deleteCustomerDetailsById(customerId);
		if(customer!=null) {
			if(!customer.isActive() ) {
				return ResponseEntity.status(HttpStatus.OK).body("CustomerId : "+customerId +" deleted successfully!!");
			}
			else {
				return ResponseEntity.status(HttpStatus.OK).body("CustomerId : "+customerId +"  not deleted!!");
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CustomerId : "+customerId +" not found in the database !!");
	}
	
	@GetMapping("/{userId}/subscribedUsers")
	public ResponseEntity<String> getTotalNoOfSubscribedUserByVendorName(@PathVariable("userId") Long userId,@RequestParam String vendorName){
		Object totalNoOfSubscribedUserResponse = customerServiceImpl.totalNumberOfSubscribedUserByVendorName(userId,vendorName);
		if(totalNoOfSubscribedUserResponse==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Info available!");
		}
		if(totalNoOfSubscribedUserResponse.equals("UNAUTHORIZED"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserId : " + userId + " is not allowed to view this info");
		return ResponseEntity.status(HttpStatus.OK).body("Total Number of subscriber for "+vendorName+" is "+totalNoOfSubscribedUserResponse);
	}
	
	@PutMapping("/{customerId}/cancellingRechargePlan/{rechargeId}")
	public ResponseEntity<String> cancellingCustomerRechargePlans(@PathVariable("customerId") Long customerId,
			@PathVariable("rechargeId") Long rechargeId) {
		Object customerServiceResponse = customerServiceImpl.cancelCustomerRechargePlans(rechargeId, customerId);
		if (customerServiceResponse == null) {
			return ResponseEntity.status(HttpStatus.OK).body("CustomerId : " + customerId
					+ " for the recharge plan rechargeId=" + rechargeId + " not Cancelled!!");
		}
		else if(customerServiceResponse.equals("SUBSCRIBER_0"))
			return ResponseEntity.status(HttpStatus.OK).body("There are zero subscriber and it cannot be less than zero!!");
		return ResponseEntity.status(HttpStatus.OK).body("CustomerId : " + customerId
				+ " for the recharge plan rechargeId=" + rechargeId + " cancelled successfully!!");
	}
	
	@PutMapping("/{customerId}/subscribePlan/{rechargeId}")
	public ResponseEntity<String> subscribePlan(@PathVariable Long customerId, @PathVariable Long rechargeId){
		String response = customerServiceImpl.subscribingPlan(customerId, rechargeId);
		if(response == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CustomerId : "+customerId +" is not there in DB");
		else if(response.equals("UPDATED"))
			return ResponseEntity.status(HttpStatus.OK).body("CustomerId : "+customerId +" for the recharge plan rechargeId="+rechargeId+ " subscribed successfully!!");
		else if (response.equals("INACTIVE_PLAN"))
			return ResponseEntity.status(HttpStatus.OK).body(
					"You cannot subscribe this plan as this plan is currently inactive and currently there are five subscriber for this plan!!");	
		else if(response.equals("SAME_PLAN"))
			return ResponseEntity.status(HttpStatus.OK).body("CustomerId : "+customerId +" for the recharge plan rechargeId="+rechargeId+ " is already subscribed !!");
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(response);
	}
}
