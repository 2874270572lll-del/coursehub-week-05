package com.zjgsu.lll.course2_new.repository;

import com.zjgsu.lll.course2_new.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    // 按学号查询
    Optional<Student> findByStudentId(String studentId);

    // 按邮箱查询
    Optional<Student> findByEmail(String email);

    // 检查学号是否存在
    boolean existsByStudentId(String studentId);

    // 检查邮箱是否存在
    boolean existsByEmail(String email);

    // 按专业查询
    List<Student> findByMajor(String major);

    // 按年级查询
    List<Student> findByGrade(int grade);

    // 按专业和年级查询
    List<Student> findByMajorAndGrade(String major, int grade);
}