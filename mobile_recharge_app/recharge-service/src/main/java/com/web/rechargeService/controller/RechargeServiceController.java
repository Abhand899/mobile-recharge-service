package com.web.rechargeService.controller;

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

import com.web.rechargeService.entity.RechargePlans;
import com.web.rechargeService.exceptions.ResourceNotFoundException;
import com.web.rechargeService.service.RechargeServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/recharge-service/recharge/")
public class RechargeServiceController {
	
	@Autowired
	private RechargeServiceImpl rechargeServiceImpl;
	
	@PostMapping("{userId}")
	@CircuitBreaker(name="rechargeServiceAdd", fallbackMethod = "addRechargeFallbackMethod")
	public ResponseEntity<Object> addRechargePlanDetails(@PathVariable("userId") long userId,@RequestBody RechargePlans rechargePlans){
		String response = rechargeServiceImpl.addRechargePlanDetails(userId,rechargePlans);
		if(response==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" record not created");
		}
		else if(response.equals("UNAUTHORIZED"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserId : "+userId+" is not authorize  for this operation");
		return ResponseEntity.status(HttpStatus.CREATED).body("Record created successfully");
		
	}
	
	public ResponseEntity<Object> addRechargeFallbackMethod(long userId, RechargePlans rechargePlans,Exception ex){
		return new ResponseEntity<>("Fallback is executed becuase user-service is down :"+ ex.getMessage(),HttpStatus.OK);
	}
	
	@GetMapping("/{rechargeId}")
	public ResponseEntity<Object> getRechargePlanDetails(@PathVariable Long rechargeId) throws ResourceNotFoundException{
		RechargePlans rechargePlanSavedRecords = rechargeServiceImpl.findById(rechargeId);
		if(rechargePlanSavedRecords==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" record not found for RechargeID :  "+rechargeId);
		}
		return ResponseEntity.status(HttpStatus.OK).body(rechargePlanSavedRecords);
		
	}
	
	@GetMapping("/vendorName/{rechargeId}")
	public ResponseEntity<Object> getVendorNameByRechargeId(@PathVariable Long rechargeId){
		String vendorName = rechargeServiceImpl.getVendorNameByRechargeId(rechargeId);
		if(vendorName == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" record not found for RecargeID :  "+rechargeId);
		}
		return ResponseEntity.status(HttpStatus.OK).body(vendorName);
		
	}
	
	@GetMapping("/viewAllPlans")
	public ResponseEntity<Object> getAllRechargePlansDetails(){
		List<RechargePlans> rechargePlansList = rechargeServiceImpl.findAllRechargePlansDetails();
		if(rechargePlansList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" No Recharge Plans are there for any Vendor(JIO, Airtel, Vodafone");
		}
		return ResponseEntity.status(HttpStatus.OK).body(rechargePlansList);
	}
	
	@GetMapping("/byVendorName")
	public ResponseEntity<Object> getAllPlansByVendorname(@RequestParam("vendorname") String vendorname){
		List<RechargePlans> rechargePlansList = rechargeServiceImpl.getAllPlansByVendorname(vendorname);
		if(rechargePlansList!=null)
			return ResponseEntity.status(HttpStatus.OK).body(rechargePlansList);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" No Plans are there for Vendorname : "+vendorname+"!!");
	}
	
	@PutMapping("/{userId}/update/{rechargeId}")
	@CircuitBreaker(name="rechargeServiceUpdate", fallbackMethod = "updateRechargeFallbackMethod")
	public ResponseEntity<Object> updatePlans(@PathVariable("userId") Long userId,@PathVariable Long rechargeId,@RequestBody RechargePlans rechargePlans){
		String response = rechargeServiceImpl.updateRechargePlansDetails(userId,rechargePlans, rechargeId);
		if(response==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("RechargeId : "+rechargeId+" record not updated!!");
		}
		else if(response.equals("UNAUTHORIZED"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserId : "+userId+" is not authorize  for this operation");
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	public ResponseEntity<Object> updateRechargeFallbackMethod(Long userId,Long rechargeId, RechargePlans rechargePlans,Exception ex){
		return new ResponseEntity<>("Fallback is executed becuase user-service is down :"+ ex.getMessage(),HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}/delete/{rechargeId}")
	@CircuitBreaker(name="rechargeServiceDelete", fallbackMethod = "deleteRechargeFallbackMethod")
	public ResponseEntity<String> deletePlanByRechargeId(@PathVariable("userId") Long userId,@PathVariable Long rechargeId){
		String response = rechargeServiceImpl.deleteRechargePlanDetailsByRechargeId(userId,rechargeId);
		if(response == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserId : "+userId+" is not there in DB.");
		else if(response.equals("UNAUTHORIZED"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserId : "+userId+" is not authorize  for this operation");
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	public ResponseEntity<String> deleteRechargeFallbackMethod(Long userId,Long rechargeId,Exception ex){
		return new ResponseEntity<>("Fallback is executed becuase user-service is down :"+ ex.getMessage(),HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<RechargePlans>> getRechargePlansListByIds(@RequestParam("rechargeIds") List<Long> rechargeIds){
		List<RechargePlans> rechargePlansList = rechargeServiceImpl.getAllRechargePlansById(rechargeIds);
		return ResponseEntity.status(HttpStatus.OK).body(rechargePlansList);
	}
	
	@PutMapping("/cancelRechargePlanByCustomer/{rechargeId}")
	public ResponseEntity<String> cancelRechargePlanByCustomer(@PathVariable Long rechargeId){
		String message = rechargeServiceImpl.cancelRechargePlanByCustomer(rechargeId);
		if(message!=null)
			return ResponseEntity.status(HttpStatus.OK).body(message);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" RechargePlan Not Updated!!");
	}
	
	@PutMapping("/subscribePlan/{rechargeId}")
	public ResponseEntity<String> updateRechargePlanByCustomerOps(@PathVariable Long rechargeId) {
		String response = rechargeServiceImpl.updateRechargePlanAfterCustomerActivity(rechargeId);
		if (response == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record Not Found for the rechargeId : "+rechargeId);
		else if(response.equals("SUBSCRIBER_0"))
			return ResponseEntity.status(HttpStatus.OK).body("SUBSCRIBER_0");
		else if (response.equals("INACTIVE"))
			return ResponseEntity.status(HttpStatus.OK)
					.body("INACTIVE_PLAN");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/activePlans")
	public ResponseEntity<Object> getAllActivePlans(){
		List<RechargePlans> rechargePlansList = rechargeServiceImpl.getAllByActivePlans();
		if(rechargePlansList==null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" There are no records in DB!!");
		return ResponseEntity.status(HttpStatus.OK).body(rechargePlansList);
	}
	
}