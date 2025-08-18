package com.ayandakhaka.springboot.service;

import java.util.List;

import com.ayandakhaka.springboot.dto.EmployeeDto;

public interface EmployeeService {

	EmployeeDto createEmployee(EmployeeDto employeeDto); 
	List<EmployeeDto> getAllEmployees();
	EmployeeDto getSingleEmployeeById(long employeeId);
	EmployeeDto updateEmployee(long id ,EmployeeDto employeeDto);
	void deleteEmployee(long id);
}
