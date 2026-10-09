<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Invoice ${payment.paymentId}" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card mx-auto" style="max-width: 720px;">
    <div class="card-body p-4 p-md-5">
        <div class="d-flex justify-content-between align-items-start mb-4">
            <div>
                <h3 class="mb-0" style="color: var(--glam-rose);"><i class="bi bi-scissors gold-text"></i> GlamBook Salon</h3>
                <small class="text-muted">45 Galle Road, Colombo 03 &middot; 011 234 5678</small>
            </div>
            <div class="text-end">
                <h5 class="mb-0">INVOICE</h5>
                <div>${payment.paymentId}</div>
                <small class="text-muted">${payment.paymentDate}</small>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-6">
                <h6 class="text-muted mb-1">Billed to</h6>
                <div><c:out value="${customer.fullName}"/></div>
                <small class="text-muted"><c:out value="${customer.email}"/><br>${customer.phone}</small>
            </div>
            <div class="col-6 text-end">
                <h6 class="text-muted mb-1">Appointment</h6>
                <div>${payment.appointmentId}</div>
                <small class="text-muted">${appointment.date} at ${appointment.timeSlot}</small>
            </div>
        </div>

        <table class="table">
            <thead class="table-light">
            <tr><th>Description</th><th class="text-end">Amount (LKR)</th></tr>
            </thead>
            <tbody>
            <tr>
                <td><c:out value="${empty service ? 'Salon service' : service.name}"/>
                    <c:if test="${not empty stylist}"><br><small class="text-muted">Stylist: <c:out value="${stylist.name}"/></small></c:if>
                </td>
                <td class="text-end"><fmt:formatNumber value="${appointment.originalPrice}" pattern="#,##0.00"/></td>
            </tr>
            <c:if test="${appointment.discount > 0}">
                <tr class="text-success">
                    <td>Premium member discount</td>
                    <td class="text-end">- <fmt:formatNumber value="${appointment.discount}" pattern="#,##0.00"/></td>
                </tr>
            </c:if>
            <tr class="fw-semibold">
                <td>Total</td>
                <td class="text-end"><fmt:formatNumber value="${payment.amount}" pattern="#,##0.00"/></td>
            </tr>
            </tbody>
        </table>

        <%-- getDetails() is overridden in CashPayment and CardPayment --%>
        <p class="mb-1"><strong>Payment method:</strong> <c:out value="${payment.details}"/></p>
        <p><strong>Status:</strong>
            <span class="badge ${payment.status == 'PAID' ? 'bg-success' : (payment.status == 'VOIDED' ? 'bg-danger' : 'bg-warning text-dark')}">${payment.status}</span>
        </p>

        <%-- stylist commission is only for salon staff; it depends on Senior/Junior (polymorphism) --%>
        <c:if test="${sessionScope.loggedUser.role == 'ADMIN' and not empty stylist}">
            <p class="small text-muted">Stylist commission (${stylist.level}):
                LKR <fmt:formatNumber value="${stylist.calculateCommission(payment.amount)}" pattern="#,##0.00"/></p>
        </c:if>

        <p class="text-center text-muted small mt-4 mb-0">Thank you for choosing GlamBook. See you again soon!</p>
    </div>
</div>

<div class="text-center mt-3 d-print-none">
    <button class="btn btn-outline-glam" onclick="window.print()"><i class="bi bi-printer"></i> Print</button>
    <a class="btn btn-glam" href="${ctx}/payments/history">Payment history</a>
</div>

<%@ include file="/includes/footer.jsp" %>
