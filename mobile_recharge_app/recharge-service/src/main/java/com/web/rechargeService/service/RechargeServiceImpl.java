package com.web.rechargeService.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.web.rechargeService.entity.RechargePlans;
import com.web.rechargeService.exceptions.ResourceNotFoundException;
import com.web.rechargeService.externalService.UserService;
import com.web.rechargeService.repository.RechargeServiceRepository;

@Service
public class RechargeServiceImpl implements RechargeService {

	@Autowired
	private RechargeServiceRepository rechargeServiceRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public RechargePlans findById(Long rechargeId) {
		RechargePlans rechargePlanRecord=null;
		try {
			rechargePlanRecord = rechargeServiceRepository.findById(rechargeId).orElseThrow(()-> new ResourceNotFoundException(" record not found for RecargeID :  "+rechargeId));
		} catch (ResourceNotFoundException e) {
			
			e.printStackTrace();
		}
		finally {
			if(rechargePlanRecord==null)
			{
				return null;
			}
		}
		return rechargePlanRecord ;
	}

	@Override
	public List<RechargePlans> findAllRechargePlansDetails() {
		List<RechargePlans> rechargePlansList =rechargeServiceRepository.findAll();
		if(rechargePlansList!=null) {
			return rechargePlansList;
		}
		return null;
	}

	@Override
	public String addRechargePlanDetails(Long userId, RechargePlans rechargePlans) {
		ResponseEntity<Boolean> isUserAdmin = userService.isUserAdmin(userId);
		if (isUserAdmin!=null && isUserAdmin.getBody().booleanValue()) {
			rechargePlans.setStatus("ACTIVE");
			RechargePlans rechargePlansSavedRecord = rechargeServiceRepository.save(rechargePlans);
			if (rechargePlansSavedRecord != null) {
				return "CREATED";
			}
			return null;
		}
		return "UNAUTHORIZED";
	}

	@Override
	public String updateRechargePlansDetails(Long userId, RechargePlans rechargePlans, Long rechargeId) {
		ResponseEntity<Boolean> isUserAdmin = userService.isUserAdmin(userId);
		if (isUserAdmin!=null && isUserAdmin.getBody().booleanValue()) {
			Optional<RechargePlans> rechargePlanRecord = rechargeServiceRepository.findById(rechargeId);
			if (!rechargePlanRecord.isEmpty() && rechargePlanRecord.get() != null) {

				RechargePlans updatedRechargePlanRecords = new RechargePlans();

				updatedRechargePlanRecords.setRechargeId(rechargeId);
				updatedRechargePlanRecords.setAmount(rechargePlans.getAmount() != null ? rechargePlans.getAmount()
						: rechargePlanRecord.get().getAmount());
				updatedRechargePlanRecords
						.setDescription(rechargePlans.getDescription() != null ? rechargePlans.getDescription()
								: rechargePlanRecord.get().getDescription());
				updatedRechargePlanRecords.setStatus(rechargePlans.getStatus() != null ? rechargePlans.getStatus()
						: rechargePlanRecord.get().getStatus());
				updatedRechargePlanRecords
						.setSubscriberCount(rechargePlans.getSubscriberCount() != 0 ? rechargePlans.getSubscriberCount()
								: rechargePlanRecord.get().getSubscriberCount());
				updatedRechargePlanRecords
						.setVendorName(rechargePlans.getVendorName() != null ? rechargePlans.getVendorName()
								: rechargePlanRecord.get().getVendorName());
				updatedRechargePlanRecords = rechargeServiceRepository.save(updatedRechargePlanRecords);
				return "RechargeId : " + rechargeId + " updated successfully!!";
			}
			return null;
		}
		return "UNAUTHORIZED";
	}

	@Override
	public String deleteRechargePlanDetailsByRechargeId(Long userId,Long rechargeId) {
		ResponseEntity<Boolean> isUserAdmin = userService.isUserAdmin(userId);
		if (isUserAdmin!=null && isUserAdmin.getBody().booleanValue()) {
		Optional<RechargePlans> rechargePlanRecord= rechargeServiceRepository.findById(rechargeId);
		if(!rechargePlanRecord.isEmpty() && rechargePlanRecord.get()!=null) {
			rechargeServiceRepository.deleteById(rechargeId);
			return "RechargeId : "+rechargeId +" record deleted successfully";
		}
		else if(rechargePlanRecord.get()==null) 
			return null;
		return  "RechargeId : "+rechargeId +" not found !!";
		}
		return "UNAUTHORIZED";
	}

	@Override
	public String getVendorNameByRechargeId(Long rechargeId) {
		return rechargeServiceRepository.getVendorNameByRechargeID(rechargeId);
	}
	
	@Override
	public List<RechargePlans> getAllRechargePlansById(List<Long> rechargeIds){
		
		List<RechargePlans> rechargePlansList = rechargeServiceRepository.getRechargePlansByRechargeIds(rechargeIds);
		
		return rechargePlansList;
	}
	
	@Override
	public String cancelRechargePlanByCustomer(Long rechargeId) {
		Optional<RechargePlans> rechargePlanRecord = rechargeServiceRepository.findById(rechargeId);
		if (!rechargePlanRecord.isEmpty()) {
			RechargePlans rechargePlan = rechargePlanRecord.get();
			if(rechargePlan.getSubscriberCount()==0) {
				return "SUBSCRIBER_0";
			}
			rechargePlan.setSubscriberCount(rechargePlan.getSubscriberCount() - 1);
			if (rechargePlan.getStatus().equals("INACTIVE"))
				rechargePlan.setStatus("ACTIVE");
			rechargePlan = rechargeServiceRepository.save(rechargePlan);
			if (rechargePlan != null)
				return "RechargePlan Cancelled successfuly";
		}
		return null;
	}
	
	@Override
	public String updateRechargePlanAfterCustomerActivity(Long rechargeId) {
		Optional<RechargePlans> rechargePlanRecord = rechargeServiceRepository.findById(rechargeId);
		if (!rechargePlanRecord.isEmpty()) {
			RechargePlans rechargePlan = rechargePlanRecord.get();
			if(rechargePlan.getSubscriberCount() != 5 )
			{	rechargePlan.setSubscriberCount(rechargePlan.getSubscriberCount() + 1);
				if(rechargePlan.getSubscriberCount()==5)
					rechargePlan.setStatus("INACTIVE");
			}
			else
				return "INACTIVE";
			rechargePlan = rechargeServiceRepository.save(rechargePlan);
			if (rechargePlan != null)
				return "RechargePlan Subscribed successfuly";
		}
		return null;
	}
	
	@Override
	public List<RechargePlans> getAllByActivePlans(){
		List<RechargePlans> rechargePlansList = findAllRechargePlansDetails(); //rechargeServiceRepository.findAll();
		if(rechargePlansList!=null) {
			List<RechargePlans> activeRechargePlanList=rechargePlansList.stream().filter(r->(r.getStatus()!=null && r.getStatus().equals("ACTIVE"))).collect(Collectors.toList());
			return activeRechargePlanList;
		}
		return null;
	}

	public List<RechargePlans> getAllPlansByVendorname(String vendorname) {
		List<RechargePlans> rechargePlansList = rechargeServiceRepository.findByVendorName(vendorname);
		return rechargePlansList;
		
		
	}
}