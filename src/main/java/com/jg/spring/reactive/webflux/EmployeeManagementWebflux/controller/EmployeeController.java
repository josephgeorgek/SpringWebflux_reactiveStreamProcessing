package com.jg.spring.reactive.webflux.EmployeeManagementWebflux.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.dto.EmployeeDTO;
import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.entity.Employee;
import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.service.impl.EmployeeServiceImpl;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	EmployeeServiceImpl employeeService;

	@PostMapping("")
	public Long saveEmployee(@RequestBody Employee employee) {

		return employeeService.createEmployee(employee);
	}

	@GetMapping("/all")
	public List<Employee> getAll() {

		return employeeService.getAllEmployees();
	}

	@GetMapping("")
	public Optional<Employee> get(@RequestParam(name = "id") Long id) {

		return employeeService.get(id);
	}
	
	@GetMapping("/reactive")
	private Flux<Employee> getAllEmployees() {
		
	     
	    
	    return Flux.fromIterable(employeeService.getAllEmployees());
	}

}
