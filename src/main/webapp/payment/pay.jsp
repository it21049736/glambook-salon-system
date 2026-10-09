<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Payment" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="row g-4 justify-content-center">
    <!-- booking summary -->
    <div class="col-lg-4">
        <div class="card glam-card">
            <div class="card-header py-3">Booking summary</div>
            <div class="card-body">
                <p class="mb-1"><strong>${appointment.appointmentId}</strong></p>
                <p class="mb-1"><c:out value="${service.name}"/></p>
                <p class="mb-1 small">with <c:out value="${stylist.name}"/></p>
                <p class="small">${appointment.date} at ${appointment.timeSlot}</p>
                <hr>
                <div class="d-flex justify-content-between small">
                    <span>Price</span><span>LKR <fmt:formatNumber value="${appointment.originalPrice}" pattern="#,##0.00"/></span>
                </div>
                <div class="d-flex justify-content-between small text-success">
                    <span>Discount</span><span>- LKR <fmt:formatNumber value="${appointment.discount}" pattern="#,##0.00"/></span>
                </div>
                <div class="d-flex justify-content-between fw-semibold fs-5 mt-2">
                    <span>Total</span><span class="price-tag">LKR <fmt:formatNumber value="${appointment.finalPrice}" pattern="#,##0.00"/></span>
                </div>
            </div>
        </div>
    </div>

    <!-- payment form -->
    <div class="col-lg-6">
        <div class="card glam-card">
            <div class="card-header py-3"><i class="bi bi-credit-card"></i> Payment details</div>
            <div class="card-body p-4">
                <form action="${ctx}/payments/pay" method="post">
                    <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">

                    <div class="mb-3">
                        <input type="radio" class="btn-check" name="method" id="methodCard" value="CARD"
                            ${param.method != 'CASH' ? 'checked' : ''}>
                        <label class="btn btn-outline-glam me-2" for="methodCard"><i class="bi bi-credit-card"></i> Card</label>
                        <input type="radio" class="btn-check" name="method" id="methodCash" value="CASH"
                            ${param.method == 'CASH' ? 'checked' : ''}>
                        <label class="btn btn-outline-glam" for="methodCash"><i class="bi bi-cash-coin"></i> Cash</label>
                    </div>

                    <%-- card fields --%>
                    <div id="cardBox">
                        <div class="mb-3">
                            <label class="form-label" for="cardHolder">Name on card</label>
                            <input type="text" class="form-control" id="cardHolder" name="cardHolder" value="<c:out value='${param.cardHolder}'/>">
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="cardNumber">Card number</label>
                            <input type="text" class="form-control" id="cardNumber" name="cardNumber" maxlength="19"
                                   placeholder="1234 5678 9012 3456" inputmode="numeric" autocomplete="off">
                        </div>
                        <div class="row">
                            <div class="col-6 mb-3">
                                <label class="form-label" for="expiry">Expiry (MM/YY)</label>
                                <input type="text" class="form-control" id="expiry" name="expiry" maxlength="5" placeholder="08/28" value="<c:out value='${param.expiry}'/>">
                            </div>
                            <div class="col-6 mb-3">
                                <label class="form-label" for="cvv">CVV</label>
                                <input type="password" class="form-control" id="cvv" name="cvv" maxlength="3" autocomplete="off">
                            </div>
                        </div>
                        <p class="small text-muted"><i class="bi bi-shield-lock"></i> Only the last 4 digits of your card are stored.</p>
                    </div>

                    <%-- cash fields --%>
                    <div id="cashBox">
                        <div class="mb-3">
                            <label class="form-label" for="amountTendered">Cash received (LKR)</label>
                            <input type="number" step="0.01" min="0" class="form-control" id="amountTendered" name="amountTendered"
                                   value="<c:out value='${param.amountTendered}'/>">
                            <div class="form-text">Change is calculated automatically.</div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-glam w-100">
                        Pay LKR <fmt:formatNumber value="${appointment.finalPrice}" pattern="#,##0.00"/>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // show the card or the cash fields depending on the chosen method
    function showMethodBox() {
        const isCard = document.getElementById('methodCard').checked;
        document.getElementById('cardBox').style.display = isCard ? 'block' : 'none';
        document.getElementById('cashBox').style.display = isCard ? 'none' : 'block';
    }
    document.querySelectorAll('input[name="method"]').forEach(function (radio) {
        radio.addEventListener('change', showMethodBox);
    });
    showMethodBox();
</script>

<%@ include file="/includes/footer.jsp" %>
