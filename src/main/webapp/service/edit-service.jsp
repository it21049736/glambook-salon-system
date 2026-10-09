<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Edit Service" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card">
    <div class="card-header d-flex justify-content-between align-items-center py-3">
        <h3 class="mb-0"><i class="bi bi-pencil-square"></i> Edit service</h3>
        <span class="badge bg-secondary">${service.serviceId} &middot; ${service.category}</span>
    </div>
    <div class="card-body p-4">
        <form action="${ctx}/admin/services/edit" method="post">
            <input type="hidden" name="serviceId" value="${service.serviceId}">
            <div class="mb-3">
                <label class="form-label" for="name">Service name *</label>
                <input type="text" class="form-control" id="name" name="name" value="<c:out value='${service.name}'/>" required>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label" for="basePrice">Base price (LKR) *</label>
                    <input type="number" step="0.01" min="1" class="form-control" id="basePrice" name="basePrice" value="${service.basePrice}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label" for="duration">Duration (minutes) *</label>
                    <input type="number" min="1" max="480" class="form-control" id="duration" name="duration" value="${service.durationMinutes}" required>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label" for="description">Description</label>
                <textarea class="form-control" id="description" name="description" rows="2"><c:out value="${service.description}"/></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label" for="extra">${service.extraLabel}</label>
                <c:choose>
                    <c:when test="${service.category == 'HAIR'}">
                        <select class="form-select" id="extra" name="extra">
                            <option value="SHORT" ${service.extraDetail == 'SHORT' ? 'selected' : ''}>Short</option>
                            <option value="MEDIUM" ${service.extraDetail == 'MEDIUM' ? 'selected' : ''}>Medium</option>
                            <option value="LONG" ${service.extraDetail == 'LONG' ? 'selected' : ''}>Long</option>
                        </select>
                    </c:when>
                    <c:when test="${service.category == 'MAKEUP'}">
                        <select class="form-select" id="extra" name="extra">
                            <option value="CASUAL" ${service.extraDetail == 'CASUAL' ? 'selected' : ''}>Casual</option>
                            <option value="PARTY" ${service.extraDetail == 'PARTY' ? 'selected' : ''}>Party</option>
                            <option value="BRIDAL" ${service.extraDetail == 'BRIDAL' ? 'selected' : ''}>Bridal</option>
                        </select>
                    </c:when>
                    <c:otherwise>
                        <input type="text" class="form-control" id="extra" name="extra" value="<c:out value='${service.extraDetail}'/>">
                    </c:otherwise>
                </c:choose>
            </div>

            <p class="small text-muted mb-4">Current customer price:
                <strong class="price-tag">LKR <fmt:formatNumber value="${service.calculatePrice()}" pattern="#,##0.00"/></strong></p>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-glam flex-fill">Update service</button>
                <a href="${ctx}/services" class="btn btn-outline-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
