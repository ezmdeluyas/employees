package com.zmd.springboot.employees.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(long id) {
        super("Employee with id " + id + " not found");
    }
}
