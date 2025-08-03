package com.ayandakhaka.springboot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayandakhaka.springboot.model.Employee;
import com.ayandakhaka.springboot.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
 
	private EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}
	
	// Build create employee REST API
	@PostMapping
	public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
		
		return new ResponseEntity<Employee>(employeeService.saveEmployee(employee),
				HttpStatus.CREATED);
	}
	
	// Build get all employees REST API
	@GetMapping
	public List<Employee> getAllEmployees() {
		
		return employeeService.getAllEmployees();
	}
	
	// Build get single employee REST API
	@GetMapping("{id}")
	public ResponseEntity<Employee> getSingleEmployeeById(@PathVariable("id") long employeeId) {
		return new ResponseEntity<Employee>(employeeService.getSingleEmployee(employeeId)
				, HttpStatus.OK);
	}
	
	// Build update employee REST API
	@PutMapping("{id}")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee, @PathVariable("id") long employeeId) {
		return new ResponseEntity<Employee>(employeeService.updateEmployee(employee, employeeId),
				HttpStatus.OK);
	}
	
	// Build delete employee REST API
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {
		employeeService.deleteEmployee(id);
		return new ResponseEntity<String>("Employee deleted successfully.!", HttpStatus.OK);
	}
	
}
