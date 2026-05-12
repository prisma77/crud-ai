package com.prisma77.crud.service;

import com.prisma77.crud.config.DatabaseConfig;
import com.prisma77.crud.domain.Enrollment;
import com.prisma77.crud.repository.EnrollmentRepository;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EnrollmentService {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    public List<Enrollment> getEnrollmentsByStudentId(Long studentId) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            EnrollmentRepository repository = session.getMapper(EnrollmentRepository.class);
            return repository.findByStudentId(studentId);
        } catch (Exception e) {
            logger.error("학생 수강 목록 조회 실패: studentId={}", studentId, e);
            throw new RuntimeException("수강 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            EnrollmentRepository repository = session.getMapper(EnrollmentRepository.class);
            return repository.findByCourseId(courseId);
        } catch (Exception e) {
            logger.error("강좌 수강자 목록 조회 실패: courseId={}", courseId, e);
            throw new RuntimeException("수강자 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    public void enrollStudent(Long studentId, Long courseId) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            EnrollmentRepository repository = session.getMapper(EnrollmentRepository.class);

            // 중복 수강 체크
            if (repository.countByStudentAndCourse(studentId, courseId) > 0) {
                throw new IllegalArgumentException("이미 수강신청한 강좌입니다.");
            }

            Enrollment enrollment = new Enrollment(studentId, courseId);
            repository.insert(enrollment);
            session.commit();
            logger.info("수강신청 완료: studentId={}, courseId={}", studentId, courseId);
        } catch (Exception e) {
            logger.error("수강신청 실패: studentId={}, courseId={}", studentId, courseId, e);
            throw new RuntimeException("수강신청 중 오류가 발생했습니다.", e);
        }
    }

    public void cancelEnrollment(Long studentId, Long courseId) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            EnrollmentRepository repository = session.getMapper(EnrollmentRepository.class);

            // 수강 여부 체크
            if (repository.countByStudentAndCourse(studentId, courseId) == 0) {
                throw new IllegalArgumentException("수강하지 않은 강좌입니다.");
            }

            repository.deleteByStudentAndCourse(studentId, courseId);
            session.commit();
            logger.info("수강취소 완료: studentId={}, courseId={}", studentId, courseId);
        } catch (Exception e) {
            logger.error("수강취소 실패: studentId={}, courseId={}", studentId, courseId, e);
            throw new RuntimeException("수강취소 중 오류가 발생했습니다.", e);
        }
    }
}
