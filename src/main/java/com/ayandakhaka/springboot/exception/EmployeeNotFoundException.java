package com.ayandakhaka.springboot.exception;

public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(String message) {
		super(message);
	}
}
