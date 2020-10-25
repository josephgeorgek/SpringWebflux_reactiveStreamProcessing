package com.jg.spring.reactive.webflux.EmployeeManagementWebflux.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.entity.Employee;
import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.repository.EmployeeRepository;
import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository employeeRepostory;
	
	@Override
	public List<Employee> getAllEmployees() {
		// TODO Auto-generated method stub
		return employeeRepostory.findAll();
	}

	@Override
	public Long createEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return employeeRepostory.save(employee).getId();
	}

	@Override
	public Optional<Employee> get(Long id) {
		// TODO Auto-generated method stub
	
		return employeeRepostory.findById(String.valueOf(id));
	}
	
	

}
