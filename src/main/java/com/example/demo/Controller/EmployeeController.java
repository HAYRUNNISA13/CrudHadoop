package com.example.demo.Controller;

import com.example.demo.Model.Employee;
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

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employeeDetails", employees);
        return "employees";
    }

    @GetMapping("/create")
    public String showCreateEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "addEmployee";
    }

    @GetMapping("/edit/{empno}")
    public String showEditEmployeeForm(@PathVariable Long empno, Model model) {
        Employee employee = employeeService.getEmployeeById(empno);
        if (employee == null) {
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee);
        return "editEmployee";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee,
                              @RequestParam("img") MultipartFile imgFile,
                              Model model) {
        try {
            if (imgFile != null && !imgFile.isEmpty()) {
                String imageName = imgFile.getOriginalFilename();
                imageService.uploadImageToHDFS(imageName, imgFile);


                employee.setImgName(imageName);
            }
            employeeService.saveEmployee(employee);
        } catch (IOException e) {
            logger.error("Error uploading image", e);
            return "addEmployee";
        }

        return "redirect:/employees";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee) {
        employeeService.updateEmployee(employee);
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
