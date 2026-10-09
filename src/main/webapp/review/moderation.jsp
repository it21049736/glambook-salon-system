<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Review Moderation" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<h2 class="mb-3"><i class="bi bi-shield-check"></i> Review Moderation</h2>
<p class="text-muted">Hidden reviews are not shown to customers. Deleting removes the review from the file.</p>

<div class="card glam-card">
    <div class="table-responsive">
        <table class="table table-hover table-glam align-middle mb-0">
            <thead>
            <tr>
                <th>ID</th>
                <th>Customer</th>
                <th>About</th>
                <th>Rating</th>
                <th style="min-width: 240px;">Comment</th>
                <th>Type</th>
                <th>Status</th>
                <th class="text-end">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="r" items="${reviews}">
                <tr class="${r.visible ? '' : 'table-secondary'}">
                    <td>${r.reviewId}<br><small class="text-muted">${r.reviewDate}</small></td>
                    <%-- admin view: full name, customer id and booking (see getDisplayName) --%>
                    <td><c:out value="${r.getDisplayName(true)}"/></td>
                    <td><small class="text-muted">${r.targetType}</small><br>
                        <c:out value="${r.targetType == 'STYLIST' ? stylistNames[r.targetId] : serviceNames[r.targetId]}"/></td>
                    <td class="rating-stars text-nowrap">${r.rating} <i class="bi bi-star-fill"></i></td>
                    <td><c:out value="${r.comment}"/></td>
                    <td><span class="badge ${r.type == 'VERIFIED' ? 'bg-success' : 'bg-light text-dark border'}">${r.badge}</span></td>
                    <td><span class="badge ${r.visible ? 'bg-primary' : 'bg-secondary'}">${r.status}</span></td>
                    <td class="text-end text-nowrap">
                        <form action="${ctx}/admin/reviews/status" method="post" class="d-inline">
                            <input type="hidden" name="reviewId" value="${r.reviewId}">
                            <input type="hidden" name="status" value="${r.visible ? 'HIDDEN' : 'VISIBLE'}">
                            <button class="btn btn-sm btn-outline-secondary">
                                <i class="bi ${r.visible ? 'bi-eye-slash' : 'bi-eye'}"></i> ${r.visible ? 'Hide' : 'Show'}
                            </button>
                        </form>
                        <form action="${ctx}/reviews/delete" method="post" class="d-inline"
                              data-confirm="Delete review ${r.reviewId} permanently?">
                            <input type="hidden" name="reviewId" value="${r.reviewId}">
                            <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty reviews}">
                <tr><td colspan="8" class="text-center text-muted py-4">No reviews yet.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
