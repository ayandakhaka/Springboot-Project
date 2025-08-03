package com.ayandakhaka.springboot.service;

import java.util.List;

import com.ayandakhaka.springboot.model.Employee;

public interface EmployeeService {

	Employee saveEmployee(Employee employee); 
	List<Employee> getAllEmployees();
	Employee getSingleEmployee(long id);
	Employee updateEmployee(Employee employee, long id);
	void deleteEmployee(long id);
}
