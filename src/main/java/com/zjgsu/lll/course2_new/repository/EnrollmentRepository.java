package com.zjgsu.lll.course2_new.repository;

import com.zjgsu.lll.course2_new.model.Enrollment;
import com.zjgsu.lll.course2_new.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    // 按课程ID查询
    List<Enrollment> findByCourseId(String courseId);

    // 按学生ID查询
    List<Enrollment> findByStudentId(String studentId);

    // 按课程和学生查询
    Optional<Enrollment> findByCourseIdAndStudentId(String courseId, String studentId);

    // 按状态查询
    List<Enrollment> findByStatus(EnrollmentStatus status);

    // 按课程和状态查询
    List<Enrollment> findByCourseIdAndStatus(String courseId, EnrollmentStatus status);

    // 按学生和状态查询
    List<Enrollment> findByStudentIdAndStatus(String studentId, EnrollmentStatus status);

    // 统计课程活跃人数
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.courseId = :courseId AND e.status = 'ACTIVE'")
    long countActiveByCourseId(@Param("courseId") String courseId);

    // 判断学生是否已选课（活跃状态）
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Enrollment e " +
            "WHERE e.courseId = :courseId AND e.studentId = :studentId AND e.status = 'ACTIVE'")
    boolean existsActiveByCourseIdAndStudentId(@Param("courseId") String courseId,
                                               @Param("studentId") String studentId);
}