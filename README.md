课程管理系统文档说明
项目概述：
本系统是一个基于 Spring Boot 的课程管理系统，主要实现学生、课程及选课记录的管理功能，支持课程查询、学生信息维护、选课 / 退课等核心操作。系统采用分层架构设计，结合 JPA 实现数据持久化，支持 H2 内存数据库（开发环境）和 MySQL 数据库（生产环境）。

技术栈：
核心框架：Spring Boot 3.3.4
数据访问：Spring Data JPA
数据库：H2（开发环境）、MySQL（生产环境）
构建工具：Maven
Java 版本：JDK 23
API 响应格式：统一 JSON 格式

系统架构
系统采用经典的三层架构：
Controller 层：处理 HTTP 请求，返回统一响应
Service 层：实现业务逻辑，处理事务
Repository 层：数据访问层，与数据库交互

核心功能模块
1. 学生管理（Student）
功能描述：
学生信息的增删改查
按学号、专业、年级查询学生
学生信息唯一性校验（学号、邮箱）
核心类：
实体类：Student.java（存储学生基本信息：学号、姓名、专业、年级、邮箱等）
控制器：StudentController.java（提供/api/students接口）
服务类：StudentService.java（实现学生管理业务逻辑）
数据访问：StudentRepository.java（提供学生数据操作方法）
主要接口：
方法	路径	描述
GET	/api/students	获取所有学生
GET	/api/students/{id}	按 ID 获取学生
POST	/api/students	创建新学生
PUT	/api/students/{id}	更新学生信息
DELETE	/api/students/{id}	删除学生（需无活跃选课记录）

2. 课程管理（Course）
功能描述：
课程信息的增删改查
按课程代码、标题关键字、讲师 ID 查询课程
查询有剩余容量的课程
核心类
实体类：Course.java（存储课程信息：课程代码、标题、容量、已选人数等）
嵌入类：Instructor.java（讲师信息）、ScheduleSlot.java（课程时间安排）
控制器：CourseController.java（提供/api/courses接口）
服务类：CourseService.java（实现课程管理业务逻辑）
数据访问：CourseRepository.java（提供课程数据操作方法）
主要接口
方法	路径	描述
GET	/api/courses	获取所有课程
GET	/api/courses/{id}	按 ID 获取课程
POST	/api/courses	创建新课程
PUT	/api/courses/{id}	更新课程信息
DELETE	/api/courses/{id}	删除课程

3. 选课管理（Enrollment）
功能描述
学生选课（需检查课程容量和重复选课）
学生退课（更新课程容量）
查询选课记录（按课程、按学生、按状态）
核心类
实体类：Enrollment.java（存储选课记录：课程 ID、学生 ID、选课状态等）
枚举类：EnrollmentStatus.java（选课状态：ACTIVE - 正常、DROPPED - 已退课、COMPLETED - 已完成）
控制器：EnrollmentController.java（提供/api/enrollments接口）
服务类：EnrollmentService.java（实现选课管理业务逻辑）
数据访问：EnrollmentRepository.java（提供选课记录操作方法）
主要接口
方法	路径	描述
POST	/api/enrollments	学生选课
GET	/api/enrollments	获取所有选课记录
GET	/api/enrollments/course/{courseId}	按课程查询选课记录
GET	/api/enrollments/student/{studentId}	按学生查询选课记录

4. 系统监控
健康检查接口：/health/db（检查数据库连接状态及各表记录数）
实现类：HealthController.java
配置说明
系统支持多环境配置，通过spring.profiles.active指定环境：
4.1 开发环境（application-dev.yml）
数据库：H2 内存数据库
配置：开启 H2 控制台（访问路径/h2-console）
JPA：自动更新表结构（ddl-auto: update），显示 SQL 日志
4.2生产环境（application-prod.yml）
数据库：MySQL
配置：连接池参数优化
JPA：验证表结构（ddl-auto: validate），关闭 SQL 日志

统一响应格式
所有 API 接口返回统一格式的 JSON 响应（ApiResponse.java）：
json
{
  "code": 200,       // 状态码（200-成功，500-错误）
  "message": "Success",  // 提示信息
  "data": {}         // 业务数据（成功时返回）
}

异常处理
全局异常处理器：GlobalExceptionHandler.java
统一捕获RuntimeException，返回错误状态码和消息

启动说明
开发环境：直接运行Course2Application.java的main方法，系统默认使用 H2 数据库
生产环境：通过--spring.profiles.active=prod指定生产环境配置，需提前创建 MySQL 数据库course_db
