package com.prisma77.crud.controller;

import com.prisma77.crud.domain.Course;
import com.prisma77.crud.domain.Student;
import com.prisma77.crud.domain.Enrollment;
import com.prisma77.crud.service.AiService;
import com.prisma77.crud.service.CourseService;
import com.prisma77.crud.service.StudentService;
import com.prisma77.crud.service.EnrollmentService;
import com.prisma77.crud.util.PageInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ai/chat")
public class AiChatController extends HttpServlet {
    private AiService aiService = new AiService();
    private CourseService courseService = new CourseService();
    private StudentService studentService = new StudentService();
    private EnrollmentService enrollmentService = new EnrollmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/ai/chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userMsg = request.getParameter("message");

        // 1. 실시간 데이터 추출
        List<Course> courses = courseService.getAllCourses();

        // ✨ 속도/로직 개선: 1페이지(10명)만 가져오던 것을 순회하여 100명 전체를 가져오도록 수정
        List<Student> students = new ArrayList<>();
        PageInfo<Student> firstPage = studentService.getStudentsWithPaging("", 1);
        students.addAll(firstPage.getItems());
        for (int i = 2; i <= firstPage.getTotalPages(); i++) {
            students.addAll(studentService.getStudentsWithPaging("", i).getItems());
        }

        // 2. 통합 데이터 컨텍스트 구성
        StringBuilder dbContext = new StringBuilder("=== 대학교 시스템 실시간 데이터 ===\n\n");

        dbContext.append("[강좌 정보]\n");
        for (Course c : courses) {
            dbContext.append(String.format("- [%s] %s (교수: %s, %d학점, 정원: %d명)\n",
                    c.getCode(), c.getTitle(), c.getProfessor(), c.getCredit(), c.getCapacity()));
        }

        dbContext.append("\n[학생 및 수강신청 내역]\n");
        for (Student s : students) {
            // 학생별 수강 목록 가져오기
            List<Enrollment> studentEnrollments = enrollmentService.getEnrollmentsByStudentId(s.getId());
            StringBuilder courseNames = new StringBuilder();
            if (studentEnrollments.isEmpty()) {
                courseNames.append("없음");
            } else {
                for (int i = 0; i < studentEnrollments.size(); i++) {
                    courseNames.append(studentEnrollments.get(i).getCourse().getTitle());
                    if (i < studentEnrollments.size() - 1) courseNames.append(", ");
                }
            }
            dbContext.append(String.format("- %s (학번: %s, 학과: %s, 이메일: %s) / 수강중: %s\n",
                    s.getName(), s.getStudentNo(), s.getDept(), s.getEmail(), courseNames.toString()));
        }

        try {
            // 3. AI 호출
            String aiResponse = aiService.askGemini(userMsg, dbContext.toString());

            request.setAttribute("userMsg", userMsg);
            request.setAttribute("aiResponse", aiResponse);
            request.getRequestDispatcher("/WEB-INF/views/ai/chat.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", "AI 통신 중 오류 발생: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
        }
    }
}