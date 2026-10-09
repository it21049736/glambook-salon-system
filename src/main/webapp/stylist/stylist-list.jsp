<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Stylists" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="d-flex flex-wrap justify-content-between align-items-center mb-3 gap-2">
    <h2 class="mb-0"><i class="bi bi-people"></i> Meet our stylists</h2>
    <c:if test="${sessionScope.loggedUser.role == 'ADMIN'}">
        <a href="${ctx}/admin/stylists/add" class="btn btn-glam"><i class="bi bi-plus-lg"></i> Add Stylist</a>
    </c:if>
</div>

<!-- search by name and specialty -->
<form class="row g-2 mb-4" action="${ctx}/stylists" method="get">
    <div class="col-md-6">
        <input type="text" class="form-control" name="q" value="<c:out value='${q}'/>" placeholder="Search by name...">
    </div>
    <div class="col-md-3">
        <select class="form-select" name="specialty">
            <option value="">All specialties</option>
            <option value="HAIR" ${specialty == 'HAIR' ? 'selected' : ''}>Hair</option>
            <option value="SKIN" ${specialty == 'SKIN' ? 'selected' : ''}>Skin</option>
            <option value="MAKEUP" ${specialty == 'MAKEUP' ? 'selected' : ''}>Makeup</option>
        </select>
    </div>
    <div class="col-md-3 d-flex gap-2">
        <button class="btn btn-glam flex-fill" type="submit"><i class="bi bi-search"></i> Search</button>
        <a class="btn btn-outline-secondary" href="${ctx}/stylists">Clear</a>
    </div>
</form>

<div class="row g-4">
    <c:forEach var="st" items="${stylists}">
        <div class="col-md-6 col-lg-4">
            <div class="card glam-card h-100">
                <div class="card-body d-flex flex-column">
                    <div class="d-flex align-items-center mb-3">
                        <div class="feature-icon me-3" style="width:52px;height:52px;font-size:1.4rem;"><i class="bi bi-person"></i></div>
                        <div>
                            <h5 class="mb-0"><c:out value="${st.name}"/></h5>
                            <span class="badge ${st.level == 'SENIOR' ? 'btn-gold' : 'bg-secondary'}">${st.level}</span>
                            <span class="badge bg-light text-dark border">${st.specialty}</span>
                        </div>
                    </div>
                    <p class="small mb-1"><i class="bi bi-award"></i> ${st.experienceYears} years experience</p>
                    <p class="small mb-1"><i class="bi bi-calendar-week"></i> ${st.workingDays}</p>
                    <p class="small mb-3"><i class="bi bi-clock"></i> ${st.shiftStart} - ${st.shiftEnd}</p>

                    <%-- admins also see the commission rule (different for Senior and Junior) --%>
                    <c:if test="${sessionScope.loggedUser.role == 'ADMIN'}">
                        <p class="small text-muted mb-3">
                            ${st.stylistId} &middot; <c:out value="${st.phone}"/><br>
                            Commission: <fmt:formatNumber value="${st.commissionRate}" type="percent"/>
                            (LKR <fmt:formatNumber value="${st.calculateCommission(10000)}" pattern="#,##0"/> on a LKR 10,000 service)
                        </p>
                    </c:if>

                    <div class="d-flex gap-2 mt-auto">
                        <c:if test="${sessionScope.loggedUser.role == 'CUSTOMER'}">
                            <a class="btn btn-glam btn-sm" href="${ctx}/appointments/book?stylistId=${st.stylistId}">Book</a>
                        </c:if>
                        <a class="btn btn-outline-glam btn-sm" href="${ctx}/reviews?type=STYLIST&id=${st.stylistId}">Reviews</a>
                        <c:if test="${sessionScope.loggedUser.role == 'ADMIN'}">
                            <a class="btn btn-outline-secondary btn-sm" href="${ctx}/admin/stylists/edit?id=${st.stylistId}"><i class="bi bi-pencil"></i> Edit</a>
                            <form action="${ctx}/admin/stylists/delete" method="post" class="d-inline"
                                  data-confirm="Remove stylist ${st.stylistId}?">
                                <input type="hidden" name="stylistId" value="${st.stylistId}">
                                <button class="btn btn-outline-danger btn-sm"><i class="bi bi-trash"></i></button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty stylists}">
        <p class="text-center text-muted py-5">No stylists match your search.</p>
    </c:if>
</div>

<%@ include file="/includes/footer.jsp" %>
