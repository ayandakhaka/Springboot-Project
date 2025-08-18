package com.ayandakhaka.springboot.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ayandakhaka.springboot.dto.EmployeeDto;
import com.ayandakhaka.springboot.exception.EmployeeNotFoundException;
import com.ayandakhaka.springboot.exception.ResourceNotFoundException;
import com.ayandakhaka.springboot.mapper.EmployeeMapper;
import com.ayandakhaka.springboot.model.Employee;
import com.ayandakhaka.springboot.repository.EmployeeRepository;
import com.ayandakhaka.springboot.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;

	
	
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}

	@Override
	public EmployeeDto createEmployee(EmployeeDto employeeDto) {
		
		Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
		Employee saveEmployee = employeeRepository.save(employee);
		return EmployeeMapper.mapToEmployeeDto(saveEmployee);
	
	}
	
	@Override
	public EmployeeDto updateEmployee(long id ,EmployeeDto employeeDto) {
		
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> 
		new ResourceNotFoundException("Employee", "Id", id));
		
		employee.setFirstName(employeeDto.getFirstName());
		employee.setLastName(employeeDto.getLastName());
		employee.setEmail(employeeDto.getEmail());
		
		Employee updatedEmployeeObj = employeeRepository.save(employee);
		return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
	}
	
	@Override
	public void deleteEmployee(long id) {
//	    Optional<Employee> employee = employeeRepository.findById(id);
//	    if (employee.isEmpty()) {
//	        throw new EmployeeNotFoundException("Employee with id " + id + " not found");
//	    }
//	    employeeRepository.deleteById(id);
		Employee employee = employeeRepository.findById(id)
			        .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
	
	    employeeRepository.delete(employee);
	}
	
	@Override
	public EmployeeDto getSingleEmployeeById(long employeeId) {
	 // Using lamda expression
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> 
		new ResourceNotFoundException("Employee", "Id", employeeId));
		
		return EmployeeMapper.mapToEmployeeDto(employee);
				
	}
	
	@Override
	public List<EmployeeDto> getAllEmployees() {
		
		List<Employee> employees = employeeRepository.findAll();
		return employees.stream().map((employee) -> 
		EmployeeMapper.mapToEmployeeDto(employee)).collect(Collectors.toList());
	}
}
