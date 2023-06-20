package ru.skypro.lessons.springboot.weblibrary.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.weblibrary.service.EmployeeService;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final EmployeeService employeeService;

    public ReportController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping()
    public int creatReport() {
        return employeeService.createReport();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadReport(@PathVariable int id) {
        Resource resource = employeeService.downloadReport(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Отчет.json\"")
                .body(resource);
    }
}
