<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
</div>

<%@ include file="/includes/footer.jsp" %>
