package com.web.rechargeService.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import com.web.rechargeService.entity.RechargePlans;
import com.web.rechargeService.externalService.UserService;
import com.web.rechargeService.repository.RechargeServiceRepository;

@ExtendWith(MockitoExtension.class)
public class RechargeServiceTest extends AbstractTestNGSpringContextTests {
	
	@Mock
	RechargeServiceRepository rechargeRepository;
	
	@InjectMocks
	RechargeServiceImpl rechargeServiceImpl;
	
	@Mock
	private UserService userService;
	
	@Test
	public void addRechargePlanFailTest() {
		long userId= 1l;
		RechargePlans rechargeRecord = new RechargePlans();
		rechargeRecord.setRechargeId(1l);
		rechargeRecord.setVendorName("Vodafone");
		rechargeRecord.setDescription("5g speed with 2.5GB daily data and unlimited local/National calls.");
		rechargeRecord.setAmount(999L);
		rechargeRecord.setStatus("ACTIVE");
		String rechargePlansFromService = rechargeServiceImpl.addRechargePlanDetails(userId,rechargeRecord);
		assertNotEquals(rechargePlansFromService, rechargeRecord);
	}
	
	@Test
	public void getRechargeById() {
		RechargePlans rechargePlan = new RechargePlans(3l, "Airtel", 
				"5g speed with 1.5GB daily data and unlimited local/National calls.", 999l,"ACTIVE",2);
		Optional<RechargePlans> optionalRechargePlanRecord = Optional.of(rechargePlan); 
		when(rechargeRepository.findById(rechargePlan.getRechargeId())).thenReturn(optionalRechargePlanRecord);
		RechargePlans rechargePlanResponse = rechargeServiceImpl.findById(rechargePlan.getRechargeId());
		assertEquals(rechargePlanResponse, rechargePlan);
	}
	
	@Test
	public void getRechargeByIdFailTest() {
		RechargePlans rechargePlan = new RechargePlans(399l, "Airtel", 
				"5g speed with 1.5GB daily data and unlimited local/National calls.", 999l,"ACTIVE",2);
		Optional<RechargePlans> optionalRechargePlanRecord = Optional.of(rechargePlan); 
	//	when(rechargeRepository.findById(rechargePlan.getRechargeId())).thenReturn(optionalRechargePlanRecord);
		RechargePlans rechargePlanResponse = rechargeServiceImpl.findById(rechargePlan.getRechargeId());
		assertEquals(rechargePlanResponse, null);
	}
	@Test
	public void getAllRechargePlanDetails() {
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		rechargePlanList.add(new RechargePlans(1L,"JIO","5g speed with 1.5GB daily data and unlimited local/National calls.",499L,"ACTIVE",4));
		rechargePlanList.add(new RechargePlans(3L,"Airtel","5g speed with 1.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",2));
		rechargePlanList.add(new RechargePlans(4l,"Airtel","5g speed with 2.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",1));
		when(rechargeRepository.findAll()).thenReturn(rechargePlanList);
		List<RechargePlans> rechargePlanResponse = rechargeServiceImpl.findAllRechargePlansDetails();
		assertEquals(rechargePlanResponse, rechargePlanList);
	}
	
	@Test
	public void getAllRechargePlanDetailsFailTest() {
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		rechargePlanList.add(new RechargePlans(199L,"JIO","5g speed with 1.5GB daily data and unlimited local/National calls.",499L,"ACTIVE",4));
		rechargePlanList.add(new RechargePlans(399L,"Airtel","5g speed with 1.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",2));
		rechargePlanList.add(new RechargePlans(499l,"Airtel","5g speed with 2.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",1));
		List<RechargePlans> rechargePlanResponse = rechargeServiceImpl.findAllRechargePlansDetails();
		assertNotEquals(rechargePlanResponse, rechargePlanList);
	}
	
	@Test
	public void updateRechargePlanDetailsFailTest() {
		long userId = 2l;
		RechargePlans rechargePlan = new RechargePlans(6l, "Airtel", 
				"5g speed with 1.5GB daily data and unlimited local/National calls.", 999l,"ACTIVE",2);
		String rechargePlanResponse = rechargeServiceImpl.updateRechargePlansDetails(userId,rechargePlan, rechargePlan.getRechargeId());
		assertEquals(rechargePlanResponse, "UNAUTHORIZED");
	}
	
	@Test
	public void deleteRechargePlanFailTest() {
		long userId = 2l;
		long rechargeId = 99l;
		String response = rechargeServiceImpl.deleteRechargePlanDetailsByRechargeId(userId,rechargeId);
		assertEquals(response, "UNAUTHORIZED");
	}
	
