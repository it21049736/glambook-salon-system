<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Something went wrong" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="text-center py-5">
    <i class="bi bi-emoji-frown" style="font-size: 5rem; color: var(--glam-rose);"></i>
    <h1 class="mt-3">Oops!</h1>
    <c:choose>
        <c:when test="${requestScope['jakarta.servlet.error.status_code'] == 404}">
            <p class="lead">We could not find the page you were looking for.</p>
        </c:when>
        <c:otherwise>
            <p class="lead">Something went wrong on our side. Please try again.</p>
        </c:otherwise>
    </c:choose>
    <a href="${ctx}/" class="btn btn-glam mt-3"><i class="bi bi-house"></i> Back to Home</a>
</div>

<%@ include file="/includes/footer.jsp" %>
