package com.prisma77.crud.controller;

import com.prisma77.crud.domain.Course;
import com.prisma77.crud.domain.Enrollment;
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

@WebServlet("/courses/*" )
public class CourseController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
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
            logger.error("강좌 컨트롤러 오류", e);
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
            logger.error("강좌 컨트롤러 오류", e);
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

        PageInfo<Course> pageInfo = courseService.getCoursesWithPaging(keyword, page);
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/WEB-INF/views/course/list.jsp").forward(request, response);
    }

    private void handleDetail(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        Long id = Long.parseLong(pathInfo.substring(1));
        Course course = courseService.getCourseById(id);

        if (course == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 해당 강좌의 수강자 목록
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(id);

        request.setAttribute("course", course);
        request.setAttribute("enrollments", enrollments);
        request.getRequestDispatcher("/WEB-INF/views/course/detail.jsp").forward(request, response);
    }

    private void handleNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "create");
        request.getRequestDispatcher("/WEB-INF/views/course/form.jsp").forward(request, response);
    }

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        Long id = Long.parseLong(pathInfo.substring(1, pathInfo.lastIndexOf("/edit")));
        Course course = courseService.getCourseById(id);

        if (course == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("course", course);
        request.setAttribute("mode", "edit");
        request.getRequestDispatcher("/WEB-INF/views/course/form.jsp").forward(request, response);
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<>();

        try {
            Course course = new Course();
            course.setCode(request.getParameter("code"));
            course.setTitle(request.getParameter("title"));
            course.setProfessor(request.getParameter("professor"));

            String creditParam = request.getParameter("credit");
            if (creditParam != null && !creditParam.trim().isEmpty()) {
                course.setCredit(Integer.parseInt(creditParam));
            }

            courseService.createCourse(course);
            response.sendRedirect(request.getContextPath() + "/courses");
        } catch (NumberFormatException e) {
            errors.add("학점은 숫자여야 합니다.");
            request.setAttribute("errors", errors);
            request.setAttribute("mode", "create");
            request.getRequestDispatcher("/WEB-INF/views/course/form.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            errors.add(e.getMessage());
            request.setAttribute("errors", errors);
            request.setAttribute("mode", "create");
            request.getRequestDispatcher("/WEB-INF/views/course/form.jsp").forward(request, response);
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        Long id = Long.parseLong(pathInfo.substring(1));
        List<String> errors = new ArrayList<>();

        try {
            Course course = new Course();
            course.setId(id);
            course.setCode(request.getParameter("code"));
            course.setTitle(request.getParameter("title"));
            course.setProfessor(request.getParameter("professor"));

            String creditParam = request.getParameter("credit");
            if (creditParam != null && !creditParam.trim().isEmpty()) {
                course.setCredit(Integer.parseInt(creditParam));
            }

            courseService.updateCourse(course);
            response.sendRedirect(request.getContextPath() + "/courses/" + id);
        } catch (NumberFormatException e) {
            errors.add("학점은 숫자여야 합니다.");
            Course course = courseService.getCourseById(id);
            request.setAttribute("course", course);
            request.setAttribute("errors", errors);
            request.setAttribute("mode", "edit");
            request.getRequestDispatcher("/WEB-INF/views/course/form.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            errors.add(e.getMessage());
            Course course = courseService.getCourseById(id);
            request.setAttribute("course", course);
            request.setAttribute("errors", errors);
            request.setAttribute("mode", "edit");
            request.getRequestDispatcher("/WEB-INF/views/course/form.jsp").forward(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        Long id = Long.parseLong(pathInfo.substring(1, pathInfo.lastIndexOf("/delete")));
        courseService.deleteCourse(id);
        response.sendRedirect(request.getContextPath() + "/courses");
    }
}