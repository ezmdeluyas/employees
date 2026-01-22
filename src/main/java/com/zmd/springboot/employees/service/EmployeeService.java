package com.zmd.springboot.employees.service;

import com.zmd.springboot.employees.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();
}
