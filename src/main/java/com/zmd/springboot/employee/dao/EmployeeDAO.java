package com.zmd.springboot.employee.dao;

import com.zmd.springboot.employee.entity.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();

    Employee findById(long id);

    Employee save(Employee employee);

    void deleteById(long id);
}
