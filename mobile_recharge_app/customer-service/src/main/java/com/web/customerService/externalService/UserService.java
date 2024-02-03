package com.web.customerService.externalService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="USER-SERVICE")
public interface UserService {
	
	@GetMapping("/users/validateUser/{userId}")
	public ResponseEntity<Boolean> isUserAdmin(@PathVariable("userId") Long userId);
}
