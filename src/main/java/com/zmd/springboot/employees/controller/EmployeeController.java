package com.zmd.springboot.employees.controller;

import com.zmd.springboot.employees.dao.EmployeeDAO;
import com.zmd.springboot.employees.entity.Employee;
import com.zmd.springboot.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

   @Autowired
   public void setEmployeeService(EmployeeService employeeService) {
       this.employeeService = employeeService;
   }

    @GetMapping
    public List<Employee> findAll() {
        return employeeService.findAll();
    }
}
