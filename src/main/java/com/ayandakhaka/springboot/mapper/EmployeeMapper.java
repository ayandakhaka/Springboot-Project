package com.ayandakhaka.springboot.mapper;

import com.ayandakhaka.springboot.dto.EmployeeDto;
import com.ayandakhaka.springboot.model.Employee;

public class EmployeeMapper {

    // Map DTO to Entity
    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();

        // Safe mapping of ID
        employee.setId(employeeDto.getId() != null ? employeeDto.getId() : null);
//        if(employee.getId() != null) {
//        	
//        }

        // Map other fields
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        
        return employee;
    }

    // Map Entity to DTO
    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        return dto;
    }
}