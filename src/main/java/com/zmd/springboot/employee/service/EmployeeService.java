package com.zmd.springboot.employee.service;

import com.zmd.springboot.employee.dto.EmployeeRequest;
import com.zmd.springboot.employee.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAll();
    Employee getById(long id);
    Employee create(EmployeeRequest request);
    Employee update(long id, EmployeeRequest request);
    void delete(long id);
}