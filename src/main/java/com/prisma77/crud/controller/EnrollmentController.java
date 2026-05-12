package com.prisma77.crud.controller;

import com.prisma77.crud.domain.Enrollment;
import com.prisma77.crud.service.EnrollmentService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/enrollments/*" )
public class EnrollmentController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);
    private EnrollmentService enrollmentService = new EnrollmentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && pathInfo.equals("/enroll")) {
                handleEnroll(request, response);
            } else if (pathInfo != null && pathInfo.equals("/cancel")) {
                handleCancel(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("수강 컨트롤러 오류", e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
        }
    }

    private void handleEnroll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long studentId = Long.parseLong(request.getParameter("studentId"));
            Long courseId = Long.parseLong(request.getParameter("courseId"));

            enrollmentService.enrollStudent(studentId, courseId);
            response.sendRedirect(request.getContextPath() + "/students/" + studentId);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/students/" + request.getParameter("studentId"));
        }
    }

    private void handleCancel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long studentId = Long.parseLong(request.getParameter("studentId"));
            Long courseId = Long.parseLong(request.getParameter("courseId"));

            enrollmentService.cancelEnrollment(studentId, courseId);
            response.sendRedirect(request.getContextPath() + "/students/" + studentId);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/students/" + request.getParameter("studentId"));
        }
    }
}