	@Test
	public void getVendornameByRechargeIdTest() {
		long rechargeId = 1l;
		String vendorName = "Airtel";
		when(rechargeRepository.getVendorNameByRechargeID(rechargeId)).thenReturn(vendorName);
		String response = rechargeServiceImpl.getVendorNameByRechargeId(rechargeId);
		assertEquals(response, vendorName);
	}
	
	@Test
	public void getVendornameByRechargeIdFailTest() {
		long rechargeId = 99l;
		String vendorName = "Airtel";
		String response = rechargeServiceImpl.getVendorNameByRechargeId(rechargeId);
		assertEquals(response, null);
	}
	
	
	@Test
	public void getAllRechargePlanByIdTest() {
		List<Long> rechargeIds = new ArrayList<>();
		rechargeIds.add(1l);
		rechargeIds.add(3l);
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		rechargePlanList.add(new RechargePlans(1L,"JIO","5g speed with 1.5GB daily data and unlimited local/National calls.",499L,"ACTIVE",4));
		rechargePlanList.add(new RechargePlans(3L,"Airtel","5g speed with 1.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",2));
		when(rechargeRepository.getRechargePlansByRechargeIds(rechargeIds)).thenReturn(rechargePlanList);
		List<RechargePlans> response = rechargeServiceImpl.getAllRechargePlansById(rechargeIds);
		assertEquals(response, rechargePlanList);
	}
	
	@Test
	public void getAllRechargePlanByIdFailTest() {
		List<Long> rechargeIds = new ArrayList<>();
		rechargeIds.add(199l);
		rechargeIds.add(399l);
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		rechargePlanList.add(new RechargePlans(199L,"JIO","5g speed with 1.5GB daily data and unlimited local/National calls.",499L,"ACTIVE",4));
		rechargePlanList.add(new RechargePlans(399L,"Airtel","5g speed with 1.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",2));
		
		List<RechargePlans> response = rechargeServiceImpl.getAllRechargePlansById(rechargeIds);
		assertNotEquals(response, rechargePlanList);
	}
	
	@Test
	public void cancelRechargePlanFailTest() {
		Long rechargeId = 5l;
		String response = rechargeServiceImpl.cancelRechargePlanByCustomer(rechargeId);
		assertEquals(response,null);
	}
	
	@Test
	public void cancelRechargePlanTest() {
		Long rechargeId = 2l;
		RechargePlans rechargeRecord=new RechargePlans(1L,"JIO","5g speed with 1.5GB daily data and unlimited local/National calls.",499L,"ACTIVE",4);
		Optional<RechargePlans> optionalRechargeRecord = Optional.of(rechargeRecord);
		when(rechargeRepository.findById(rechargeId)).thenReturn(optionalRechargeRecord);
		String response = rechargeServiceImpl.cancelRechargePlanByCustomer(rechargeId);
		assertEquals(response,null);
	}
	
	@Test
	public void updateRechargePlanAfterCustomerActivityFailTest() {
		Long rechargeId = 1l;
		RechargePlans rechargeRecord=new RechargePlans(1L,"JIO","5g speed with 1.5GB daily data and unlimited local/National calls.",499L,"ACTIVE",4);
		Optional<RechargePlans> optionalRechargeRecord = Optional.of(rechargeRecord);
		when(rechargeRepository.findById(rechargeId)).thenReturn(optionalRechargeRecord);
		String response = rechargeServiceImpl.updateRechargePlanAfterCustomerActivity(rechargeId);
		assertEquals(response, null);
	}
	
	@Test
	public void getAllActivePlansTest() {
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		List<RechargePlans> dummyRecord = new ArrayList<>();
		rechargePlanList.add(new RechargePlans(1L,"JIO","5g speed with 1.5GB daily data and unlimited local/National calls.",499L,"ACTIVE",4));
		rechargePlanList.add(new RechargePlans(3L,"Airtel","5g speed with 1.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",2));
		rechargePlanList.add(new RechargePlans(4l,"Airtel","5g speed with 2.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",1));
		List<RechargePlans> response = rechargeServiceImpl.getAllByActivePlans();
		assertEquals(response, dummyRecord);
	}
	
	@Test
	public void getAllActivePlansFailTest() {
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		List<RechargePlans> dummyRecord = new ArrayList<>();
		rechargePlanList.add(new RechargePlans(1L,"JIO","5g speed with 1.5GB daily data and unlimited local/National calls.",499L,"ACTIVE",4));
		rechargePlanList.add(new RechargePlans(3L,"Airtel","5g speed with 1.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",2));
		rechargePlanList.add(new RechargePlans(4l,"Airtel","5g speed with 2.5GB daily data and unlimited local/National calls.",999L,"ACTIVE",1));
		List<RechargePlans> response = rechargeServiceImpl.getAllByActivePlans();
		assertEquals(response, dummyRecord);
	}
}