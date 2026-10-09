<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Admin Dashboard" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<h2 class="mb-4"><i class="bi bi-speedometer2"></i> Admin Dashboard</h2>

<div class="row g-3 mb-4">
    <div class="col-6 col-lg-3">
        <div class="card glam-card stat-card p-3 h-100">
            <div class="text-muted small">Customers</div>
            <div class="stat-number">${customerCount}</div>
            <div class="small">${premiumCount} premium members</div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card glam-card stat-card p-3 h-100">
            <div class="text-muted small">Admins</div>
            <div class="stat-number">${adminCount}</div>
            <a class="small" href="${ctx}/admin/users">Manage users</a>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card glam-card stat-card p-3 h-100">
            <div class="text-muted small">Services</div>
            <div class="stat-number">${serviceCount}</div>
            <a class="small" href="${ctx}/services">Manage services</a>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card glam-card stat-card p-3 h-100">
            <div class="text-muted small">Stylists</div>
            <div class="stat-number">${stylistCount}</div>
            <a class="small" href="${ctx}/stylists">Manage stylists</a>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card glam-card stat-card p-3 h-100">
            <div class="text-muted small">Upcoming appointments</div>
            <div class="stat-number">${upcomingCount}</div>
            <div class="small">${todayCount} today &middot; <a href="${ctx}/appointments/my">view all</a></div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card glam-card stat-card p-3 h-100">
            <div class="text-muted small">Revenue (paid)</div>
            <div class="stat-number" style="font-size: 1.5rem;">LKR <fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></div>
            <div class="small">${pendingPayments} pending &middot; <a href="${ctx}/payments/history">payments</a></div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card glam-card stat-card p-3 h-100">
            <div class="text-muted small">Reviews</div>
            <div class="stat-number">${reviewCount}</div>
            <div class="small"><fmt:formatNumber value="${averageRating}" pattern="0.0"/> avg &middot; ${hiddenReviews} hidden</div>
        </div>
    </div>
</div>

<div class="row g-4">
    <div class="col-lg-4">
        <div class="card glam-card h-100">
            <div class="card-header">Quick actions</div>
            <div class="list-group list-group-flush">
                <a class="list-group-item list-group-item-action" href="${ctx}/admin/services/add"><i class="bi bi-plus-circle"></i> Add a service</a>
                <a class="list-group-item list-group-item-action" href="${ctx}/admin/stylists/add"><i class="bi bi-person-plus"></i> Register a stylist</a>
                <a class="list-group-item list-group-item-action" href="${ctx}/appointments/my?status=BOOKED"><i class="bi bi-calendar-check"></i> Booked appointments</a>
                <a class="list-group-item list-group-item-action" href="${ctx}/payments/history"><i class="bi bi-receipt"></i> Payments and invoices</a>
                <a class="list-group-item list-group-item-action" href="${ctx}/admin/reviews"><i class="bi bi-shield-check"></i> Moderate reviews</a>
            </div>
        </div>
    </div>
    <div class="col-lg-8">
        <div class="card glam-card h-100">
            <div class="card-header">Latest feedback</div>
            <ul class="list-group list-group-flush">
                <c:forEach var="r" items="${latestReviews}">
                    <li class="list-group-item">
                        <span class="rating-stars">${r.rating} <i class="bi bi-star-fill"></i></span>
                        <span class="badge ${r.visible ? 'bg-primary' : 'bg-secondary'} ms-1">${r.status}</span>
                        <c:out value="${r.comment}"/>
                        <br><small class="text-muted"><c:out value="${r.getDisplayName(true)}"/> &middot; ${r.reviewDate}</small>
                    </li>
                </c:forEach>
                <c:if test="${empty latestReviews}">
                    <li class="list-group-item text-muted">No reviews yet.</li>
                </c:if>
            </ul>
        </div>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
