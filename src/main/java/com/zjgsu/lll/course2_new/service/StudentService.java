package com.zjgsu.lll.course2_new.service;

import com.zjgsu.lll.course2_new.model.Enrollment;
import com.zjgsu.lll.course2_new.model.EnrollmentStatus;
import com.zjgsu.lll.course2_new.model.Student;
import com.zjgsu.lll.course2_new.repository.EnrollmentRepository;
import com.zjgsu.lll.course2_new.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentService(StudentRepository studentRepository,
                          EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student getById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Student getByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with studentId: " + studentId));
    }

    public Student createStudent(Student student) {
        // 检查学号是否已存在
        if (student.getStudentId() != null &&
                studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("Duplicate studentId: " + student.getStudentId());
        }

        // 检查邮箱是否已存在
        if (student.getEmail() != null &&
                studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists: " + student.getEmail());
        }

        // 验证邮箱格式
        if (student.getEmail() == null || !student.getEmail().contains("@")) {
            throw new RuntimeException("Invalid email format");
        }

        // 设置ID
        if (student.getId() == null || student.getId().isEmpty()) {
            student.setId(UUID.randomUUID().toString());
        }

        return studentRepository.save(student);
    }

    public Student updateStudent(String id, Student updated) {
        Student existing = getById(id);

        // 保留原有的ID和创建时间
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());

        // 如果学号改变,检查新学号是否已存在
        if (!existing.getStudentId().equals(updated.getStudentId()) &&
                studentRepository.existsByStudentId(updated.getStudentId())) {
            throw new RuntimeException("Duplicate studentId: " + updated.getStudentId());
        }

        // 如果邮箱改变,检查新邮箱是否已存在
        if (!existing.getEmail().equals(updated.getEmail()) &&
                studentRepository.existsByEmail(updated.getEmail())) {
            throw new RuntimeException("Email already exists: " + updated.getEmail());
        }

        // 验证邮箱格式
        if (!updated.getEmail().contains("@")) {
            throw new RuntimeException("Invalid email format");
        }

        return studentRepository.save(updated);
    }

    public void deleteStudent(String id) {
        // 检查学生是否存在
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }

        // 检查是否有活跃的选课记录
        List<Enrollment> activeEnrollments = enrollmentRepository
                .findByStudentIdAndStatus(id, EnrollmentStatus.ACTIVE);

        if (!activeEnrollments.isEmpty()) {
            throw new RuntimeException("无法删除:该学生存在选课记录");
        }

        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Student> getByMajor(String major) {
        return studentRepository.findByMajor(major);
    }

    @Transactional(readOnly = true)
    public List<Student> getByGrade(int grade) {
        return studentRepository.findByGrade(grade);
    }
}