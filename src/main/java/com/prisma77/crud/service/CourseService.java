package com.prisma77.crud.service;

import com.prisma77.crud.config.DatabaseConfig;
import com.prisma77.crud.domain.Course;
import com.prisma77.crud.repository.CourseRepository;
import com.prisma77.crud.util.PageInfo;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private static final int PAGE_SIZE = 10;

    public PageInfo<Course> getCoursesWithPaging(String keyword, int page) {
        if (keyword == null) keyword = "";
        if (page < 1) page = 1;

        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);

            long totalItems = repository.countByKeyword(keyword);
            // 전체 페이지 수 계산
            int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
            int offset = (page - 1) * PAGE_SIZE;

            List<Course> list = repository.findByKeywordWithPaging(keyword, PAGE_SIZE, offset);

            return new PageInfo<Course>(list, page, totalPages, totalItems, PAGE_SIZE);
        } catch (Exception e) {
            logger.error("강좌 목록 조회 실패", e);
            throw new RuntimeException("강좌 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    public List<Course> getAvailableCourses(Long studentId) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);
            return repository.findAvailableCoursesByStudentId(studentId);
        }
    }

    public Course getCourseById(Long id) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);
            return repository.findById(id);
        }
    }

    public List<Course> getAllCourses() {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);
            return repository.findAll();
        }
    }

    public void createCourse(Course course) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);
            if (repository.findByCode(course.getCode()) != null) {
                throw new IllegalArgumentException("이미 존재하는 강의코드입니다.");
            }
            repository.insert(course);
            session.commit();
        }
    }

    public void updateCourse(Course course) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);
            repository.update(course);
            session.commit();
        }
    }

    public void deleteCourse(Long id) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);
            repository.deleteById(id);
            session.commit();
        }
    }
}