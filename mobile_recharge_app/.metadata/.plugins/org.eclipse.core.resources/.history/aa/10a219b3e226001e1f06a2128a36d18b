package com.web.customerService.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.web.customerService.dto.ApiResponse;
import com.web.customerService.entity.Customer;
import com.web.customerService.entity.RechargePlans;
import com.web.customerService.exception.GlobalExceptionHandler;
import com.web.customerService.exception.ResourceNotFoundException;
import com.web.customerService.externalService.RechargeService;
import com.web.customerService.externalService.UserService;
import com.web.customerService.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RechargeService rechargeServive;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GlobalExceptionHandler handler;

	@Override
	public Customer createCustomer(Customer customer) {
		if(customer!=null) {
		customer.setActive(true);
		Customer savedCustomerRecord = customerRepository.save(customer);
		if(savedCustomerRecord!=null) {
			return savedCustomerRecord;
		}
		}
		return null;
	}

	@Override
	public Customer findCustomerById(Long customerId) {
		Customer customerRecord = null;
		try {
			customerRecord = customerRepository.findById(customerId).orElseThrow(
					() -> new ResourceNotFoundException(" There is no customer record for customerId =" + customerId));
		} catch (ResourceNotFoundException e) {
			ApiResponse response = new ApiResponse();
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setSuccess(false);
			System.out.println(response);
			return null;

		}
		if (customerRecord != null) {
			if (customerRecord.getRechargeIds() != null) {
				ResponseEntity<List<RechargePlans>> response = rechargeServive
						.getRechargePlansListByIds(customerRecord.getRechargeIds());
				if (response.getStatusCode() == HttpStatus.OK) {
					List<RechargePlans> rechargePlanList = response.getBody();
					if (rechargePlanList != null) {
						customerRecord.setRechargePlansList(rechargePlanList);
					}
				}
			}

		}
		return customerRecord;
	}

	@Override
	public Object getAllCustomerDetails(Long userId) {
		ResponseEntity<Boolean> isUserAdmin = userService.isUserAdmin(userId);
		if (isUserAdmin!=null && isUserAdmin.getBody().booleanValue()) {
			List<Customer> customerRecordList = customerRepository.findAll();
			if (customerRecordList != null) {
				for (Customer customerRecord : customerRecordList) {
					if (customerRecord.getRechargeIds()!=null && !customerRecord.getRechargeIds().isEmpty()) {
						ResponseEntity<List<RechargePlans>> response = rechargeServive
								.getRechargePlansListByIds(customerRecord.getRechargeIds());
						if (response.getStatusCode() == HttpStatus.OK) {
							List<RechargePlans> rechargePlanList = response.getBody();
							if (rechargePlanList != null) {
								customerRecord.setRechargePlansList(rechargePlanList);
							}
						}
					}
				}
				return customerRecordList;
			}
			return null;
		}
		return "UNAUTHORIZED";
	}

	@Override
	public Customer updateCustomer(Customer customer, Long customerId) {
		Customer customerExistingRecord = null;
		try {
			customerExistingRecord = customerRepository.findById(customerId).orElseThrow(
					() -> new ResourceNotFoundException(" There is no customer record for customerId =" + customerId));
		} catch (ResourceNotFoundException e) {
			ApiResponse response = new ApiResponse(e.getMessage(), false, HttpStatus.NOT_FOUND);
			return null;

		}
		if (customerExistingRecord != null) {

			Customer updatedCustomerRecord = new Customer();
			updatedCustomerRecord.setCustomerName(customer.getCustomerName() != null ? customer.getCustomerName()
					: customerExistingRecord.getCustomerName());
			updatedCustomerRecord.setEmailId(
					customer.getEmailId() != null ? customer.getEmailId() : customerExistingRecord.getEmailId());
			updatedCustomerRecord.setContactNumber(customer.getContactNumber() != null ? customer.getContactNumber()
					: customerExistingRecord.getContactNumber());
			updatedCustomerRecord.setCustomerId(customerId);
			updatedCustomerRecord.setVendorName(customer.getVendorName() != null ? customer.getVendorName()
					: customerExistingRecord.getVendorName());
			updatedCustomerRecord.setActive(true);
			
			updatedCustomerRecord.setRechargeStatus(customer.getRechargeIds()!=null ? "RECHARGED"
					: customerExistingRecord.getRechargeStatus());
			updatedCustomerRecord.setRechargeIds(customer.getRechargeIds() != null ? customer.getRechargeIds()
					: customerExistingRecord.getRechargeIds());
			updatedCustomerRecord = customerRepository.save(updatedCustomerRecord);
			return updatedCustomerRecord;
		}
		return null;
	}
	
	@Override
	public String subscribingPlan(Long customerId, Long rechargeId) {
		Optional<Customer> customerExistingRecord = customerRepository.findById(customerId);
		if (!customerExistingRecord.isEmpty() && customerExistingRecord.get() != null) {
			List<Long> rechargeIds = new ArrayList<>();
			Customer updatedCustomerRecord = customerExistingRecord.get();
			if (customerExistingRecord.get().getRechargeIds() != null) {
				for (Long rechargePlanId : customerExistingRecord.get().getRechargeIds())
					if (customerExistingRecord.get().getRechargeIds().contains(rechargePlanId)) {
						rechargeIds.add(rechargePlanId);
					}
			}
			if (customerExistingRecord.get().getRechargeIds() != null
					&& !customerExistingRecord.get().getRechargeIds().contains(rechargeId)) {
				rechargeIds.add(rechargeId);
				ResponseEntity<String> response = rechargeServive.updateRechargePlanByCustomerOps(rechargeId);
				if (response.getStatusCode() == HttpStatus.OK && !response.getBody().equals("INACTIVE_PLAN")) {
					updatedCustomerRecord.setRechargeIds(rechargeIds);
					updatedCustomerRecord.setRechargeStatus("RECHARGED");
					updatedCustomerRecord = customerRepository.save(updatedCustomerRecord);
					if (updatedCustomerRecord != null)
						return "UPDATED";
				}
				else
					return "INACTIVE_PLAN";
				
			} else if (customerExistingRecord.get().getRechargeIds() == null) {
				rechargeIds.add(rechargeId);
				ResponseEntity<String> response = rechargeServive.updateRechargePlanByCustomerOps(rechargeId);
				if (response.getStatusCode() == HttpStatus.OK && !response.getBody().equals("INACTIVE_PLAN")) {
					updatedCustomerRecord.setRechargeIds(rechargeIds);
					updatedCustomerRecord.setRechargeStatus("RECHARGED");
					updatedCustomerRecord = customerRepository.save(updatedCustomerRecord);
					if (updatedCustomerRecord != null)
						return "UPDATED";
				}
				else
					return "INACTIVE_PLAN";

			} else
				return "SAME_PLAN";
		}
		return null;
	}

	@Override
	public Customer deleteCustomerDetailsById(Long customerId) {
		Optional<Customer> customerExistingRecord = customerRepository.findById(customerId);
		if(!customerExistingRecord.isEmpty() && customerExistingRecord.get()!=null) {
			Customer updatedCustomerDetails =customerExistingRecord.get();
			updatedCustomerDetails.setActive(false);
			updatedCustomerDetails = customerRepository.save(updatedCustomerDetails);
			return updatedCustomerDetails;
		}
		return null;
	}
	
	public String cancelCustomerRechargePlans(Long rechargeId, Long customerId) {
		Customer customerExistingRecord = null;
		try {
			customerExistingRecord = customerRepository.findById(customerId)
					.orElseThrow(() -> new ResourceNotFoundException("Record Not Found!!"));
		} catch (ResourceNotFoundException e) {

			e.printStackTrace();
		}
		if (customerExistingRecord != null) {
		
			ResponseEntity<String> response = rechargeServive.cancelRechargePlanByCustomer(rechargeId);
			if(response.getStatusCode() == HttpStatus.OK && response.getBody().equals("SUBSCRIBER_0")) {
				return "SUBSCRIBER_0";
			}
			if (response.getStatusCode() == HttpStatus.OK) {
			List<Long> rechargeIds = new ArrayList<>();
			rechargeIds = customerExistingRecord.getRechargeIds();
			rechargeIds.remove(rechargeId);
			customerExistingRecord.setRechargeIds(rechargeIds);
			customerExistingRecord.setRechargeStatus("CANCELLED");
			customerRepository.save(customerExistingRecord);
			
		}
			else
				return "INACTIVE_PLAN";
		}
		return "CANCELLED";

	}
	
	@Override
	public Object totalNumberOfSubscribedUserByVendorName(Long userId, String vendorName) {
		ResponseEntity<Boolean> isUserAdmin = userService.isUserAdmin(userId);
		if (isUserAdmin!=null && isUserAdmin.getBody().booleanValue()) {
			List<Customer> customerList = customerRepository.findByVendorName(vendorName);
			Long totalNoOfSubscribedUsers = 0l;
			for (Customer customer : customerList) {
				if (customer.getRechargeIds() != null && customer.getRechargeIds().size() > 0) {
					totalNoOfSubscribedUsers++;
				}
			}
			return totalNoOfSubscribedUsers;
		}
		return "UNAUTHORIZED";
	}
}
