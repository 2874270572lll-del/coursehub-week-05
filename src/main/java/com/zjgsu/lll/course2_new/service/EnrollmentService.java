package com.zjgsu.lll.course2_new.service;

import com.zjgsu.lll.course2_new.model.Course;
import com.zjgsu.lll.course2_new.model.Enrollment;
import com.zjgsu.lll.course2_new.model.EnrollmentStatus;
import com.zjgsu.lll.course2_new.model.Student;
import com.zjgsu.lll.course2_new.repository.CourseRepository;
import com.zjgsu.lll.course2_new.repository.EnrollmentRepository;
import com.zjgsu.lll.course2_new.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepo;
    private final CourseRepository courseRepo;
    private final StudentRepository studentRepo;

    public EnrollmentService(EnrollmentRepository e, CourseRepository c, StudentRepository s) {
        this.enrollmentRepo = e;
        this.courseRepo = c;
        this.studentRepo = s;
    }

    public Enrollment enroll(String courseId, String studentId) {
        // 验证课程存在
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        // 验证学生存在
        Student student = studentRepo.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with studentId: " + studentId));

        // 检查是否已选课（活跃状态）
        if (enrollmentRepo.existsActiveByCourseIdAndStudentId(courseId, student.getId())) {
            throw new RuntimeException("Duplicate enrollment: Student already enrolled in this course");
        }

        // 检查课程容量
        if (course.getEnrolled() >= course.getCapacity()) {
            throw new RuntimeException("Course full: No available seats");
        }

        // 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setId(UUID.randomUUID().toString());
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(student.getId());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepo.save(enrollment);

        // 更新课程已选人数
        course.setEnrolled(course.getEnrolled() + 1);
        courseRepo.save(course);

        return enrollment;
    }

    public void dropCourse(String courseId, String studentId) {
        // 查找学生
        Student student = studentRepo.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with studentId: " + studentId));

        // 查找选课记录
        Enrollment enrollment = enrollmentRepo.findByCourseIdAndStudentId(courseId, student.getId())
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // 检查状态
        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new RuntimeException("Cannot drop: Enrollment is not active");
        }

        // 更新选课状态为已退课
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepo.save(enrollment);

        // 更新课程已选人数
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setEnrolled(Math.max(0, course.getEnrolled() - 1));
        courseRepo.save(course);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getAll() {
        return enrollmentRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Enrollment> byCourse(String courseId) {
        return enrollmentRepo.findByCourseId(courseId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> byStudent(String studentId) {
        return enrollmentRepo.findByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getActiveByCourse(String courseId) {
        return enrollmentRepo.findByCourseIdAndStatus(courseId, EnrollmentStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getActiveByStudent(String studentId) {
        return enrollmentRepo.findByStudentIdAndStatus(studentId, EnrollmentStatus.ACTIVE);
    }
}