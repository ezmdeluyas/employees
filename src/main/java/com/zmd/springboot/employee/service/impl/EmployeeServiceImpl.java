package com.zmd.springboot.employee.service.impl;

import com.zmd.springboot.employee.dao.EmployeeDAO;
import com.zmd.springboot.employee.dto.EmployeeRequest;
import com.zmd.springboot.employee.entity.Employee;
import com.zmd.springboot.employee.exception.EmployeeNotFoundException;
import com.zmd.springboot.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDAO employeeDao;

    @Autowired
    public void setEmployeeDao(EmployeeDAO employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAll() {
        return employeeDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getById(long id) {
        Employee employee = employeeDao.findById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException(id);
        }
        return employee;
    }

    @Override
    public Employee create(EmployeeRequest request) {
        // Using id=0 is OK because IDENTITY will generate a real id
        Employee employee = new Employee(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()
        );
        return employeeDao.save(employee);
    }

    @Override
    public Employee update(long id, EmployeeRequest request) {
        Employee existing = getById(id); // throws if missing

        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEmail(request.getEmail());

        return employeeDao.save(existing);
    }

    @Override
    public void delete(long id) {
        // ensure consistent 404 behavior
        getById(id);
        employeeDao.deleteById(id);
    }
}
