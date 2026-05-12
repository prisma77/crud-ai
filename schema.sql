-- 수강신청 CRUD 프로젝트 데이터베이스 스키마
-- 데이터베이스: orange

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS orange DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE orange;

-- 학생 테이블
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '학생 ID (기본키)',
    student_no VARCHAR(20) NOT NULL UNIQUE COMMENT '학번',
    name VARCHAR(50) NOT NULL COMMENT '학생 이름',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '이메일',
    dept VARCHAR(50) COMMENT '학과',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'
) COMMENT '학생 정보 테이블';

-- 강좌 테이블
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '강좌 ID (기본키)',
    code VARCHAR(20) NOT NULL UNIQUE COMMENT '강좌 코드',
    title VARCHAR(100) NOT NULL COMMENT '강좌명',
    professor VARCHAR(50) COMMENT '담당 교수',
    credit INT COMMENT '학점',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'
) COMMENT '강좌 정보 테이블';

-- 수강신청 테이블
CREATE TABLE enrollments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '수강신청 ID (기본키)',
    student_id BIGINT NOT NULL COMMENT '학생 ID (외래키)',
    course_id BIGINT NOT NULL COMMENT '강좌 ID (외래키)',
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '수강신청일시',
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE (student_id, course_id) COMMENT '한 학생은 같은 강좌를 중복 수강할 수 없음'
) COMMENT '수강신청 정보 테이블';

-- 인덱스 생성
CREATE INDEX idx_students_student_no ON students(student_no);
CREATE INDEX idx_students_name ON students(name);
CREATE INDEX idx_courses_code ON courses(code);
CREATE INDEX idx_courses_title ON courses(title);
CREATE INDEX idx_enrollments_student_id ON enrollments(student_id);
CREATE INDEX idx_enrollments_course_id ON enrollments(course_id);

