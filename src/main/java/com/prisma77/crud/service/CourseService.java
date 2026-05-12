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
            int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
            int offset = (page - 1) * PAGE_SIZE;

            List<Course> courses = repository.findByKeywordWithPaging(keyword, PAGE_SIZE, offset);

            return new PageInfo<>(courses, page, totalPages, totalItems, PAGE_SIZE);
        } catch (Exception e) {
            logger.error("강좌 목록 조회 실패", e);
            throw new RuntimeException("강좌 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    public Course getCourseById(Long id) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);
            return repository.findById(id);
        } catch (Exception e) {
            logger.error("강좌 조회 실패: id={}", id, e);
            throw new RuntimeException("강좌 정보 조회 중 오류가 발생했습니다.", e);
        }
    }

    public List<Course> getAllCourses() {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);
            return repository.findAll();
        } catch (Exception e) {
            logger.error("전체 강좌 조회 실패", e);
            throw new RuntimeException("강좌 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    public void createCourse(Course course) {
        validateCourse(course);

        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);

            // 강의코드 중복 체크
            if (repository.findByCode(course.getCode()) != null) {
                throw new IllegalArgumentException("이미 존재하는 강의코드입니다.");
            }

            repository.insert(course);
            session.commit();
            logger.info("강좌 등록 완료: {}", course.getCode());
        } catch (Exception e) {
            logger.error("강좌 등록 실패", e);
            throw new RuntimeException("강좌 등록 중 오류가 발생했습니다.", e);
        }
    }

    public void updateCourse(Course course) {
        validateCourse(course);

        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);

            // 기존 강좌 존재 확인
            Course existing = repository.findById(course.getId());
            if (existing == null) {
                throw new IllegalArgumentException("존재하지 않는 강좌입니다.");
            }

            // 강의코드 중복 체크 (자신 제외)
            Course duplicateCourse = repository.findByCode(course.getCode());
            if (duplicateCourse != null && !duplicateCourse.getId().equals(course.getId())) {
                throw new IllegalArgumentException("이미 존재하는 강의코드입니다.");
            }

            repository.update(course);
            session.commit();
            logger.info("강좌 수정 완료: {}", course.getCode());
        } catch (Exception e) {
            logger.error("강좌 수정 실패", e);
            throw new RuntimeException("강좌 정보 수정 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteCourse(Long id) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            CourseRepository repository = session.getMapper(CourseRepository.class);

            if (repository.findById(id) == null) {
                throw new IllegalArgumentException("존재하지 않는 강좌입니다.");
            }

            repository.deleteById(id);
            session.commit();
            logger.info("강좌 삭제 완료: id={}", id);
        } catch (Exception e) {
            logger.error("강좌 삭제 실패: id={}", id, e);
            throw new RuntimeException("강좌 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateCourse(Course course) {
        if (course.getCode() == null || course.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("강의코드는 필수입니다.");
        }
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("과목명은 필수입니다.");
        }
        if (course.getCredit() == null || course.getCredit() < 1 || course.getCredit() > 6) {
            throw new IllegalArgumentException("학점은 1~6 사이여야 합니다.");
        }
    }
}