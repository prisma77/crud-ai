package com.prisma77.crud.domain;

import java.time.LocalDateTime;

public class Course {
    private Long id;
    private String code;         // 강의코드
    private String title;        // 과목명
    private String professor;    // 담당교수
    private Integer credit;      // 학점
    private LocalDateTime createdAt;

    public Course() {}

    public Course(String code, String title, String professor, Integer credit) {
        this.code = code;
        this.title = title;
        this.professor = professor;
        this.credit = credit;
    }

    // Getter, Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }

    public Integer getCredit() { return credit; }
    public void setCredit(Integer credit) { this.credit = credit; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
