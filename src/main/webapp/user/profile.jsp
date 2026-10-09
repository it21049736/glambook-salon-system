<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Profile" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card">
    <div class="card-header d-flex justify-content-between align-items-center py-3">
        <h3 class="mb-0"><i class="bi bi-person-circle"></i> My Profile</h3>
        <span class="badge bg-secondary">${profile.userId} &middot; ${profile.role}</span>
    </div>
    <div class="card-body p-4">
        <form action="${ctx}/profile" method="post">
            <div class="mb-3">
                <label class="form-label" for="fullName">Full name *</label>
                <input type="text" class="form-control" id="fullName" name="fullName" value="<c:out value='${profile.fullName}'/>" required>
            </div>
            <div class="mb-3">
                <label class="form-label" for="email">Email *</label>
                <input type="email" class="form-control" id="email" name="email" value="<c:out value='${profile.email}'/>" required>
            </div>
            <div class="mb-3">
                <label class="form-label" for="phone">Phone number *</label>
                <input type="text" class="form-control" id="phone" name="phone" value="<c:out value='${profile.phone}'/>" maxlength="10" required>
            </div>

            <%-- only customers have a membership --%>
            <c:if test="${profile.role == 'CUSTOMER'}">
                <div class="mb-3">
                    <label class="form-label" for="membership">Membership</label>
                    <select class="form-select" id="membership" name="membership">
                        <option value="REGULAR" ${profile.membership == 'REGULAR' ? 'selected' : ''}>Regular</option>
                        <option value="PREMIUM" ${profile.membership == 'PREMIUM' ? 'selected' : ''}>Premium (10% off every booking)</option>
                    </select>
                </div>
            </c:if>

            <div class="mb-4">
                <label class="form-label" for="newPassword">New password</label>
                <input type="password" class="form-control" id="newPassword" name="newPassword" minlength="6">
                <div class="form-text">Leave empty to keep your current password.</div>
            </div>
            <button type="submit" class="btn btn-glam w-100">Save changes</button>
        </form>

        <c:if test="${profile.role == 'CUSTOMER'}">
            <hr class="my-4">
            <form action="${ctx}/profile/delete" method="post"
                  data-confirm="Delete your account permanently? This cannot be undone.">
                <button type="submit" class="btn btn-outline-danger w-100"><i class="bi bi-trash"></i> Delete my account</button>
            </form>
        </c:if>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
