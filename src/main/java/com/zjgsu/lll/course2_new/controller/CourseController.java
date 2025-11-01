package com.zjgsu.lll.course2_new.controller;

import com.zjgsu.lll.course2_new.common.ApiResponse;
import com.zjgsu.lll.course2_new.model.Course;
import com.zjgsu.lll.course2_new.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<Course>> all() {
        return ApiResponse.success(service.getAllCourses());
    }

    @GetMapping("/{id}")
    public ApiResponse<Course> byId(@PathVariable String id) {
        return ApiResponse.success(service.getCourseById(id));
    }

    @PostMapping
    public ApiResponse<Course> create(@RequestBody Course c) {
        return ApiResponse.success(service.createCourse(c));
    }

    @PutMapping("/{id}")
    public ApiResponse<Course> update(@PathVariable String id, @RequestBody Course c) {
        return ApiResponse.success(service.updateCourse(id, c));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        service.deleteCourse(id);
        return ApiResponse.success(null);
    }
}
