<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Write a Review" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card">
    <div class="card-header py-3">
        <h3 class="mb-0"><i class="bi bi-chat-heart"></i> Write a review</h3>
    </div>
    <div class="card-body p-4">
        <form action="${ctx}/reviews/submit" method="post">
            <div class="mb-3">
                <label class="form-label" for="target">What are you reviewing? *</label>
                <select class="form-select" id="target" name="target" required>
                    <option value="">-- choose --</option>
                    <optgroup label="Services">
                        <c:forEach var="s" items="${services}">
                            <option value="SERVICE:${s.serviceId}" ${target == 'SERVICE:'.concat(s.serviceId) ? 'selected' : ''}><c:out value="${s.name}"/></option>
                        </c:forEach>
                    </optgroup>
                    <optgroup label="Stylists">
                        <c:forEach var="st" items="${stylists}">
                            <option value="STYLIST:${st.stylistId}" ${target == 'STYLIST:'.concat(st.stylistId) ? 'selected' : ''}><c:out value="${st.name}"/></option>
                        </c:forEach>
                    </optgroup>
                </select>
            </div>
            <%@ include file="/review/rating-fields.jsp" %>
            <p class="small text-muted"><i class="bi bi-patch-check"></i> If you have a completed appointment for it,
                your review is marked as a <strong>Verified Visit</strong>.</p>
            <button type="submit" class="btn btn-glam w-100">Post review</button>
        </form>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
