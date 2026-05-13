<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="강좌 상세정보" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>📚 강좌 상세정보</h2>
        <div>
          <a href="${pageContext.request.contextPath}/courses/${course.id}/edit"
             class="btn btn-success">수정</a>
          <a href="${pageContext.request.contextPath}/courses"
             class="btn btn-secondary">목록</a>
        </div>
      </div>

      <div class="card mb-4">
        <div class="card-header">
          <h5 class="mb-0">📋 강좌 정보</h5>
        </div>
        <div class="card-body">
          <table class="table table-borderless">
            <tr>
              <th width="120">ID:</th>
              <td>${course.id}</td>
            </tr>
            <tr>
              <th>강의코드:</th>
              <td><span class="badge bg-light text-dark border fs-6">${course.code}</span></td>
            </tr>
            <tr>
              <th>과목명:</th>
              <td><strong>${course.title}</strong></td>
            </tr>
            <tr>
              <th>담당교수:</th>
              <td>${course.professor}</td>
            </tr>
            <tr>
              <th>학점:</th>
              <td><span class="badge bg-warning text-dark fs-6">${course.credit}학점</span></td>
            </tr>
            <tr>
              <th>등록일:</th>
              <td>${course.createdAt}</td>
            </tr>
          </table>
        </div>
      </div>

      <div class="card mb-4">
        <div class="card-header">
          <h5 class="mb-0">👥 수강자 목록 (총 ${enrollments.size()}명)</h5>
        </div>
        <div class="card-body">
          <c:if test="${empty enrollments}">
            <div class="alert alert-info">아직 수강신청한 학생이 없습니다.</div>
          </c:if>
          <c:if test="${not empty enrollments}">
            <div class="table-responsive">
              <table class="table table-hover">
                <thead class="table-light">
                <tr>
                  <th>학번</th>
                  <th>이름</th>
                  <th>이메일</th>
                  <th>학과</th>
                  <th>수강신청일</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="enrollment" items="${enrollments}">
                  <tr>
                    <td>${enrollment.student.studentNo}</td>
                    <td>
                      <a href="${pageContext.request.contextPath}/students/${enrollment.student.id}">
                          ${enrollment.student.name}
                      </a>
                    </td>
                    <td>${enrollment.student.email}</td>
                    <td>${enrollment.student.dept}</td>
                    <td>${enrollment.enrolledAt}</td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>
          </c:if>
        </div>
      </div>

      <div class="mt-3">
        <form method="post" action="${pageContext.request.contextPath}/courses/${course.id}/delete"
              onsubmit="return confirm('정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.' );">
          <button class="btn btn-danger" type="submit">🗑️ 강좌 삭제</button>
        </form>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>