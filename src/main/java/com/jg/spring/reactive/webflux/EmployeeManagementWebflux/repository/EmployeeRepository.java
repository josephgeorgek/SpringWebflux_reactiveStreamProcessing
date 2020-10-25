package com.jg.spring.reactive.webflux.EmployeeManagementWebflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.entity.Employee;

@Repository
public interface EmployeeRepository  extends JpaRepository <Employee,String>{

}
