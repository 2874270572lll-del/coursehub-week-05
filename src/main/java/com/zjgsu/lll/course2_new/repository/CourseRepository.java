package com.zjgsu.lll.course2_new.repository;

import com.zjgsu.lll.course2_new.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    // 按课程代码查询
    Optional<Course> findByCode(String code);

    // 检查课程代码是否存在
    boolean existsByCode(String code);

    // 按讲师ID查询
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId")
    List<Course> findByInstructorId(@Param("instructorId") String instructorId);

    // 查询有剩余容量的课程
    @Query("SELECT c FROM Course c WHERE c.enrolled < c.capacity")
    List<Course> findAvailableCourses();

    // 标题关键字模糊查询
    List<Course> findByTitleContaining(String keyword);

    // 统计有剩余容量的课程数量
    @Query("SELECT COUNT(c) FROM Course c WHERE c.enrolled < c.capacity")
    long countAvailableCourses();
}