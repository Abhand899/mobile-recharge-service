package com.web.userService.exception;



public class ResourceNotFoundException extends Exception{
	

	public ResourceNotFoundException() {
		super("Resource Not Found on the server!!");
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
}