package com.prisma77.crud.domain;

import java.time.LocalDateTime;

public class Enrollment {
    private Long id;
    private Long studentId;      // 학생 ID (FK)
    private Long courseId;       // 강좌 ID (FK)
    private LocalDateTime enrolledAt;  // 수강신청일

    // 조인용 필드들
    private Student student;     // 학생 정보
    private Course course;       // 강좌 정보

    public Enrollment() {}

    public Enrollment(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Getter, Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
