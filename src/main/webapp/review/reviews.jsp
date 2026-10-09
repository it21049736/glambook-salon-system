<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Reviews" scope="request"/>
<%@ include file="/includes/header.jsp" %>
<c:set var="isAdmin" value="${sessionScope.loggedUser.role == 'ADMIN'}"/>
<c:set var="targetName" value="${type == 'STYLIST' ? stylistNames[id] : serviceNames[id]}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center mb-3 gap-2">
    <h2 class="mb-0"><i class="bi bi-star"></i>
        <c:choose>
            <c:when test="${not empty targetName}">Reviews for <c:out value="${targetName}"/></c:when>
            <c:otherwise>Latest reviews</c:otherwise>
        </c:choose>
    </h2>
    <c:if test="${sessionScope.loggedUser.role == 'CUSTOMER'}">
        <a class="btn btn-glam" href="${ctx}/reviews/submit?type=${type}&id=${id}"><i class="bi bi-pencil"></i> Write a review</a>
    </c:if>
</div>

<!-- choose what to view -->
<div class="row g-2 mb-4">
    <div class="col-md-6">
        <select class="form-select" onchange="if (this.value) location.href = this.value;">
            <option value="${ctx}/reviews">All latest reviews</option>
            <optgroup label="Services">
                <c:forEach var="s" items="${services}">
                    <option value="${ctx}/reviews?type=SERVICE&id=${s.serviceId}" ${id == s.serviceId ? 'selected' : ''}><c:out value="${s.name}"/></option>
                </c:forEach>
            </optgroup>
            <optgroup label="Stylists">
                <c:forEach var="st" items="${stylists}">
                    <option value="${ctx}/reviews?type=STYLIST&id=${st.stylistId}" ${id == st.stylistId ? 'selected' : ''}><c:out value="${st.name}"/></option>
                </c:forEach>
            </optgroup>
        </select>
    </div>
    <c:if test="${not empty reviews}">
        <div class="col-md-6 d-flex align-items-center">
            <span class="rating-stars fs-5 me-2">
                <c:forEach begin="1" end="5" var="i"><i class="bi ${i <= averageRating + 0.5 ? 'bi-star-fill' : 'bi-star'}"></i></c:forEach>
            </span>
            <span><fmt:formatNumber value="${averageRating}" pattern="0.0"/> average from ${reviews.size()} review(s)</span>
        </div>
    </c:if>
</div>

<div class="row g-3">
    <c:forEach var="r" items="${reviews}">
        <div class="col-md-6">
            <div class="card glam-card h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <span class="rating-stars">
                                <c:forEach begin="1" end="5" var="i"><i class="bi ${i <= r.rating ? 'bi-star-fill' : 'bi-star'}"></i></c:forEach>
                            </span>
                            <%-- getBadge() is overridden in PublicReview and VerifiedReview --%>
                            <span class="badge ${r.type == 'VERIFIED' ? 'bg-success' : 'bg-light text-dark border'} ms-1">
                                <c:if test="${r.type == 'VERIFIED'}"><i class="bi bi-patch-check"></i></c:if> ${r.badge}
                            </span>
                        </div>
                        <small class="text-muted">${r.reviewDate}</small>
                    </div>
                    <p class="my-2"><c:out value="${r.comment}"/></p>
                    <small class="text-muted">
                        <%-- different display for admin and for normal users --%>
                        &mdash; <c:out value="${r.getDisplayName(isAdmin)}"/>
                        on <c:out value="${r.targetType == 'STYLIST' ? stylistNames[r.targetId] : serviceNames[r.targetId]}"/>
                    </small>

                    <c:if test="${r.customerId == sessionScope.loggedUser.userId or isAdmin}">
                        <div class="mt-2 d-flex gap-2">
                            <c:if test="${r.customerId == sessionScope.loggedUser.userId}">
                                <a class="btn btn-sm btn-outline-secondary" href="${ctx}/reviews/edit?id=${r.reviewId}"><i class="bi bi-pencil"></i> Edit</a>
                            </c:if>
                            <form action="${ctx}/reviews/delete" method="post" data-confirm="Delete this review?">
                                <input type="hidden" name="reviewId" value="${r.reviewId}">
                                <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i> Delete</button>
                            </form>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty reviews}">
        <p class="text-center text-muted py-5">No reviews yet. Be the first to share your experience!</p>
    </c:if>
</div>

<%@ include file="/includes/footer.jsp" %>
