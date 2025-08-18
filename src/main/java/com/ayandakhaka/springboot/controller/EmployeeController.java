package com.ayandakhaka.springboot.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ayandakhaka.springboot.exception.EmployeeNotFoundException;
import com.ayandakhaka.springboot.exception.ErrorResponse;
import com.ayandakhaka.springboot.exception.ResourceNotFoundException;
import com.ayandakhaka.springboot.model.Employee;
import com.ayandakhaka.springboot.repository.EmployeeRepository;
import com.ayandakhaka.springboot.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	//private static final String BASE_URL = "/api/employees/";
	@Autowired
	private EmployeeService employeeService;

	//	public EmployeeController(EmployeeService employeeService) {
	//		this.employeeService = employeeService;
	//	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	//	public EmployeeController() {
	//
	//	}

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

	// Build create employee REST API
	@PostMapping
	public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {

		// Check if ID is provided while creating a new employee
		boolean idProvided = employeeDto.getId() != null;

		// Check if any required field is missing or blank
		boolean missingFields = employeeDto.getFirstName() == null || employeeDto.getFirstName().isBlank()
				|| employeeDto.getLastName() == null || employeeDto.getLastName().isBlank()
				|| employeeDto.getEmail() == null || employeeDto.getEmail().isBlank();

		// If ID is provided OR required fields are missing, return 400
		if (idProvided || missingFields) {
			String message;
			if (idProvided && missingFields) {
				message = "ID should not be provided and required fields must not be empty";
			} else if (idProvided) {
				message = "ID should not be provided when creating a new employee";
			} else {
				message = "Required fields must not be empty";
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(400, message, LocalDateTime.now()));
		}

		// If validation passes, save employee
		EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
		// Build Location URI
		URI location = URI.create("/api/employees/" + savedEmployee.getId());
		return ResponseEntity.created(location).body(savedEmployee);
	}

	// Build update employee REST API
	@PutMapping("{id}")
	public ResponseEntity<?> updateEmployeeDetails(@PathVariable("id") long employeeId ,@RequestBody @Valid EmployeeDto employeeDto) {

		if (employeeDto.getId() != null && !employeeDto.getId().equals(employeeId)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(
							404,
							"Employee ID in request body must match path variable",
							LocalDateTime.now()
							));
		}

		// Required fields cannot be empty
		if (employeeDto.getFirstName() == null || employeeDto.getFirstName().isBlank() ||
				employeeDto.getLastName() == null || employeeDto.getLastName().isBlank() ||
				employeeDto.getEmail() == null || employeeDto.getEmail().isBlank()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(
							400,
							"First name, last name, and email cannot be empty",
							LocalDateTime.now()
							));
		}

		// Check if employee exist
		EmployeeDto employeeExist = employeeService.getSingleEmployeeById(employeeId);
		if(employeeExist == null) {
			throw new ResourceNotFoundException("Employee with ID " + employeeId + " not found", "Employee ID", employeeExist);
		}

		// Update employee
		EmployeeDto updateEmployee = employeeService.updateEmployee(employeeId ,employeeDto);
		return ResponseEntity.ok(updateEmployee);

	}
	// Build delete employee REST API
	@DeleteMapping("{id}")
	public ResponseEntity<Object> deleteEmployee(@PathVariable("id") long id) {
		employeeService.deleteEmployee(id);
	    return ResponseEntity.noContent().build(); // 204 No Content when successful


	}

}
