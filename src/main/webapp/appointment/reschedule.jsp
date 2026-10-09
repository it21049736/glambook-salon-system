<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Reschedule Appointment" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card" style="max-width: 640px;">
    <div class="card-header d-flex justify-content-between align-items-center py-3">
        <h3 class="mb-0"><i class="bi bi-arrow-repeat"></i> Reschedule</h3>
        <span class="badge bg-secondary">${appointment.appointmentId}</span>
    </div>
    <div class="card-body p-4">
        <div class="p-3 rounded mb-4" style="background: var(--glam-pink);">
            <div><strong>Service:</strong> <c:out value="${service.name}"/></div>
            <div><strong>Stylist:</strong> <c:out value="${stylist.name}"/>
                <small class="text-muted">(works ${stylist.workingDays}, ${stylist.shiftStart} - ${stylist.shiftEnd})</small></div>
            <div><strong>Current booking:</strong> ${appointment.date} at ${appointment.timeSlot}</div>
        </div>

        <form action="${ctx}/appointments/reschedule" method="post">
            <input type="hidden" name="id" value="${appointment.appointmentId}">
            <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
            <div class="mb-3">
                <label class="form-label" for="date">New date *</label>
                <div class="input-group">
                    <input type="date" class="form-control no-past" id="date" name="date" value="${date}" required>
                    <button type="submit" class="btn btn-outline-glam" formmethod="get" formnovalidate>
                        <i class="bi bi-search"></i> Check slots
                    </button>
                </div>
            </div>

            <c:if test="${slotsChecked}">
                <div class="mb-3">
                    <label class="form-label d-block">New time slot *</label>
                    <c:choose>
                        <c:when test="${empty availableSlots}">
                            <div class="alert alert-warning mb-0">No free slots on this day (the stylist may be off or fully booked).</div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="slot" items="${availableSlots}">
                                <input type="radio" class="btn-check" name="timeSlot" id="slot_${slot}" value="${slot}" required>
                                <label class="btn btn-outline-glam mb-2 me-1" for="slot_${slot}">${slot}</label>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-glam flex-fill" ${empty availableSlots ? 'disabled' : ''}>Save new time</button>
                <a href="${ctx}/appointments/my" class="btn btn-outline-secondary">Back</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
