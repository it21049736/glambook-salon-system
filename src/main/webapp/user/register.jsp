<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Register" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card">
    <div class="card-header text-center py-3">
        <h3 class="mb-0"><i class="bi bi-person-plus"></i> Create your account</h3>
    </div>
    <div class="card-body p-4">
        <form action="${ctx}/register" method="post" novalidate>
            <div class="mb-3">
                <label class="form-label" for="fullName">Full name *</label>
                <input type="text" class="form-control" id="fullName" name="fullName" value="<c:out value='${fullName}'/>" required>
            </div>
            <div class="mb-3">
                <label class="form-label" for="email">Email *</label>
                <input type="email" class="form-control" id="email" name="email" value="<c:out value='${email}'/>" required>
            </div>
            <div class="mb-3">
                <label class="form-label" for="phone">Phone number *</label>
                <input type="text" class="form-control" id="phone" name="phone" value="<c:out value='${phone}'/>"
                       placeholder="0771234567" maxlength="10" required>
                <div class="form-text">10 digits starting with 0.</div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label" for="password">Password *</label>
                    <input type="password" class="form-control" id="password" name="password" minlength="6" required>
                    <div class="form-text">At least 6 characters.</div>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label" for="confirmPassword">Confirm password *</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                </div>
            </div>
            <div class="mb-4">
                <label class="form-label" for="membership">Membership</label>
                <select class="form-select" id="membership" name="membership">
                    <option value="REGULAR" ${membership != 'PREMIUM' ? 'selected' : ''}>Regular</option>
                    <option value="PREMIUM" ${membership == 'PREMIUM' ? 'selected' : ''}>Premium (10% off every booking)</option>
                </select>
            </div>
            <button type="submit" class="btn btn-glam w-100">Register</button>
        </form>
        <p class="text-center mt-3 mb-0">Already have an account? <a href="${ctx}/login">Log in</a></p>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
