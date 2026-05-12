<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="학생 상세정보" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>👨‍🎓 학생 상세정보</h2>
        <div>
          <a href="${pageContext.request.contextPath}/students/${student.id}/edit"
             class="btn btn-primary">수정</a>
          <a href="${pageContext.request.contextPath}/students"
             class="btn btn-secondary">목록</a>
        </div>
      </div>

      <!-- 학생 기본 정보 -->
      <div class="card mb-4">
        <div class="card-header">
          <h5 class="mb-0">📋 기본 정보</h5>
        </div>
        <div class="card-body">
          <table class="table table-borderless">
            <tr>
              <th width="120">ID:</th>
              <td>${student.id}</td>
            </tr>
            <tr>
              <th>학번:</th>
              <td>${student.studentNo}</td>
            </tr>
            <tr>
              <th>이름:</th>
              <td>${student.name}</td>
            </tr>
            <tr>
              <th>이메일:</th>
              <td>${student.email}</td>
            </tr>
            <tr>
              <th>학과:</th>
              <td>${student.dept}</td>
            </tr>
            <tr>
              <th>등록일:</th>
              <td>${student.createdAt}</td>
            </tr>
          </table>
        </div>
      </div>

      <!-- 현재 수강 중인 강좌 -->
      <div class="card mb-4">
        <div class="card-header">
          <h5 class="mb-0">📚 수강 중인 강좌</h5>
        </div>
        <div class="card-body">
          <c:if test="${empty enrollments}">
            <div class="alert alert-info">수강 중인 강좌가 없습니다.</div>
          </c:if>
          <c:if test="${not empty enrollments}">
            <div class="table-responsive">
              <table class="table table-hover">
                <thead class="thead-light">
                <tr>
                  <th>강의코드</th>
                  <th>과목명</th>
                  <th>담당교수</th>
                  <th>학점</th>
                  <th>수강신청일</th>
                  <th>액션</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="enrollment" items="${enrollments}">
                  <tr>
                    <td>${enrollment.course.code}</td>
                    <td>
                      <a href="${pageContext.request.contextPath}/courses/${enrollment.course.id}">
                          ${enrollment.course.title}
                      </a>
                    </td>
                    <td>${enrollment.course.professor}</td>
                    <td>${enrollment.course.credit}</td>
                    <td>${enrollment.enrolledAt}</td>
                    <td>
                      <form method="post" action="${pageContext.request.contextPath}/enrollments/cancel"
                            class="d-inline" onsubmit="return confirm('수강을 취소하시겠습니까?');">
                        <input type="hidden" name="studentId" value="${student.id}">
                        <input type="hidden" name="courseId" value="${enrollment.course.id}">
                        <button type="submit" class="btn btn-sm btn-outline-danger">수강취소</button>
                      </form>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>
          </c:if>
        </div>
      </div>

      <!-- 수강신청 -->
      <div class="card mb-4">
        <div class="card-header">
          <h5 class="mb-0">➕ 수강신청</h5>
        </div>
        <div class="card-body">
          <c:if test="${empty availableCourses}">
            <div class="alert alert-info">수강 가능한 강좌가 없습니다.</div>
          </c:if>
          <c:if test="${not empty availableCourses}">
            <form method="post" action="${pageContext.request.contextPath}/enrollments/enroll" class="form-inline">
              <input type="hidden" name="studentId" value="${student.id}">
              <div class="form-group mr-3">
                <label for="courseId" class="mr-2">강좌 선택:</label>
                <select name="courseId" id="courseId" class="form-control" required>
                  <option value="">-- 강좌를 선택하세요 --</option>
                  <c:forEach var="course" items="${availableCourses}">
                    <option value="${course.id}">
                      [${course.code}] ${course.title} (${course.professor}, ${course.credit}학점)
                    </option>
                  </c:forEach>
                </select>
              </div>
              <button type="submit" class="btn btn-success">수강신청</button>
            </form>

            <div class="mt-3">
              <h6>수강 가능한 강좌 목록:</h6>
              <div class="table-responsive">
                <table class="table table-sm table-striped">
                  <thead>
                  <tr>
                    <th>강의코드</th>
                    <th>과목명</th>
                    <th>담당교수</th>
                    <th>학점</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="course" items="${availableCourses}">
                    <tr>
                      <td>${course.code}</td>
                      <td>
                        <a href="${pageContext.request.contextPath}/courses/${course.id}">
                            ${course.title}
                        </a>
                      </td>
                      <td>${course.professor}</td>
                      <td>${course.credit}</td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
            </div>
          </c:if>
        </div>
      </div>

      <!-- 삭제 버튼 -->
      <div class="mt-3">
        <form method="post" action="${pageContext.request.contextPath}/students/${student.id}/delete"
              onsubmit="return confirm('정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.' );">
          <button class="btn btn-danger" type="submit">🗑️ 학생 삭제</button>
        </form>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
