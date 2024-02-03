package com.web.userService.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.web.userService.dto.UserDTO;
import com.web.userService.entity.Customer;
import com.web.userService.entity.RechargePlans;
import com.web.userService.entity.User;
import com.web.userService.exception.ResourceNotFoundException;
import com.web.userService.service.UserServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@PostMapping("/register")
	public ResponseEntity<String> createUser(@RequestBody UserDTO userDto){
		User savedUser = userServiceImpl.createUser(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!!");
	}
	
	@GetMapping("/login")
	public ResponseEntity<String> loginUser(@RequestParam("username") String username,@RequestParam("password") String password){
		String message = userServiceImpl.loginUser(username,password);
		return ResponseEntity.status(HttpStatus.OK).body(message);
	}

	@GetMapping("/userInfo/{userId}")
	public ResponseEntity<User> getUserByUserId(@PathVariable("userId") Long userId){
		User user = userServiceImpl.findUserById(userId);
		if(user == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/validateUser/{userId}")
	public ResponseEntity<Boolean> isUserAdmin(@PathVariable("userId") Long userId){
		boolean isUserAdmin = userServiceImpl.isUserAdmin(userId);
		if(isUserAdmin)
			return ResponseEntity.status(HttpStatus.OK).body(Boolean.valueOf(isUserAdmin));
		return ResponseEntity.status(HttpStatus.OK).body(Boolean.valueOf(false));
	}
	
	
	@PutMapping("/update/{userId}")
	public ResponseEntity<String> updateUserDetails(@PathVariable("userId") Long userId,@RequestBody User user){
		User userUpdatedRecord = userServiceImpl.updateUserDetails(userId, user);
		if(userUpdatedRecord == null)
			return ResponseEntity.status(HttpStatus.OK).body("UserId : "+userId+" not updated successfully!!");
		return ResponseEntity.status(HttpStatus.OK).body("UserId : "+userId+" updated successfully!!");
	}
	@PostMapping("/{userId}/rechargePlans")
	@CircuitBreaker(name="rechargeService", fallbackMethod = "rechargeServiceFallback")
	public ResponseEntity<String> addRechargePlans(@PathVariable("userId") Long userId,@RequestBody RechargePlans rechargePlans){
		ResponseEntity<Object> response = userServiceImpl.addRechargePlans(userId,rechargePlans);
		if(response==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" Records not created !!");
		}
		else if(response.getStatusCode()== HttpStatus.UNAUTHORIZED) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" UserId : "+userId+" is not authorized for Accessing this endpoint");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Recharge Record created successfully !!");
	}
	
	public ResponseEntity<String> rechargeServiceFallback( Long userId,  RechargePlans rechargePlans,Exception ex){
		logger.info("Fallback is executed becuase recharge service is down :", ex.getMessage());
		return new ResponseEntity<>("Fallback is executed becuase recharge services is down :"+ ex.getMessage(),HttpStatus.OK);
		
	}
	
	@GetMapping("/rechargePlans/{rechargeId}")
	public ResponseEntity<Object> viewRechargePlansById(@PathVariable Long rechargeId) throws ResourceNotFoundException{
		Object rechargePlans = userServiceImpl.viewRechargePlansById(rechargeId);
		if(rechargePlans == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" No Recharge Plans are for the rechargeId : "+rechargeId);
		}
		return ResponseEntity.status(HttpStatus.OK).body(rechargePlans);
	}
	
	public ResponseEntity<Object> rechargeServiceGetFallback( Exception ex){
		logger.info("Fallback is executed becuase Recharge service is down :", ex.getMessage());
		return new ResponseEntity<>("Fallback is executed becuase Recharge services is down :"+ ex.getMessage(),HttpStatus.OK);
		
	}
	
	@GetMapping("/rechargePlans")
	@CircuitBreaker(name="rechargeServiceGet", fallbackMethod = "rechargeServiceGetFallback")
	public ResponseEntity<Object> viewAllRechargePlans(){
		Object rechargePlansList = userServiceImpl.viewAllRechargePlans();
		if(rechargePlansList == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" No Recharge Plans are there for any Vendor(JIO, Airtel, Vodafone");
		}
		return ResponseEntity.status(HttpStatus.OK).body(rechargePlansList);
	}
	
	@PutMapping("/{userId}/rechargePlans/{rechargeId}")
	public ResponseEntity<Object> updateRechargeDetails(@PathVariable("userId") Long userId,@PathVariable("rechargeId") Long rechargeId, @RequestBody RechargePlans rechargePlans){
		ResponseEntity<Object> response = userServiceImpl.updateRechargePlanById(userId,rechargeId, rechargePlans);
		if(response == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" UserId : "+userId+" is not authorized for Accessing this endpoint");
		}
		return ResponseEntity.status(HttpStatus.OK).body("RechargeId : "+rechargeId+" updated successfully.!!");
	}
	
	@DeleteMapping("/{userId}/rechargePlans/{rechargeId}")
	public ResponseEntity<String> deleteRechargePlanById(@PathVariable("userId") Long userId,@PathVariable Long rechargeId){
		ResponseEntity<String> response = userServiceImpl.deleteRechargePlans(userId,rechargeId);
		if(response == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" UserId : "+userId+" is not authorized for Accessing this endpoint");
		}
		return ResponseEntity.status(HttpStatus.OK).body("RechargeId : "+rechargeId+" deleted successfully!!");
	}
	
	
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<Object> getCustomerDetailsById(@PathVariable Long customerId){
		Object customerServiceResponse	=userServiceImpl.getCustomerDetailsById(customerId);
		if(customerServiceResponse==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" No Customer details found for the customerId : "+customerId);
		}
		return ResponseEntity.status(HttpStatus.OK).body(customerServiceResponse);
	}
	
	public ResponseEntity<Object> customerServiceGetFallBack( Long customerId, Exception ex){
		logger.info("Fallback is executed becuase customer service is down :", ex.getMessage());
		return new ResponseEntity<>("Fallback is executed becuase customer service is down :"+ ex.getMessage(),HttpStatus.OK);
		
	}
	
	@GetMapping("/{userId}/customer")
	@CircuitBreaker(name="custumerServiceGet", fallbackMethod = "customerServiceGetFallBack")
	public ResponseEntity<Object> getAllCustomerDetails(@PathVariable("userId") Long userId){
		ResponseEntity<Object> customerList = userServiceImpl.getAllCustomerDetails(userId);
		if(customerList == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" No Customer Details found!!");
		}
		else if(customerList.getStatusCode()== HttpStatus.UNAUTHORIZED) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" UserId : "+userId+" is not authorized for Accessing this endpoint");
		}
		return ResponseEntity.status(HttpStatus.OK).body(customerList.getBody());
	}
	
	@GetMapping("/{userId}/customer/subscribedUsers")
	public ResponseEntity<String> getTotalNoOfSubscribedUserByVendorName(@PathVariable("userId") Long userId,@RequestParam String vendorName){
		ResponseEntity<String> totalNoOfSubscribedUser = userServiceImpl.totalNumberOfSubscribedUserByVendorName(userId,vendorName);
		if(totalNoOfSubscribedUser==null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" UserId : "+userId+" is not authorized for Accessing this endpoint");
		return ResponseEntity.status(HttpStatus.OK).body(" Total number of subscribed users for "+vendorName+" : "+totalNoOfSubscribedUser.getBody());
	}
	
	@PostMapping("/customer")
	public ResponseEntity<String> createCustomerDetailsForUsers(@RequestBody Customer customer){
		Object obj = userServiceImpl.createCustomerDetails(customer);
		if(obj==null) {
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(" Records not created !!");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Customer Record created successfully !!");
	}
	
	@PutMapping("/customer/{customerId}")
	public ResponseEntity<Object> updateCustomerDetails(@PathVariable Long customerId, @RequestBody Customer customer){
		Object response = userServiceImpl.updateCustomerDetails(customerId, customer);
		if(response == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CustomerId : "+customerId+" not updated.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping("/customer/{customerId}")
	public ResponseEntity<Object> deleteCustomerById(@PathVariable Long customerId){
		String  message= userServiceImpl.deleteCustomerDetailsById(customerId);
		if(message==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CustomerId : "+customerId+" not updated.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(message);
	}
	
	@GetMapping("/customer/{customerId}/cancellingRechargePlan/{rechargeId}")
	public ResponseEntity<String> cancellingCustomerRechargePlans(@PathVariable("customerId") Long customerId,@PathVariable("rechargeId") Long rechargeId){
		String response= userServiceImpl.cancelCustomerRechargePlans(rechargeId, customerId);
		if(response==null) {
			return ResponseEntity.status(HttpStatus.OK).body("CustomerId : "+customerId +" for the recharge plan rechargeId="+rechargeId+ " not Cancelled!!");
		}
		return ResponseEntity.status(HttpStatus.OK).body("CustomerId : "+customerId +" for the recharge plan rechargeId="+rechargeId+ " cancelled successfully!!");
	}
	
	@PutMapping("/customer/{customerId}/subscribePlan/{rechargeId}")
	public ResponseEntity<String> subscribePlan(@PathVariable("customerId") Long customerId,@PathVariable("rechargeId") Long rechargeId){
		return userServiceImpl.subscribePlan(customerId, rechargeId);
	}
	
	@GetMapping("/rechargePlans/activePlans")
	public ResponseEntity<Object> getAllByActivePlans(){
		Object response =  userServiceImpl.getAllActivePlans();
		if(response == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" No Recharge Plans are there for any Vendor(JIO, Airtel, Vodafone");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
