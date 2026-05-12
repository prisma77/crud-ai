<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="오류" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
  <div class="row justify-content-center">
    <div class="col-md-8">
      <div class="alert alert-danger" role="alert">
        <h4 class="alert-heading">⚠️ 오류가 발생했습니다</h4>
        <p class="mb-0">
          <c:choose>
            <c:when test="${not empty errorMessage}">
              ${errorMessage}
            </c:when>
            <c:otherwise>
              시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.
            </c:otherwise>
          </c:choose>
        </p>
        <hr>
        <div class="mb-0">
          <a href="${pageContext.request.contextPath}/" class="btn btn-primary">홈으로 돌아가기</a>
          <button onclick="history.back( )" class="btn btn-secondary">이전 페이지</button>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
