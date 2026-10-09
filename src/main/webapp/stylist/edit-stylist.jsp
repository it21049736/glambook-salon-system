<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Edit Stylist" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card" style="max-width: 680px;">
    <div class="card-header d-flex justify-content-between align-items-center py-3">
        <h3 class="mb-0"><i class="bi bi-pencil-square"></i> Edit stylist</h3>
        <span class="badge bg-secondary">${stylist.stylistId}</span>
    </div>
    <div class="card-body p-4">
        <form action="${ctx}/admin/stylists/edit" method="post">
            <input type="hidden" name="stylistId" value="${stylist.stylistId}">
            <%@ include file="/stylist/stylist-form-fields.jsp" %>
            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-glam flex-fill">Update stylist</button>
                <a href="${ctx}/stylists" class="btn btn-outline-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
