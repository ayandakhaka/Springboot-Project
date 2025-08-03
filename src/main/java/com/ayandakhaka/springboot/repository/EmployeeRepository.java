package com.ayandakhaka.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayandakhaka.springboot.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	
}
