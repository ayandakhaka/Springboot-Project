package com.ayandakhaka.springboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmployeeDto {

	private Long id;
	
	@NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
	private String firstName;
    
	@NotBlank(message = "Last name is required")
	private String lastName;
	
	@NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
	private String email;
	public Long getId() {
		return id;
	}
	
	
	public EmployeeDto() {
	}
	
	public EmployeeDto(long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
