<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Add Stylist" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card" style="max-width: 680px;">
    <div class="card-header py-3">
        <h3 class="mb-0"><i class="bi bi-person-badge"></i> Register a stylist</h3>
    </div>
    <div class="card-body p-4">
        <form action="${ctx}/admin/stylists/add" method="post">
            <%@ include file="/stylist/stylist-form-fields.jsp" %>
            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-glam flex-fill">Save stylist</button>
                <a href="${ctx}/stylists" class="btn btn-outline-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
