<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Book Appointment" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card" style="max-width: 720px;">
    <div class="card-header py-3">
        <h3 class="mb-0"><i class="bi bi-calendar-heart"></i> Book an appointment</h3>
    </div>
    <div class="card-body p-4">
        <%-- one form, two buttons: "Check slots" sends a GET, "Confirm booking" sends a POST --%>
        <form action="${ctx}/appointments/book" method="post">
            <div class="mb-3">
                <label class="form-label" for="serviceId">1. Service *</label>
                <select class="form-select" id="serviceId" name="serviceId" required>
                    <option value="">-- choose a service --</option>
                    <c:forEach var="s" items="${services}">
                        <option value="${s.serviceId}" data-category="${s.category}" ${s.serviceId == serviceId ? 'selected' : ''}>
                            <c:out value="${s.name}"/> (${s.category}) - LKR <fmt:formatNumber value="${s.calculatePrice()}" pattern="#,##0"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label" for="stylistId">2. Stylist *</label>
                <select class="form-select" id="stylistId" name="stylistId" required>
                    <option value="">-- choose a stylist --</option>
                    <c:forEach var="st" items="${stylists}">
                        <option value="${st.stylistId}" data-specialty="${st.specialty}" ${st.stylistId == stylistId ? 'selected' : ''}>
                            <c:out value="${st.name}"/> (${st.level}, ${st.specialty}) - ${st.workingDays}
                        </option>
                    </c:forEach>
                </select>
                <div class="form-text">Only stylists who do the chosen type of service are listed.</div>
            </div>
            <div class="mb-3">
                <label class="form-label" for="date">3. Date *</label>
                <div class="input-group">
                    <input type="date" class="form-control no-past" id="date" name="date" value="${date}" required>
                    <button type="submit" class="btn btn-outline-glam" formmethod="get" formnovalidate>
                        <i class="bi bi-search"></i> Check available slots
                    </button>
                </div>
            </div>

            <%-- available time slots (shown after "Check available slots") --%>
            <c:if test="${slotsChecked}">
                <div class="mb-3">
                    <label class="form-label d-block">4. Time slot *</label>
                    <c:choose>
                        <c:when test="${not empty slotMessage}">
                            <div class="alert alert-warning mb-0"><c:out value="${slotMessage}"/></div>
                        </c:when>
                        <c:when test="${empty availableSlots}">
                            <div class="alert alert-warning mb-0">No free slots on this day. Please try another date or stylist.</div>
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

            <%-- price summary with the premium discount --%>
            <c:if test="${not empty selectedService}">
                <div class="p-3 rounded mb-3" style="background: var(--glam-pink);">
                    <div class="d-flex justify-content-between">
                        <span>Service price</span>
                        <span>LKR <fmt:formatNumber value="${selectedService.calculatePrice()}" pattern="#,##0.00"/></span>
                    </div>
                    <c:if test="${sessionScope.loggedUser.premium}">
                        <div class="d-flex justify-content-between text-success">
                            <span>Premium discount (10%)</span>
                            <span>- LKR <fmt:formatNumber value="${selectedService.calculatePrice() * 0.10}" pattern="#,##0.00"/></span>
                        </div>
                    </c:if>
                    <hr class="my-2">
                    <div class="d-flex justify-content-between fw-semibold">
                        <span>You pay</span>
                        <span class="price-tag">LKR <fmt:formatNumber
                                value="${sessionScope.loggedUser.premium ? selectedService.calculatePrice() * 0.90 : selectedService.calculatePrice()}"
                                pattern="#,##0.00"/></span>
                    </div>
                </div>
            </c:if>

            <button type="submit" class="btn btn-glam w-100" ${empty availableSlots ? 'disabled' : ''}>
                <i class="bi bi-check2-circle"></i> Confirm booking
            </button>
        </form>
    </div>
</div>

<script>
    // show only stylists whose specialty matches the chosen service category
    function filterStylists() {
        const selected = document.querySelector('#serviceId option:checked');
        const category = selected ? selected.dataset.category : '';
        const stylistSelect = document.getElementById('stylistId');
        stylistSelect.querySelectorAll('option[data-specialty]').forEach(function (option) {
            const match = !category || option.dataset.specialty === category;
            option.hidden = !match;
            if (!match && option.selected) {
                stylistSelect.value = '';
            }
        });
    }
    document.getElementById('serviceId').addEventListener('change', filterStylists);
    filterStylists();
</script>

<%@ include file="/includes/footer.jsp" %>
