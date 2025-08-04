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

import com.ayandakhaka.springboot.dto.EmployeeDto;
import com.ayandakhaka.springboot.model.Employee;
import com.ayandakhaka.springboot.service.EmployeeService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
 
	private EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	// Build create employee REST API
	@PostMapping()
	public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
		
		EmployeeDto createdEmployee = employeeService.createEmployee(employeeDto);
		return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
	}
	
	// Build get all employees REST API
	@GetMapping()
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
		List<EmployeeDto> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(employees);
	}
	
	// Build get single employee REST API
	@GetMapping("{id}")
	public ResponseEntity<EmployeeDto> getSingleEmployeeById(@PathVariable("id") long employeeId) {
		
		EmployeeDto employeeDto = employeeService.getSingleEmployeeById(employeeId);
		return ResponseEntity.ok(employeeDto);
	}
	
	// Build update employee REST API
	@PutMapping("{id}")
	public ResponseEntity<EmployeeDto> updateEmployeeDetails(@RequestBody EmployeeDto employee, @PathVariable("id") long employeeId) {
		return new ResponseEntity<EmployeeDto>(employeeService.updateEmployee(employee, employeeId),
				HttpStatus.OK);
	}
	
	// Build delete employee REST API
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {
		employeeService.deleteEmployee(id);
		return new ResponseEntity<String>("Employee deleted successfully.!", HttpStatus.OK);
	}
	
}
