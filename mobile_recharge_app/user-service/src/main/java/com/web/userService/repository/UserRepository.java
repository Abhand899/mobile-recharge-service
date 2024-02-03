package com.web.userService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.userService.entity.Customer;
import com.web.userService.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	User findByUsername(String username);
	
	@Query(nativeQuery = true,value="select * from user u1 where u1.username=:username and u1.password=:password")
	public User findByUsernameAndPassword(@Param("username") String username,@Param("password") String password);
}
