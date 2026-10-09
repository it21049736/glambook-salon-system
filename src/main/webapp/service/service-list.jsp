<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Services" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="d-flex flex-wrap justify-content-between align-items-center mb-3 gap-2">
    <h2 class="mb-0"><i class="bi bi-stars"></i> Our Services</h2>
    <c:if test="${sessionScope.loggedUser.role == 'ADMIN'}">
        <a href="${ctx}/admin/services/add" class="btn btn-glam"><i class="bi bi-plus-lg"></i> Add Service</a>
    </c:if>
</div>

<!-- search and filter -->
<form class="row g-2 mb-4" action="${ctx}/services" method="get">
    <div class="col-md-6">
        <input type="text" class="form-control" name="q" value="<c:out value='${q}'/>" placeholder="Search services...">
    </div>
    <div class="col-md-3">
        <select class="form-select" name="category">
            <option value="">All categories</option>
            <option value="HAIR" ${category == 'HAIR' ? 'selected' : ''}>Hair</option>
            <option value="SKIN" ${category == 'SKIN' ? 'selected' : ''}>Skin</option>
            <option value="MAKEUP" ${category == 'MAKEUP' ? 'selected' : ''}>Makeup</option>
        </select>
    </div>
    <div class="col-md-3 d-flex gap-2">
        <button class="btn btn-glam flex-fill" type="submit"><i class="bi bi-search"></i> Search</button>
        <a class="btn btn-outline-secondary" href="${ctx}/services">Clear</a>
    </div>
</form>

<div class="row g-4">
    <c:forEach var="s" items="${services}">
        <div class="col-md-6 col-lg-4">
            <div class="card glam-card h-100">
                <div class="card-body d-flex flex-column">
                    <div class="d-flex justify-content-between mb-2">
                        <span class="badge bg-light text-dark border">${s.category}</span>
                        <small class="text-muted">${s.serviceId}</small>
                    </div>
                    <h5 class="card-title"><c:out value="${s.name}"/></h5>
                    <p class="text-muted small flex-grow-1"><c:out value="${s.description}"/></p>
                    <p class="small mb-1"><i class="bi bi-clock"></i> ${s.durationMinutes} minutes
                        &middot; ${s.extraLabel}: <c:out value="${s.extraDetail}"/></p>
                    <%-- calculatePrice() is different for each subclass (polymorphism) --%>
                    <p class="price-tag fs-5 mb-3">LKR <fmt:formatNumber value="${s.calculatePrice()}" pattern="#,##0.00"/></p>

                    <div class="d-flex gap-2">
                        <c:if test="${sessionScope.loggedUser.role == 'CUSTOMER'}">
                            <a class="btn btn-glam btn-sm" href="${ctx}/appointments/book?serviceId=${s.serviceId}">Book</a>
                        </c:if>
                        <a class="btn btn-outline-glam btn-sm" href="${ctx}/reviews?type=SERVICE&id=${s.serviceId}">Reviews</a>
                        <c:if test="${sessionScope.loggedUser.role == 'ADMIN'}">
                            <a class="btn btn-outline-secondary btn-sm" href="${ctx}/admin/services/edit?id=${s.serviceId}"><i class="bi bi-pencil"></i> Edit</a>
                            <form action="${ctx}/admin/services/delete" method="post" class="d-inline"
                                  data-confirm="Delete service ${s.serviceId}?">
                                <input type="hidden" name="serviceId" value="${s.serviceId}">
                                <button class="btn btn-outline-danger btn-sm"><i class="bi bi-trash"></i></button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty services}">
        <p class="text-center text-muted py-5">No services match your search.</p>
    </c:if>
</div>

<%@ include file="/includes/footer.jsp" %>
