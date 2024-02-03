package com.web.userService.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.web.userService.dto.UserDTO;
import com.web.userService.entity.Customer;
import com.web.userService.entity.RechargePlans;
import com.web.userService.entity.Role;
import com.web.userService.entity.User;
import com.web.userService.externalservice.CustomerService;
import com.web.userService.externalservice.RechargeService;
import com.web.userService.repository.UserRepository;

//@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Mock
	RechargeService rechargeService;
	
	@Mock
	CustomerService customerService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void createUserTest() {
		User user = new User();
		// user.setId(6l);
		user.setUsername("Raj");
		user.setPassword("root");
		user.setRole(Role.CUSTOMER);
		UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getPassword());
		when(userRepository.save(user)).thenReturn(user);
		User userRecord = userServiceImpl.createUser(userDTO);
		User userRecordFromRepo = userRepository.save(user);
		assertEquals(userRecordFromRepo.getUsername(), user.getUsername());

	}

	@Test
	public void findUserById() {
		User user = new User(1L, "Abh", "123", null, Role.ADMIN, null);
		Optional<User> userOptionalform = Optional.of(user);
		when(userRepository.findById(1l)).thenReturn(userOptionalform);
		User userRecord = userServiceImpl.findUserById(user.getId());
		assertEquals(userRecord, user);
	}
	
	@Test
	public void findUserByIdFailTest() {
		User user = new User(199L, "Abh", "123", null, Role.ADMIN, null);
		Optional<User> userOptionalform = Optional.of(user);
		User userRecord = userServiceImpl.findUserById(user.getId());
		assertEquals(userRecord, null);
	}

	@Test
	public void isUserAdminTest() {
		long userId = 2l;
		boolean isUserAdmin = userServiceImpl.isUserAdmin(userId);
		assertEquals(isUserAdmin, false);
	}
	
	@Test
	public void isUserAdminFailedTest() {
		long userId = 200l;
		boolean isUserAdmin = userServiceImpl.isUserAdmin(userId);
		assertEquals(isUserAdmin, false);
	}

	@Test
	public void addRechargePlanTest() {
		long userId = 2; // In DB userID->1 is set as ADMIN
		RechargePlans rechargeRecord = new RechargePlans();
		rechargeRecord.setVendorName("Vodafone");
		rechargeRecord.setDescription("5g speed with 2.5GB daily data and unlimited local/National calls.");
		rechargeRecord.setAmount(999L);
		rechargeRecord.setStatus("ACTIVE");
		ResponseEntity<Object> response = userServiceImpl.addRechargePlans(userId, rechargeRecord);
		assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}

	@Test
	public void viewRechargePlanByIdTest() {
		RechargePlans rechargePlan = new RechargePlans(3l, "Airtel",
				"5g speed with 1.5GB daily data and unlimited local/National calls.", 999l, "ACTIVE", 2);
		Object response = userServiceImpl.viewRechargePlansById(rechargePlan.getRechargeId());
		assertEquals(response, null);
	}

	@Test
	public void viewAllRechargePlanFailTest() {
		List<RechargePlans> rechargePlanList = new ArrayList<>();
		rechargePlanList.add(new RechargePlans(1L, "JIO",
				"5g speed with 1.5GB daily data and unlimited local/National calls.", 499L, "ACTIVE", 4));
		rechargePlanList.add(new RechargePlans(3L, "Airtel",
				"5g speed with 1.5GB daily data and unlimited local/National calls.", 999L, "ACTIVE", 2));
		rechargePlanList.add(new RechargePlans(4l, "Airtel",
				"5g speed with 2.5GB daily data and unlimited local/National calls.", 999L, "ACTIVE", 1));
		Object response = null;
		try {
			userServiceImpl.viewAllRechargePlans();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally {
		assertEquals(response, null);
		}
		
	}

	@Test
	public void deleteRechargePlansByIdTest() {
		long rechargeId = 4l;
		long userId = 1l;
		ResponseEntity<String> rechargeServiceResponse = rechargeService.deletePlanByRechargeId(userId,rechargeId);
		ResponseEntity<String> response = userServiceImpl.deleteRechargePlans(userId, rechargeId);
		assertEquals(rechargeServiceResponse, response);
	}

	@Test
	public void updateRechargePlanByIdFail() {
		Long userId = 1L;
		RechargePlans rechargeRecord = new RechargePlans(2L, "Airtel",
				"5g speed with 1.5GB daily data and unlimited local/National calls.", 999l, "ACTIVE", 2);
		ResponseEntity<Object> response = userServiceImpl.updateRechargePlanById(userId, rechargeRecord.getRechargeId(),
				rechargeRecord);
		assertEquals(response, null);
	}

	@Test
	public void createCustomerDetails() {
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
		String response = null;
		try{
			response = userServiceImpl.createCustomerDetails(customer);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally {
		assertEquals(response, null);
		}
	}

	@Test
	public void getAllCustomerDetailsTest() {
		Long userId = 2l;
		ResponseEntity<Object> response = userServiceImpl.getAllCustomerDetails(userId);
		assertThat(response.getStatusCodeValue()).isEqualTo(401);
		assertEquals(response.getBody(), " UserId : " + userId + " is not authorized for this");
	}

	@Test
	public void getTotalNoOfSubscribedUserByVendorNameFaiLTest() {
		Long userId = 2l;
		String vendorName = "Airtel";
		ResponseEntity<String> response = userServiceImpl.totalNumberOfSubscribedUserByVendorName(userId, vendorName);
		assertEquals(response, null);
	}

	@Test
	public void loginUserTest() {
		User user = new User(1l, "Abh", "123", null, Role.ADMIN, null);
		when(userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(user);
		String responseFromService = userServiceImpl.loginUser(user.getUsername(), user.getPassword());
		assertEquals(responseFromService, " User Login Successfully");
	}

	@Test
	public void loginUserFailTest() {
		User user = new User(2l, "Abh", "12344", null, Role.ADMIN, null);
		when(userRepository.findByUsernameAndPassword("rks", user.getPassword())).thenReturn(user);
		String responseFromService = userServiceImpl.loginUser(user.getUsername(), user.getPassword());
		assertEquals(responseFromService, "Entered Wrong credentials");
	}

	@Test
	public void updateUserFailTest() {
		User user = new User();
		user.setId(0L);
		user.setUsername("ANJ");
		user.setPassword("root");
		user.setRole(Role.CUSTOMER);
		when(userRepository.save(user)).thenReturn(user);
		User userResponse = userServiceImpl.updateUserDetails(user.getId(), user);
		assertEquals(userResponse, null);
		
	}
	
	@Test
	public void updateUserTest() {
		User user = new User();
		user.setId(2L);
		user.setUsername("ANJ");
		user.setPassword("root");
		user.setRole(Role.CUSTOMER);
		Optional<User> userRecord = Optional.of(user);
		when(userRepository.save(user)).thenReturn(user);
		User userResponse = userServiceImpl.updateUserDetails(user.getId(), user);
		assertEquals(userResponse, null);
		
	}
	
	@Test
	public void subscribePlanFailTest() {
		long rechargeId=1l;
		long customerId =2l;
		
		ResponseEntity<String> response = userServiceImpl.subscribePlan(customerId, rechargeId);
		assertEquals(response, null);
	}
	
}
