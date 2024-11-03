package com.example.demo.Controller;

import com.example.demo.Model.Department;
import com.example.demo.Model.Employee;
import com.example.demo.Service.DepartmentService; // Import the DepartmentService
import com.example.demo.Service.EmployeeService;
import com.example.demo.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;
    private final ImageService imageService;
    private final DepartmentService departmentService; // Declare the DepartmentService

    @Autowired
    public EmployeeController(EmployeeService employeeService, ImageService imageService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.imageService = imageService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employeeDetails", employees);
        return "employees";
    }

    @GetMapping("/create")
    public String showCreateEmployeeForm(Model model) {
        List<Department> departments = departmentService.getAllDepartments(); // Use the departmentService
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departments); // Add departments to the model
        return "addEmployee";
    }

    @GetMapping("/edit/{empno}")
    public String showEditEmployeeForm(@PathVariable Long empno, Model model) {
        Employee employee = employeeService.getEmployeeById(empno);
        if (employee == null) {
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee);
        List<Department> departments = departmentService.getAllDepartments(); // Fetch departments for edit form
        model.addAttribute("departments", departments);
        return "editEmployee";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee,
                              @RequestParam("img") MultipartFile imgFile) {
        try {
            if (imgFile != null && !imgFile.isEmpty()) {
                String imageName = imgFile.getOriginalFilename();
                imageService.uploadImageToHDFS(imageName, imgFile);
                employee.setImgName(imageName);
            }
            // Check if the employee has a department set
            if (employee.getDepartment() != null && employee.getDepartment().getDeptno() != null) {
                Department department = departmentService.getDepartmentById(employee.getDepartment().getDeptno());
                employee.setDepartment(department);
            } else {
                logger.error("Department is not set for employee");
                // Handle this scenario as per your application logic, e.g. redirect or return an error
                return "redirect:/employees"; // or an error page
            }
            employeeService.saveEmployee(employee);
        } catch (IOException e) {
            logger.error("Error uploading image", e);
            return "addEmployee"; // Consider returning an error message or view
        }
        return "redirect:/employees";
    }


    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee,
                                 @RequestParam("img") MultipartFile imgFile) {
        try {
            if (imgFile != null && !imgFile.isEmpty()) {
                String imageName = imgFile.getOriginalFilename();
                imageService.uploadImageToHDFS(imageName, imgFile);
                employee.setImgName(imageName);
            }
            // Ensure the employee's department is set
            if (employee.getDepartment() != null && employee.getDepartment().getDeptno() != null) {
                Department department = departmentService.getDepartmentById(employee.getDepartment().getDeptno());
                employee.setDepartment(department);
            } else {
                logger.error("Department is not set for employee");
                return "redirect:/employees"; // or handle as needed
            }
            employeeService.updateEmployee(employee);
        } catch (IOException e) {
            logger.error("Error uploading image", e);
            return "editEmployee"; // Consider returning an error message or view
        }
        return "redirect:/employees";
    }


    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployeeById(id);
        return "redirect:/employees";
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Resource resource = imageService.loadImageFromHDFS(imageName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName + "\"")
                    .body(resource);
        } catch (IOException e) {
            logger.error("Error loading image", e);
            return ResponseEntity.notFound().build();
        }
    }
}
