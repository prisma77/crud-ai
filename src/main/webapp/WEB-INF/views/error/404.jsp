<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="페이지를 찾을 수 없음" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
  <div class="row justify-content-center">
    <div class="col-md-8 text-center">
      <h1 class="display-1">404</h1>
      <h2>페이지를 찾을 수 없습니다</h2>
      <p class="lead">요청하신 페이지가 존재하지 않거나 이동되었습니다.</p>
      <a href="${pageContext.request.contextPath}/" class="btn btn-primary">홈으로 돌아가기</a>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
