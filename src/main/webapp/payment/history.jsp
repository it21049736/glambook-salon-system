<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Payment History" scope="request"/>
<%@ include file="/includes/header.jsp" %>
<c:set var="isAdmin" value="${sessionScope.loggedUser.role == 'ADMIN'}"/>

<h2 class="mb-3"><i class="bi bi-receipt"></i> Payment History</h2>

<div class="card glam-card">
    <div class="table-responsive">
        <table class="table table-hover table-glam align-middle mb-0">
            <thead>
            <tr>
                <th>Invoice</th>
                <c:if test="${isAdmin}"><th>Customer</th></c:if>
                <th>Appointment</th>
                <th>Date</th>
                <th>Method</th>
                <th>Amount (LKR)</th>
                <th>Status</th>
                <th class="text-end">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${payments}">
                <tr>
                    <td><a href="${ctx}/payments/invoice?id=${p.paymentId}">${p.paymentId}</a></td>
                    <c:if test="${isAdmin}">
                        <td><c:out value="${empty userNames[p.customerId] ? p.customerId : userNames[p.customerId]}"/></td>
                    </c:if>
                    <td>${p.appointmentId}</td>
                    <td>${p.paymentDate}</td>
                    <td><i class="bi ${p.method == 'CARD' ? 'bi-credit-card' : 'bi-cash-coin'}"></i> ${p.method}</td>
                    <td><fmt:formatNumber value="${p.amount}" pattern="#,##0.00"/></td>
                    <td>
                        <span class="badge ${p.status == 'PAID' ? 'bg-success' : (p.status == 'VOIDED' ? 'bg-danger' : 'bg-warning text-dark')}">${p.status}</span>
                    </td>
                    <td class="text-end text-nowrap">
                        <a class="btn btn-sm btn-outline-glam" href="${ctx}/payments/invoice?id=${p.paymentId}"><i class="bi bi-eye"></i></a>
                        <c:if test="${isAdmin}">
                            <%-- admin: change the status --%>
                            <form action="${ctx}/admin/payments/status" method="post" class="d-inline-flex gap-1">
                                <input type="hidden" name="paymentId" value="${p.paymentId}">
                                <select name="status" class="form-select form-select-sm" style="width: auto;">
                                    <c:forEach var="s" items="PENDING,PAID,VOIDED">
                                        <option value="${s}" ${p.status == s ? 'selected' : ''}>${s}</option>
                                    </c:forEach>
                                </select>
                                <button class="btn btn-sm btn-outline-secondary">Update</button>
                            </form>
                            <%-- admin: delete is only offered for voided records --%>
                            <c:if test="${p.voided}">
                                <form action="${ctx}/admin/payments/delete" method="post" class="d-inline"
                                      data-confirm="Delete voided payment ${p.paymentId} permanently?">
                                    <input type="hidden" name="paymentId" value="${p.paymentId}">
                                    <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
                                </form>
                            </c:if>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty payments}">
                <tr><td colspan="8" class="text-center text-muted py-4">No payments yet.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
