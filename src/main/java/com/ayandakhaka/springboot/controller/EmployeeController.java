package com.ayandakhaka.springboot.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ayandakhaka.springboot.dto.EmployeeDto;
import com.ayandakhaka.springboot.exception.ResourceNotFoundException;
import com.ayandakhaka.springboot.model.Employee;
import com.ayandakhaka.springboot.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private EmployeeService employeeService;

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public EmployeeController() {

	}

	// Build get all employees REST API
	@GetMapping()
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
		List<EmployeeDto> employees = employeeService.getAllEmployees();

		if(employees.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(employees);
		}

	}

	// Build get single employee REST API
	@GetMapping("{id}")
	public ResponseEntity<EmployeeDto> getSingleEmployeeById(@PathVariable("id") long employeeId) {

		EmployeeDto employeeDto = employeeService.getSingleEmployeeById(employeeId);
		return ResponseEntity.ok(employeeDto);
	}

	// Build update employee REST API
	@PutMapping("{id}")
	public ResponseEntity<EmployeeDto> updateEmployeeDetails(@RequestBody @Valid EmployeeDto employee,
			@PathVariable("id") long employeeId) {

		try {
			EmployeeDto updateEmployee = employeeService.updateEmployee(employee, employeeId);
			//URI location = URI.create("/api/employees/" + updateEmployee.getId());
			return ResponseEntity.ok(updateEmployee);

		} catch(ResourceNotFoundException ex) {
			return ResponseEntity.notFound().build();
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}


	}
	
	// Build create employee REST API
		@PostMapping()
		public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws MethodArgumentNotValidException {


			try {
				EmployeeDto createdEmployee = employeeService.createEmployee(employeeDto);
				URI location = URI.create("/api/employees/" + createdEmployee.getId());
				return ResponseEntity.created(location).body(createdEmployee);
				
			} catch(ResponseStatusException ex) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		}

	// Build delete employee REST API
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {
		try {
			employeeService.deleteEmployee(id);
			return ResponseEntity.noContent().build(); // 204
		} catch (ResponseStatusException e) {
			return ResponseEntity.notFound().build(); // 404
		}
		
		
	}

}
