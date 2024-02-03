package com.web.customerService.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.web.customerService.entity.Customer;
import com.web.customerService.entity.RechargePlans;
import com.web.customerService.externalService.RechargeService;
import com.web.customerService.externalService.UserService;
import com.web.customerService.repository.CustomerRepository;
import com.web.customerService.service.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest extends AbstractTestNGSpringContextTests{

	@Mock
	private CustomerRepository customerRepository;
	
	@InjectMocks
	private CustomerServiceImpl customerServiceImpl;
	
	@Mock
	private UserService userService;
	
	@Mock
	RechargeService rechargeService;
	
	@Test
	public void createCustomer() {
		Customer customer = new Customer();
		customer.setCustomerId(3l);
		customer.setActive(true);
		customer.setCustomerName("Karan");
		customer.setEmailId("dacac");
		customer.setRechargePlansList(null);
		customer.setVendorName("Airtel");
		customer.setContactNumber(7567567l);
		customer.setRechargeStatus("RECHARGED");
		List<Long> rechargeIds = new ArrayList<>();
		rechargeIds.add(1l);
		customer.setRechargeIds(rechargeIds);
		when(customerRepository.save(customer)).thenReturn(customer);
		Customer response = customerServiceImpl.createCustomer(customer);
		
		assertEquals(response,customer);
	}
	
	@Test
	public void createCustomerFailTest() {
		Customer response = customerServiceImpl.createCustomer(null);
		assertEquals(response,null);
	}
	
	@Test
	public void getCustomerbyIdFailTest() {
		Customer customer = new Customer();
		customer.setCustomerId(3l);
		customer.setActive(true);
		customer.setCustomerName("Karan");
		customer.setEmailId("dacac");
		customer.setRechargePlansList(null);
		customer.setVendorName("Airtel");
		customer.setContactNumber(7567567l);
		customer.setRechargeStatus("RECHARGED");
		List<Long> rechargeIds = new ArrayList<>();
		rechargeIds.add(1l);
		Customer response  = customerServiceImpl.findCustomerById(customer.getCustomerId());
		assertEquals(null, response);
	}
	
	@Test
	public void getCustomerbyIdTest() {
		Customer customer = new Customer();
		customer.setCustomerId(399l);
		customer.setActive(true);
		customer.setCustomerName("Karan");
		customer.setEmailId("dacac");
		customer.setRechargePlansList(null);
		customer.setVendorName("Airtel");
		customer.setContactNumber(7567567l);
		customer.setRechargeStatus("RECHARGED");
		List<Long> rechargeIds = new ArrayList<>();
		rechargeIds.add(1l);
		Optional<Customer> customerRecord = Optional.of(customer);
		when(customerRepository.findById(customer.getCustomerId())).thenReturn(customerRecord);
		Customer response  = customerServiceImpl.findCustomerById(customer.getCustomerId());
		assertEquals(customer, response);
	}
	
	@Test
	public void getAllCustomerTest(){
		Long userId = 2l;
		Object response =  customerServiceImpl.getAllCustomerDetails(userId);
		assertEquals(response, "UNAUTHORIZED");
	}

	
	@Test
	public void updateCustomerFailTest() {
		Customer customer = new Customer(39l,"Karan","dacac",7567567l,"Airtel",null,null,true,null);
		customer.setRechargeStatus("RECHARGED");
		List<Long> rechargeIds = new ArrayList<>();
		rechargeIds.add(1l);
		customer.setRechargeIds(rechargeIds);
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		RechargePlans rechargeRecord = new RechargePlans();
		rechargeRecord.setRechargeId(1l);;
		rechargeRecord.setVendorName("Vodafone");
		rechargeRecord.setDescription("5g speed with 2.5GB daily data and unlimited local/National calls.");
		rechargeRecord.setAmount(999L);
		rechargeRecord.setStatus("ACTIVE");
		rechargePlanList.add(rechargeRecord);
		customer.setRechargePlansList(rechargePlanList);
		Customer response = customerServiceImpl.updateCustomer(customer, customer.getCustomerId());
		assertEquals(response, null);
	}
	
	
	@Test
	public void subscribePlanFailTest() {
		long customerId = 3l;
		long rechargeId = 1l;
		String response  = customerServiceImpl.subscribingPlan(customerId, rechargeId);
		assertEquals(response, null);
	}
	
	@Test
	public void cancelCustomerRechargePlanFailTest() {
		long customerId=31l;
		long rechargeId = 1l;
		Customer customer = new Customer();
		customer.setCustomerId(31l);
		customer.setActive(true);
		customer.setCustomerName("Karan");
		customer.setEmailId("dacac");
		customer.setRechargePlansList(null);
		customer.setVendorName("Airtel");
		customer.setContactNumber(7567567l);
		customer.setRechargeStatus("RECHARGED");
		List<Long> rechargeIds = new ArrayList<>();
		rechargeIds.add(1l);
		customer.setRechargeIds(rechargeIds);
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		RechargePlans rechargeRecord = new RechargePlans();
		rechargeRecord.setRechargeId(1l);;
		rechargeRecord.setVendorName("Vodafone");
		rechargeRecord.setDescription("5g speed with 2.5GB daily data and unlimited local/National calls.");
		rechargeRecord.setAmount(999L);
		rechargeRecord.setStatus("ACTIVE");
		rechargePlanList.add(rechargeRecord);
		customer.setRechargePlansList(rechargePlanList);
		String response = customerServiceImpl.cancelCustomerRechargePlans( rechargeId,customerId);
		assertEquals(response, "CANCELLED");
	}
	
	
	@Test
	public void deletePlanFailtest() {
		long customerId=31l;
		long rechargeId = 1l;
		Customer customer = new Customer();
		customer.setCustomerId(3l);
		customer.setActive(true);
		customer.setCustomerName("Karan");
		customer.setEmailId("dacac");
		customer.setRechargePlansList(null);
		customer.setVendorName("Airtel");
		customer.setContactNumber(7567567l);
		customer.setRechargeStatus("RECHARGED");
		List<Long> rechargeIds = new ArrayList<>();
		rechargeIds.add(1l);
		customer.setRechargeIds(rechargeIds);
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		RechargePlans rechargeRecord = new RechargePlans();
		rechargeRecord.setRechargeId(1l);;
		rechargeRecord.setVendorName("Vodafone");
		rechargeRecord.setDescription("5g speed with 2.5GB daily data and unlimited local/National calls.");
		rechargeRecord.setAmount(999L);
		rechargeRecord.setStatus("ACTIVE");
		rechargePlanList.add(rechargeRecord);
		customer.setRechargePlansList(rechargePlanList);
		Customer response = customerServiceImpl.deleteCustomerDetailsById(customerId);
		assertEquals(response, null);
	}

	@Test
	public void getTotalNoOfSubscribedUserByVendorNameFailTest() {
		Long userId = 3l;
		String vendorName="Vodafone";
		Object response = customerServiceImpl.totalNumberOfSubscribedUserByVendorName(userId,vendorName);
		assertEquals(response, "UNAUTHORIZED");
	}
}
