<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Appointments" scope="request"/>
<%@ include file="/includes/header.jsp" %>
<c:set var="isAdmin" value="${sessionScope.loggedUser.role == 'ADMIN'}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center mb-3 gap-2">
    <h2 class="mb-0"><i class="bi bi-calendar-check"></i> ${isAdmin ? 'All Appointments' : 'My Appointments'}</h2>
    <div class="d-flex gap-2">
        <form action="${ctx}/appointments/my" method="get">
            <select class="form-select" name="status" onchange="this.form.submit()">
                <option value="">All statuses</option>
                <c:forEach var="st" items="BOOKED,RESCHEDULED,COMPLETED,CANCELLED">
                    <option value="${st}" ${status == st ? 'selected' : ''}>${st}</option>
                </c:forEach>
            </select>
        </form>
        <c:if test="${not isAdmin}">
            <a href="${ctx}/appointments/book" class="btn btn-glam text-nowrap"><i class="bi bi-plus-lg"></i> New booking</a>
        </c:if>
    </div>
</div>

<div class="card glam-card">
    <div class="table-responsive">
        <table class="table table-hover table-glam align-middle mb-0">
            <thead>
            <tr>
                <th>ID</th>
                <c:if test="${isAdmin}"><th>Customer</th></c:if>
                <th>Service</th>
                <th>Stylist</th>
                <th>Date</th>
                <th>Time</th>
                <th>Price (LKR)</th>
                <th>Status</th>
                <th class="text-end">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="a" items="${appointments}">
                <tr>
                    <td>${a.appointmentId}</td>
                    <c:if test="${isAdmin}">
                        <td><c:out value="${empty userNames[a.customerId] ? a.customerId : userNames[a.customerId]}"/></td>
                    </c:if>
                    <td><c:out value="${empty serviceNames[a.serviceId] ? a.serviceId : serviceNames[a.serviceId]}"/></td>
                    <td><c:out value="${empty stylistNames[a.stylistId] ? a.stylistId : stylistNames[a.stylistId]}"/></td>
                    <td>${a.date}</td>
                    <td>${a.timeSlot}</td>
                    <td>
                        <fmt:formatNumber value="${a.finalPrice}" pattern="#,##0.00"/>
                        <c:if test="${a.discount > 0}">
                            <br><small class="text-success">saved <fmt:formatNumber value="${a.discount}" pattern="#,##0"/></small>
                        </c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${a.status == 'COMPLETED'}"><span class="badge bg-success">COMPLETED</span></c:when>
                            <c:when test="${a.status == 'CANCELLED'}"><span class="badge bg-danger">CANCELLED</span></c:when>
                            <c:when test="${a.status == 'RESCHEDULED'}"><span class="badge bg-warning text-dark">RESCHEDULED</span></c:when>
                            <c:otherwise><span class="badge bg-primary">BOOKED</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-end text-nowrap">
                        <c:if test="${a.changeable}">
                            <a class="btn btn-sm btn-outline-secondary" href="${ctx}/appointments/reschedule?id=${a.appointmentId}">
                                <i class="bi bi-arrow-repeat"></i> Reschedule</a>
                            <form action="${ctx}/appointments/cancel" method="post" class="d-inline"
                                  data-confirm="Cancel appointment ${a.appointmentId}?">
                                <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                <button class="btn btn-sm btn-outline-danger"><i class="bi bi-x-circle"></i> Cancel</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty appointments}">
                <tr><td colspan="9" class="text-center text-muted py-4">No appointments found.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
