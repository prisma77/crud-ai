<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="서버 오류" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
  <div class="row justify-content-center">
    <div class="col-md-8 text-center">
      <h1 class="display-1">500</h1>
      <h2>서버 내부 오류</h2>
      <p class="lead">서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.</p>
      <a href="${pageContext.request.contextPath}/" class="btn btn-primary">홈으로 돌아가기</a>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
