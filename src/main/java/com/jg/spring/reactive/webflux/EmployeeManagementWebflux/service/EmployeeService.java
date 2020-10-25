package com.jg.spring.reactive.webflux.EmployeeManagementWebflux.service;

import java.util.List;
import java.util.Optional;

import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.entity.Employee;

public interface EmployeeService {
	
	List<Employee> getAllEmployees();
	Long createEmployee(Employee employee);
	 Optional<Employee> get(Long id);

}
