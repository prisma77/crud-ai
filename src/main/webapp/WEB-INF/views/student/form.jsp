<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="${mode == 'edit' ? '학생 수정' : '학생 등록'}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
  <div class="row justify-content-center">
    <div class="col-md-8">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>👨‍🎓 ${mode == 'edit' ? '학생 수정' : '학생 등록'}</h2>
        <a href="${pageContext.request.contextPath}/students" class="btn btn-secondary">목록</a>
      </div>

      <!-- 에러 메시지 -->
      <c:if test="${not empty errors}">
        <div class="alert alert-danger">
          <h6>다음 오류를 수정해주세요:</h6>
          <ul class="mb-0">
            <c:forEach var="error" items="${errors}">
              <li>${error}</li>
            </c:forEach>
          </ul>
        </div>
      </c:if>

      <div class="card">
        <div class="card-body">
          <form method="post" action="${pageContext.request.contextPath}/students${mode == 'edit' ? '/'.concat(student.id ) : ''}">
            <div class="mb-3">
              <label for="studentNo" class="form-label">학번 *</label>
              <input type="text" class="form-control" id="studentNo" name="studentNo"
                     value="${student.studentNo}" required>
              <div class="form-text">예: 20250001</div>
            </div>

            <div class="mb-3">
              <label for="name" class="form-label">이름 *</label>
              <input type="text" class="form-control" id="name" name="name"
                     value="${student.name}" required>
            </div>

            <div class="mb-3">
              <label for="email" class="form-label">이메일</label>
              <input type="email" class="form-control" id="email" name="email"
                     value="${student.email}">
              <div class="form-text">선택사항입니다.</div>
            </div>

            <div class="mb-3">
              <label for="dept" class="form-label">학과</label>
              <input type="text" class="form-control" id="dept" name="dept"
                     value="${student.dept}">
              <div class="form-text">예: 컴퓨터공학과</div>
            </div>

            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
              <a href="${pageContext.request.contextPath}/students" class="btn btn-secondary">취소</a>
              <button type="submit" class="btn btn-primary">
                ${mode == 'edit' ? '수정' : '등록'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
