<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Edit Review" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card">
    <div class="card-header d-flex justify-content-between align-items-center py-3">
        <h3 class="mb-0"><i class="bi bi-pencil-square"></i> Edit review</h3>
        <span class="badge bg-secondary">${review.reviewId} &middot; ${review.badge}</span>
    </div>
    <div class="card-body p-4">
        <form action="${ctx}/reviews/edit" method="post">
            <input type="hidden" name="reviewId" value="${review.reviewId}">
            <%@ include file="/review/rating-fields.jsp" %>
            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-glam flex-fill">Save changes</button>
                <a href="${ctx}/reviews?type=${review.targetType}&id=${review.targetId}" class="btn btn-outline-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
