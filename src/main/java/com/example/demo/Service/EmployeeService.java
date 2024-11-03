package com.example.demo.Service;

import com.example.demo.Model.Employee;
import com.example.demo.Repository.DepartmentRepository;
import com.example.demo.Repository.EmployeeRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ImageService imageService;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, ImageService imageService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.imageService = imageService;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long empno) {
        return employeeRepository.findById(empno).orElse(null);
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void updateEmployee(Employee employee) {

        employeeRepository.save(employee);
    }

    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }


    public Resource loadImageFromHDFS(String imageName) throws IOException {
        return imageService.loadImageFromHDFS(imageName);
    }
}
