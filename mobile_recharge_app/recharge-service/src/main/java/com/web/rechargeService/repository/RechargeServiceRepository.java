package com.web.rechargeService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.rechargeService.entity.RechargePlans;

@Repository
public interface RechargeServiceRepository extends JpaRepository<RechargePlans, Long> {
	
	@Query(value="select vendorname from recharge_plans where rechargeid=:rechargeId",nativeQuery = true)
	public String getVendorNameByRechargeID(@Param("rechargeId")Long rechargeId);
	
	@Query(value="select * from recharge_plans where vendorname=:vendorname",nativeQuery = true)
	public List<RechargePlans> findByVendorName(@Param("vendorname")String vendorname);
	
	@Query(nativeQuery = true,value="select * from microservices.recharge_plans r1 where r1.rechargeid in :rechargeIds")
	public List<RechargePlans> getRechargePlansByRechargeIds(@Param("rechargeIds") List<Long> rechargeIds);
}