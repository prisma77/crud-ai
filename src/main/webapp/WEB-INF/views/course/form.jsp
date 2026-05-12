<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="${mode == 'edit' ? '강좌 수정' : '강좌 등록'}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
  <div class="row justify-content-center">
    <div class="col-md-8">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>📚 ${mode == 'edit' ? '강좌 수정' : '강좌 등록'}</h2>
        <a href="${pageContext.request.contextPath}/courses" class="btn btn-secondary">목록</a>
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
          <form method="post" action="${pageContext.request.contextPath}/courses${mode == 'edit' ? '/'.concat(course.id ) : ''}">
            <div class="mb-3">
              <label for="code" class="form-label">강의코드 *</label>
              <input type="text" class="form-control" id="code" name="code"
                     value="${course.code}" required>
              <div class="form-text">예: CS101, MATH201</div>
            </div>

            <div class="mb-3">
              <label for="title" class="form-label">과목명 *</label>
              <input type="text" class="form-control" id="title" name="title"
                     value="${course.title}" required>
              <div class="form-text">예: 프로그래밍 기초, 데이터베이스</div>
            </div>

            <div class="mb-3">
              <label for="professor" class="form-label">담당교수</label>
              <input type="text" class="form-control" id="professor" name="professor"
                     value="${course.professor}">
              <div class="form-text">예: 김교수, 이박사</div>
            </div>

            <div class="mb-3">
              <label for="credit" class="form-label">학점 *</label>
              <select class="form-select" id="credit" name="credit" required>
                <option value="">학점을 선택하세요</option>
                <c:forEach var="i" begin="1" end="6">
                  <option value="${i}" ${course.credit == i ? 'selected' : ''}>${i}학점</option>
                </c:forEach>
              </select>
              <div class="form-text">1~6학점 중 선택하세요.</div>
            </div>

            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
              <a href="${pageContext.request.contextPath}/courses" class="btn btn-secondary">취소</a>
              <button type="submit" class="btn btn-success">
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
