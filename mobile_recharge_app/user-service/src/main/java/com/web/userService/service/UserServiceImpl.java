package com.web.userService.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.web.userService.dto.UserDTO;
import com.web.userService.entity.Customer;
import com.web.userService.entity.RechargePlans;
import com.web.userService.entity.Role;
import com.web.userService.entity.User;
import com.web.userService.exception.ResourceNotFoundException;
import com.web.userService.externalservice.CustomerService;
import com.web.userService.externalservice.RechargeService;
import com.web.userService.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private RechargeService rechargeService;
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public User createUser(UserDTO userDto) {
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setPassword(userDto.getPassword());
		user.setRole(Role.CUSTOMER);
		User savedUser = userRepository.save(user);
		return savedUser;
	}
	
	@Override
	public User findUserById(Long userId) {
		Optional<User> userRecord = userRepository.findById(userId);
		if(!userRecord.isEmpty()) {
			User user = userRecord.get();
			ResponseEntity<Object> response = null;
			
			if(user.getCustomerId()!=null) {
			Object customerDetails =  getCustomerDetailsById(user.getCustomerId());
			user.setCustomerDetails(customerDetails);
			}
			return user;
		}
	
		return null;
	}
	
	public boolean isUserAdmin(Long userId) {
		Optional<User> userRecord = userRepository.findById(userId);
		if(!userRecord.isEmpty()) {
			if(userRecord.get().getRole()== Role.ADMIN)
				return true;
			else
				return false;
		}
		return false;
	}

	
	@Override
	public ResponseEntity<Object> addRechargePlans(Long userId,RechargePlans rechargePlans) {
		if(isUserAdmin(userId)) {
		ResponseEntity<Object> response =null;
		try {
		response = rechargeService.addRechargePlanDetails(userId,rechargePlans);
		logger.info("{}",response);
		if(response==null)
			return null;
		return response;
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
		return ResponseEntity.status(HttpStatus.OK).body(" Recharge Plan Added successfully!!");
		}

		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" UserId : "+userId+" is not authorized for this");
	}

	@Override
	public Object viewRechargePlansById(Long rechargeId) {
		ResponseEntity<Object> response = null;
		try {
			response = rechargeService.getRechargePlanDetails(rechargeId);
			// response =restTemplate.getForObject("http://RECHARGE-SERVICE/recharge-service/recharge/"+rechargeId, Object.class);
			logger.info("{}", response);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (response == null)
				return null;
		}
		return response.getBody();
	}

	@Override
	public Object viewAllRechargePlans() {
		
		ResponseEntity<Object> response = rechargeService.getAllRechargePlansDetails();		
		logger.info("{}",response);
		return response.getBody();
	}
	
	public Object getAllActivePlans(){
		ResponseEntity<Object> response = rechargeService.getAllActivePlans();		
		logger.info("{}",response);
		if(response.getStatusCode()== HttpStatus.OK)
			return response.getBody();
		return null;
	}

	@Override
	public ResponseEntity<String> deleteRechargePlans(Long userId,Long rechargeId) {
		if (isUserAdmin(userId)) {
		ResponseEntity<String> response = rechargeService.deletePlanByRechargeId(userId,rechargeId);
		return response;
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> updateRechargePlanById(Long userId,Long rechargeId, RechargePlans rechargePlans) {
		if (isUserAdmin(userId)) {
			ResponseEntity<Object> response = rechargeService.updatePlans(userId,rechargeId, rechargePlans);
			return response;
		}
		return null;
	}

	@Override
	public String createCustomerDetails(Customer customer) {
		
		ResponseEntity<String>response=null;
		response = customerService.addCustomerDetails(customer);	
		logger.info("{}",response.getBody());
		
		if(response==null)
			return null;
		return response.getBody();
	}

	@Override
	public Object getCustomerDetailsById(Long customerId) {
		ResponseEntity<Object> response = null;
		try {
			response = customerService.getCustomerDetailsById(customerId);
			logger.info("{}", response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response == null)
				return null;
		}
		return response.getBody();
	}

	@Override
	public ResponseEntity<Object> getAllCustomerDetails(Long userId) {
		if (isUserAdmin(userId)) {
			ResponseEntity<Object> response = customerService.getAllCustomerDetails(userId);
			logger.info("{}", response.getBody());
			return response;
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" UserId : "+userId+" is not authorized for this");
	}

	@Override
	public Object updateCustomerDetails(Long customerId, Customer customer) {
		
		//HttpHeaders headers = new HttpHeaders();
	//	HttpEntity<Object> requestEntity = new HttpEntity<>(customer,headers);
	//	ResponseEntity<Object> response = restTemplate.exchange("http://localhost:8083/customer/",HttpMethod.PUT,requestEntity,Object.class, customerId); 
		ResponseEntity<Object> response = customerService.updateCustomerDetails(customerId, customer);
		if(response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}
		return null;
	}

	@Override
	public String deleteCustomerDetailsById(Long customerId) {
		ResponseEntity<String> response = customerService.deleteCustomerDetails(customerId);
		return response.getBody();
	}

	@Override
	public ResponseEntity<String> totalNumberOfSubscribedUserByVendorName(Long userId,String vendorName) {
		if(isUserAdmin(userId)) {
			ResponseEntity<String> response = customerService.getTotalNoOfSubscribedUserByVendorName(userId,vendorName);
			return response;
		}
		return null;
	}

	@Override
	public String cancelCustomerRechargePlans(Long rechargeId, Long customerId) {
		ResponseEntity<String> response = customerService.cancellingCustomerRechargePlans(customerId, rechargeId);
		logger.info("{}",response.getBody());
		return response.getBody();
	}
	
	@Override
	public String loginUser(String username, String password) {
		User user = userRepository.findByUsernameAndPassword(username, password);
		if(user!=null) {
			return " User Login Successfully";
		}
		return "Entered Wrong credentials";
	}
	
	@Override
	public User updateUserDetails(Long userId,User user) {
		Optional<User> userRecord = userRepository.findById(userId);
		if(!userRecord.isEmpty()) {
		User updatedUserRecord = new User(userId,user.getUsername(),user.getPassword(),user.getCustomerId(),user.getRole(),null);
		
			return userRepository.save(updatedUserRecord);
		}
		return null;
	}
	
	public ResponseEntity<String> subscribePlan(Long customerId, Long rechargeId){
		return customerService.subscribePlan(customerId, rechargeId);
	}
}
