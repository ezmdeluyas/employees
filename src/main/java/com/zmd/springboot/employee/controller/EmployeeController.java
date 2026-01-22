package com.zmd.springboot.employee.controller;

import com.zmd.springboot.employee.dto.EmployeeRequest;
import com.zmd.springboot.employee.entity.Employee;
import com.zmd.springboot.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Rest API Endpoints", description = "Operations related to employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees")
    @GetMapping
    public ResponseEntity<List<Employee>> findAll() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @Operation(summary = "Get a single employee", description = "Get a single employee from database")
    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable @Min(1) long employeeId) {
        return ResponseEntity.ok(employeeService.getById(employeeId));
    }

    @Operation(summary = "Create a new employee", description = "Add a new employee to database")
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeRequest request) {
        Employee created = employeeService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Update an employee", description = "Update the details of a current employee")
    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable @Min(1) long employeeId,
            @Valid @RequestBody EmployeeRequest request
    ) {
        return ResponseEntity.ok(employeeService.update(employeeId, request));
    }

    @Operation(summary = "Delete an employee", description = "Remove an employee from the database")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable @Min(1) long employeeId) {
        employeeService.delete(employeeId);
        return ResponseEntity.noContent().build();
    }

}
