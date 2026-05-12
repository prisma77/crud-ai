<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="학생 목록" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>👨‍🎓 학생 목록</h2>
    <a href="${pageContext.request.contextPath}/students/new" class="btn btn-primary">
      ➕ 학생 등록
    </a>
  </div>

  <!-- 검색 폼 -->
  <form class="row g-3 mb-4" method="get">
    <div class="col-md-10">
      <input type="text" name="q" value="${keyword}" class="form-control"
             placeholder="이름 또는 학번으로 검색하세요">
    </div>
    <div class="col-md-2">
      <button class="btn btn-outline-secondary w-100" type="submit">🔍 검색</button>
    </div>
  </form>

  <!-- 검색 결과 -->
  <c:if test="${not empty keyword}">
    <div class="alert alert-info">
      '<strong>${keyword}</strong>' 검색 결과: ${pageInfo.totalItems}건
    </div>
  </c:if>

  <!-- 학생 목록 -->
  <c:choose>
    <c:when test="${pageInfo.isEmpty( )}">
      <div class="alert alert-warning text-center">
        <h5>등록된 학생이 없습니다</h5>
        <p>새로운 학생을 등록해보세요.</p>
      </div>
    </c:when>
    <c:otherwise>
      <div class="table-responsive">
        <table class="table table-hover">
          <thead class="table-dark">
          <tr>
            <th>ID</th>
            <th>학번</th>
            <th>이름</th>
            <th>이메일</th>
            <th>학과</th>
            <th>관리</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="student" items="${pageInfo.items}">
            <tr>
              <td>${student.id}</td>
              <td>${student.studentNo}</td>
              <td>
                <a href="${pageContext.request.contextPath}/students/${student.id}"
                   class="text-decoration-none">
                    ${student.name}
                </a>
              </td>
              <td>${student.email}</td>
              <td>${student.dept}</td>
              <td>
                <a href="${pageContext.request.contextPath}/students/${student.id}/edit"
                   class="btn btn-sm btn-outline-primary">수정</a>
                <form method="post" action="${pageContext.request.contextPath}/students/${student.id}/delete"
                      class="d-inline" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                  <button class="btn btn-sm btn-outline-danger" type="submit">삭제</button>
                </form>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>

      <!-- 페이징 -->
      <c:if test="${pageInfo.totalPages > 1}">
        <nav aria-label="페이지 네비게이션">
          <ul class="pagination justify-content-center">
            <c:if test="${pageInfo.hasPrevious()}">
              <li class="page-item">
                <a class="page-link" href="?page=${pageInfo.previousPage}&q=${keyword}">이전</a>
              </li>
            </c:if>

            <c:forEach var="p" begin="1" end="${pageInfo.totalPages}">
              <li class="page-item ${p == pageInfo.currentPage ? 'active' : ''}">
                <a class="page-link" href="?page=${p}&q=${keyword}">${p}</a>
              </li>
            </c:forEach>

            <c:if test="${pageInfo.hasNext()}">
              <li class="page-item">
                <a class="page-link" href="?page=${pageInfo.nextPage}&q=${keyword}">다음</a>
              </li>
            </c:if>
          </ul>
        </nav>
      </c:if>
    </c:otherwise>
  </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
