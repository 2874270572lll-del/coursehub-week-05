package com.zjgsu.lll.course2_new.controller;

import com.zjgsu.lll.course2_new.common.ApiResponse;
import com.zjgsu.lll.course2_new.model.Student;
import com.zjgsu.lll.course2_new.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service) { this.service = service; }

    @GetMapping
    public ApiResponse<List<Student>> all() { return ApiResponse.success(service.getAllStudents()); }

    @GetMapping("/{id}")
    public ApiResponse<Student> byId(@PathVariable String id) {
        return ApiResponse.success(service.getById(id));
    }

    @PostMapping
    public ApiResponse<Student> create(@RequestBody Student s) {
        return ApiResponse.success(service.createStudent(s));
    }

    @PutMapping("/{id}")
    public ApiResponse<Student> update(@PathVariable String id, @RequestBody Student s) {
        return ApiResponse.success(service.updateStudent(id, s));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        service.deleteStudent(id);
        return ApiResponse.success(null);
    }
}
