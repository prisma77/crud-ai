<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="AI 조수" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/nav.jsp"/>

<div class="container">
    <div class="mb-4">
        <h2>✨ AI 어시스턴트</h2>
        <p class="text-muted">무엇이든 물어보세요! 학생 관리나 시스템 사용법에 대해 도와드립니다.</p>
    </div>

    <div class="card mb-4" style="height: 400px; overflow-y: auto; background-color: #f8f9fa;" id="chatWindow">
        <div class="card-body">
            <div id="loadingOverlay" class="text-center mt-5" style="display: none;">
                <div class="spinner-border text-primary" role="status" style="width: 3rem; height: 3rem;">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <h5 class="mt-3 text-primary">조회중..</h5>
            </div>

            <div id="chatContent">
                <c:if test="${not empty userMsg}">
                    <div class="text-end mb-3">
                        <span class="badge bg-primary p-2" style="font-size: 1rem;">나: ${userMsg}</span>
                    </div>
                    <div class="text-start mb-3">
                        <div class="card border-0 shadow-sm">
                            <div class="card-body bg-white rounded">
                                <strong>🤖:</strong><br>
                                <div class="mt-2">${aiResponse}</div>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${empty userMsg}">
                    <div class="text-center text-muted mt-5">
                        <h5>질문을 입력하면 답변해 드립니다.</h5>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/ai/chat" method="post" id="chatForm">
        <div class="input-group mb-3">
            <input type="text" name="message" id="messageInput" class="form-control form-control-lg"
                   placeholder="물어볼 내용을 입력하세요..." required>
            <button class="btn btn-primary" type="submit" id="sendBtn">전송</button>
        </div>
    </form>
</div>

<script>
    // 폼 전송 시 실행되는 로직
    document.getElementById('chatForm').onsubmit = function() {
        const input = document.getElementById('messageInput');
        if (input.value.trim() === "") return false;

        // 1. 기존 답변 영역 투명도 조절
        document.getElementById('chatContent').style.opacity = "0.3";
        // 2. "조회중.." 로딩창 표시
        document.getElementById('loadingOverlay').style.display = "block";
        // 3. 버튼 비활성화 및 텍스트 변경
        const btn = document.getElementById('sendBtn');
        btn.disabled = true;
        btn.innerText = "조회중..";

        return true;
    };

    // 페이지 로드 시 채팅창 하단으로 자동 스크롤
    window.onload = function() {
        const chatWindow = document.getElementById('chatWindow');
        chatWindow.scrollTop = chatWindow.scrollHeight;
    };
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>