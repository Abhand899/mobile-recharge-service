package com.web.userService.service;

import org.springframework.http.ResponseEntity;

import com.web.userService.dto.UserDTO;
import com.web.userService.entity.Customer;
import com.web.userService.entity.RechargePlans;
import com.web.userService.entity.User;
import com.web.userService.exception.ResourceNotFoundException;

public interface UserService {
	
	User createUser(UserDTO userDto);
	ResponseEntity<Object> addRechargePlans(Long userId,RechargePlans rechargePlans);
	Object viewRechargePlansById(Long rechargeId) throws ResourceNotFoundException;
	Object viewAllRechargePlans();
	ResponseEntity<String> deleteRechargePlans(Long userId,Long rechargeId);
	ResponseEntity<Object> updateRechargePlanById(Long userId,Long rechargeById, RechargePlans rechargePlans);
	
	String createCustomerDetails(Customer customer);
	Object getCustomerDetailsById(Long customerId);
	ResponseEntity<Object> getAllCustomerDetails(Long userId);
	Object updateCustomerDetails(Long customerId, Customer customer);
	Object deleteCustomerDetailsById(Long customerId);
	ResponseEntity<String> totalNumberOfSubscribedUserByVendorName(Long userId,String vendorName);
	String loginUser(String username, String password);
	String cancelCustomerRechargePlans(Long rechargeId, Long customerId);
	User updateUserDetails(Long userId, User user);
	User findUserById(Long userId);

}
