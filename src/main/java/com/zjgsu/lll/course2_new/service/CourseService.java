package com.zjgsu.lll.course2_new.service;

import com.zjgsu.lll.course2_new.model.Course;
import com.zjgsu.lll.course2_new.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Course getCourseByCode(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Course not found with code: " + code));
    }

    public Course createCourse(Course course) {
        // 检查课程代码是否已存在
        if (course.getCode() != null && courseRepository.existsByCode(course.getCode())) {
            throw new RuntimeException("Course code already exists: " + course.getCode());
        }

        // 设置ID
        if (course.getId() == null || course.getId().isEmpty()) {
            course.setId(UUID.randomUUID().toString());
        }

        // 验证容量
        if (course.getCapacity() <= 0) {
            throw new RuntimeException("Course capacity must be greater than 0");
        }

        return courseRepository.save(course);
    }

    public Course updateCourse(String id, Course updated) {
        Course existing = getCourseById(id);

        // 保留原有的ID和创建时间
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());

        // 如果课程代码改变,检查新代码是否已存在
        if (!existing.getCode().equals(updated.getCode()) &&
                courseRepository.existsByCode(updated.getCode())) {
            throw new RuntimeException("Course code already exists: " + updated.getCode());
        }

        // 验证容量不能小于已选人数
        if (updated.getCapacity() < existing.getEnrolled()) {
            throw new RuntimeException("Capacity cannot be less than enrolled count");
        }

        return courseRepository.save(updated);
    }

    public void deleteCourse(String id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses();
    }

    @Transactional(readOnly = true)
    public List<Course> searchByTitle(String keyword) {
        return courseRepository.findByTitleContaining(keyword);
    }
}