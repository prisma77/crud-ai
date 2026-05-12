package com.prisma77.crud.service;

import com.prisma77.crud.config.DatabaseConfig;
import com.prisma77.crud.domain.Student;
import com.prisma77.crud.repository.StudentRepository;
import com.prisma77.crud.util.PageInfo;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private static final int PAGE_SIZE = 10;

    public PageInfo<Student> getStudentsWithPaging(String keyword, int page) {
        if (keyword == null) keyword = "";
        if (page < 1) page = 1;

        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            StudentRepository repository = session.getMapper(StudentRepository.class);

            long totalItems = repository.countByKeyword(keyword);
            int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
            int offset = (page - 1) * PAGE_SIZE;

            List<Student> students = repository.findByKeywordWithPaging(keyword, PAGE_SIZE, offset);

            return new PageInfo<>(students, page, totalPages, totalItems, PAGE_SIZE);
        } catch (Exception e) {
            logger.error("학생 목록 조회 실패", e);
            throw new RuntimeException("학생 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    public Student getStudentById(Long id) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            StudentRepository repository = session.getMapper(StudentRepository.class);
            return repository.findById(id);
        } catch (Exception e) {
            logger.error("학생 조회 실패: id={}", id, e);
            throw new RuntimeException("학생 정보 조회 중 오류가 발생했습니다.", e);
        }
    }

    public void createStudent(Student student) {
        validateStudent(student);

        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            StudentRepository repository = session.getMapper(StudentRepository.class);

            // 학번 중복 체크
            if (repository.findByStudentNo(student.getStudentNo()) != null) {
                throw new IllegalArgumentException("이미 존재하는 학번입니다.");
            }

            repository.insert(student);
            session.commit();
            logger.info("학생 등록 완료: {}", student.getStudentNo());
        } catch (Exception e) {
            logger.error("학생 등록 실패", e);
            throw new RuntimeException("학생 등록 중 오류가 발생했습니다.", e);
        }
    }

    public void updateStudent(Student student) {
        validateStudent(student);

        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            StudentRepository repository = session.getMapper(StudentRepository.class);

            // 기존 학생 존재 확인
            Student existing = repository.findById(student.getId());
            if (existing == null) {
                throw new IllegalArgumentException("존재하지 않는 학생입니다.");
            }

            // 학번 중복 체크 (자신 제외)
            Student duplicateStudent = repository.findByStudentNo(student.getStudentNo());
            if (duplicateStudent != null && !duplicateStudent.getId().equals(student.getId())) {
                throw new IllegalArgumentException("이미 존재하는 학번입니다.");
            }

            repository.update(student);
            session.commit();
            logger.info("학생 수정 완료: {}", student.getStudentNo());
        } catch (Exception e) {
            logger.error("학생 수정 실패", e);
            throw new RuntimeException("학생 정보 수정 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteStudent(Long id) {
        try (SqlSession session = DatabaseConfig.getSqlSessionFactory().openSession()) {
            StudentRepository repository = session.getMapper(StudentRepository.class);

            if (repository.findById(id) == null) {
                throw new IllegalArgumentException("존재하지 않는 학생입니다.");
            }

            repository.deleteById(id);
            session.commit();
            logger.info("학생 삭제 완료: id={}", id);
        } catch (Exception e) {
            logger.error("학생 삭제 실패: id={}", id, e);
            throw new RuntimeException("학생 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateStudent(Student student) {
        if (student.getStudentNo() == null || student.getStudentNo().trim().isEmpty()) {
            throw new IllegalArgumentException("학번은 필수입니다.");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        if (student.getEmail() != null && !student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }
}
