<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Login" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card" style="max-width: 440px;">
    <div class="card-header text-center py-3">
        <h3 class="mb-0"><i class="bi bi-box-arrow-in-right"></i> Welcome back</h3>
    </div>
    <div class="card-body p-4">
        <form action="${ctx}/login" method="post">
            <div class="mb-3">
                <label class="form-label" for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="<c:out value='${email}'/>" required autofocus>
            </div>
            <div class="mb-4">
                <label class="form-label" for="password">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-glam w-100">Log in</button>
        </form>
        <p class="text-center mt-3 mb-0">New to GlamBook? <a href="${ctx}/register">Create an account</a></p>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
