package com.zmd.springboot.employees.dao;

import com.zmd.springboot.employees.entity.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();
}
