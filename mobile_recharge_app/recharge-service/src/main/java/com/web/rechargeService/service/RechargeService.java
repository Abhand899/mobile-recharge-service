package com.web.rechargeService.service;

import java.util.List;
import java.util.Optional;

import com.web.rechargeService.entity.RechargePlans;
import com.web.rechargeService.exceptions.ResourceNotFoundException;

public interface RechargeService {
	
	public RechargePlans findById(Long rechargeId) throws ResourceNotFoundException;
	
	public List<RechargePlans> findAllRechargePlansDetails();
	
	public String addRechargePlanDetails(Long userId,RechargePlans rechargePlans);
	
	public String updateRechargePlansDetails(Long userId,RechargePlans rechargePlans,Long rechargeId);
	
	public String deleteRechargePlanDetailsByRechargeId(Long userId,Long rechargeId);
	
	public String getVendorNameByRechargeId(Long rechargeId);

	public List<RechargePlans> getAllRechargePlansById(List<Long> rechargeIds);

	public List<RechargePlans> getAllByActivePlans();

	public String updateRechargePlanAfterCustomerActivity(Long rechargeId);

	public String cancelRechargePlanByCustomer(Long rechargeId);
}
