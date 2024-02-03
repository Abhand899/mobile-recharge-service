package com.web.customerService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.customerService.entity.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Query(nativeQuery = true,value="select * from customer r1 where r1.vendorname=:vendorName")
	public List<Customer> findByVendorName(@Param("vendorName") String vendorName);
	
}
