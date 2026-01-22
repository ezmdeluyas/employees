package com.zmd.springboot.employee.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(long id) {
        super("Employee with id " + id + " not found");
    }
}
