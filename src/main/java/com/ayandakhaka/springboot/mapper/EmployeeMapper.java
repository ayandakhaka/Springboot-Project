package com.ayandakhaka.springboot.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.ayandakhaka.springboot.dto.EmployeeDto;
import com.ayandakhaka.springboot.model.Employee;

public class EmployeeMapper {
	
	public static EmployeeDto mapToEmployeeDto(Employee employee) {
		return new EmployeeDto(
				employee.getId(),
				employee.getFirstName(),
				employee.getLastName(),
				employee.getEmail()
		);
		
	}
	
	public static Employee mapToEmployee(EmployeeDto employeeDto) {
		return new Employee(
				employeeDto.getId(),
				employeeDto.getFirstName(),
				employeeDto.getLastName(),
				employeeDto.getEmail()
		);
	}

}
