package com.zjgsu.lll.course2_new.controller;

import com.zjgsu.lll.course2_new.common.ApiResponse;
import com.zjgsu.lll.course2_new.model.Enrollment;
import com.zjgsu.lll.course2_new.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService service;
    public EnrollmentController(EnrollmentService service) { this.service = service; }

    @PostMapping
    public ApiResponse<Enrollment> enroll(@RequestBody Map<String, String> req) {
        return ApiResponse.success(service.enroll(req.get("courseId"), req.get("studentId")));
    }

    @GetMapping
    public ApiResponse<List<Enrollment>> all() { return ApiResponse.success(service.getAll()); }

    @GetMapping("/course/{courseId}")
    public ApiResponse<List<Enrollment>> byCourse(@PathVariable String courseId) {
        return ApiResponse.success(service.byCourse(courseId));
    }

    @GetMapping("/student/{studentId}")
    public ApiResponse<List<Enrollment>> byStudent(@PathVariable String studentId) {
        return ApiResponse.success(service.byStudent(studentId));
    }
}
