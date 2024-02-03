package com.web.customerService.service;

import com.web.customerService.entity.Customer;

public interface CustomerService {
	
	public Customer createCustomer(Customer customer);
	
	public Customer findCustomerById(Long customerId);
	
	public Object getAllCustomerDetails(Long userId);
	
	public Customer updateCustomer(Customer customer, Long customerId);
	
	public Customer deleteCustomerDetailsById(Long customerId);

	public String subscribingPlan(Long customerId, Long rechargeId);

	public Object totalNumberOfSubscribedUserByVendorName(Long userId, String vendorName);
}
