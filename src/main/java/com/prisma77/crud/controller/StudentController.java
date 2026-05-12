package com.prisma77.crud.controller;

import com.prisma77.crud.domain.Student;
import com.prisma77.crud.domain.Course;
import com.prisma77.crud.domain.Enrollment;
import com.prisma77.crud.service.StudentService;
import com.prisma77.crud.service.CourseService;
import com.prisma77.crud.service.EnrollmentService;
import com.prisma77.crud.util.PageInfo;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/students/*" )
public class StudentController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private StudentService studentService = new StudentService();
    private CourseService courseService = new CourseService();
    private EnrollmentService enrollmentService = new EnrollmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleList(request, response);
            } else if (pathInfo.equals("/new")) {
                handleNewForm(request, response);
            } else if (pathInfo.matches("/\\d+")) {
                handleDetail(request, response, pathInfo);
            } else if (pathInfo.matches("/\\d+/edit")) {
                handleEditForm(request, response, pathInfo);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("학생 컨트롤러 오류", e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleCreate(request, response);
            } else if (pathInfo.matches("/\\d+")) {
                handleUpdate(request, response, pathInfo);
            } else if (pathInfo.matches("/\\d+/delete")) {
                handleDelete(request, response, pathInfo);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("학생 컨트롤러 오류", e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("q");
        String pageParam = request.getParameter("page");
        int page = 1;

        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        PageInfo<Student> pageInfo = studentService.getStudentsWithPaging(keyword, page);
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/WEB-INF/views/student/list.jsp").forward(request, response);
    }

    private void handleDetail(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        Long id = Long.parseLong(pathInfo.substring(1));
        Student student = studentService.getStudentById(id);

        if (student == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 현재 수강 중인 강좌 목록
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(id);

        // 수강 가능한 강좌 목록 (전체 강좌에서 수강 중인 강좌 제외)
        List<Course> allCourses = courseService.getAllCourses();
        List<Course> availableCourses = new ArrayList<>();

        for (Course course : allCourses) {
            boolean isEnrolled = false;
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourseId().equals(course.getId())) {
                    isEnrolled = true;
                    break;
                }
            }
            if (!isEnrolled) {
                availableCourses.add(course);
            }
        }

        request.setAttribute("student", student);
        request.setAttribute("enrollments", enrollments);
        request.setAttribute("availableCourses", availableCourses);
        request.getRequestDispatcher("/WEB-INF/views/student/detail.jsp").forward(request, response);
    }

    private void handleNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "create");
        request.getRequestDispatcher("/WEB-INF/views/student/form.jsp").forward(request, response);
    }

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        Long id = Long.parseLong(pathInfo.substring(1, pathInfo.lastIndexOf("/edit")));
        Student student = studentService.getStudentById(id);

        if (student == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("student", student);
        request.setAttribute("mode", "edit");
        request.getRequestDispatcher("/WEB-INF/views/student/form.jsp").forward(request, response);
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<>();

        try {
            Student student = new Student();
            student.setStudentNo(request.getParameter("studentNo"));
            student.setName(request.getParameter("name"));
            student.setEmail(request.getParameter("email"));
            student.setDept(request.getParameter("dept"));

            studentService.createStudent(student);
            response.sendRedirect(request.getContextPath() + "/students");
        } catch (IllegalArgumentException e) {
            errors.add(e.getMessage());
            request.setAttribute("errors", errors);
            request.setAttribute("mode", "create");
            request.getRequestDispatcher("/WEB-INF/views/student/form.jsp").forward(request, response);
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        Long id = Long.parseLong(pathInfo.substring(1));
        List<String> errors = new ArrayList<>();

        try {
            Student student = new Student();
            student.setId(id);
            student.setStudentNo(request.getParameter("studentNo"));
            student.setName(request.getParameter("name"));
            student.setEmail(request.getParameter("email"));
            student.setDept(request.getParameter("dept"));

            studentService.updateStudent(student);
            response.sendRedirect(request.getContextPath() + "/students/" + id);
        } catch (IllegalArgumentException e) {
            errors.add(e.getMessage());
            Student student = studentService.getStudentById(id);
            request.setAttribute("student", student);
            request.setAttribute("errors", errors);
            request.setAttribute("mode", "edit");
            request.getRequestDispatcher("/WEB-INF/views/student/form.jsp").forward(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        Long id = Long.parseLong(pathInfo.substring(1, pathInfo.lastIndexOf("/delete")));
        studentService.deleteStudent(id);
        response.sendRedirect(request.getContextPath() + "/students");
    }
}