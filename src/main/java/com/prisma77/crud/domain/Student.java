package com.prisma77.crud.domain;

import java.time.LocalDateTime;

public class Student {
    private Long id;
    private String studentNo;    // 학번
    private String name;         // 이름
    private String email;        // 이메일
    private String dept;         // 학과
    private LocalDateTime createdAt;  // 생성일

    // 기본 생성자
    public Student() {}

    // 생성자
    public Student(String studentNo, String name, String email, String dept) {
        this.studentNo = studentNo;
        this.name = name;
        this.email = email;
        this.dept = dept;
    }

    // Getter, Setter 메서드들
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
