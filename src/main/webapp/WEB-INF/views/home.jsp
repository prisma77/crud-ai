<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="홈" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
  <div class="row">
    <div class="col-12">
      <div class="jumbotron bg-primary text-white p-5 rounded mb-4">
        <h1 class="display-4">🎓 대학교 관리 시스템</h1>
        <p class="lead">학생, 강좌, 수강신청을 효율적으로 관리하세요.</p>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-6 mb-4">
      <div class="card h-100">
        <div class="card-body text-center">
          <h5 class="card-title">👨‍🎓 학생 관리</h5>
          <p class="card-text">학생 정보를 등록, 수정, 삭제하고 검색할 수 있습니다.</p>
          <a href="${pageContext.request.contextPath}/students" class="btn btn-primary">학생 관리로 이동</a>
        </div>
      </div>
    </div>

    <div class="col-md-6 mb-4">
      <div class="card h-100">
        <div class="card-body text-center">
          <h5 class="card-title">📚 강좌 관리</h5>
          <p class="card-text">강좌 정보를 등록, 수정, 삭제하고 검색할 수 있습니다.</p>
          <a href="${pageContext.request.contextPath}/courses" class="btn btn-success">강좌 관리로 이동</a>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
