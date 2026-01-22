package com.zmd.springboot.employees.service;

import com.zmd.springboot.employees.dto.EmployeeRequest;
import com.zmd.springboot.employees.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();

    Employee findById(long id);

    Employee save(EmployeeRequest employeeRequest);

    Employee update(long id, EmployeeRequest employeeRequest);

    Employee convertToEmployee(long id, EmployeeRequest employeeRequest);

    void deleteById(long id);
}
