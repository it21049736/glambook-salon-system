<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Home" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<!-- hero section -->
<section class="hero mb-5">
    <div class="row align-items-center">
        <div class="col-lg-7">
            <p class="text-uppercase gold-text fw-semibold mb-2">Colombo's friendly beauty salon</p>
            <h1 class="mb-3">Look good, feel glamorous.</h1>
            <p class="lead mb-4">Book hair, skin and makeup appointments with our expert stylists.
                Choose your service, pick a free time slot and you are done.</p>
            <a href="${ctx}/appointments/book" class="btn btn-glam btn-lg me-2"><i class="bi bi-calendar-heart"></i> Book Appointment</a>
            <a href="${ctx}/services" class="btn btn-outline-glam btn-lg">View Services</a>
        </div>
        <div class="col-lg-5 text-center d-none d-lg-block">
            <i class="bi bi-flower1" style="font-size: 11rem; color: var(--glam-rose); opacity: .75;"></i>
        </div>
    </div>
</section>

<!-- service categories -->
<section class="mb-5">
    <h2 class="text-center mb-4">Our Services</h2>
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card glam-card h-100 text-center p-4">
                <div class="feature-icon mx-auto mb-3"><i class="bi bi-scissors"></i></div>
                <h4>Hair</h4>
                <p class="text-muted">Cuts, colouring, keratin and blow-dry styling for every hair length.</p>
                <a href="${ctx}/services?category=HAIR" class="btn btn-outline-glam mt-auto">See hair services</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card glam-card h-100 text-center p-4">
                <div class="feature-icon mx-auto mb-3"><i class="bi bi-droplet-half"></i></div>
                <h4>Skin</h4>
                <p class="text-muted">Facials, clean-ups and glow treatments using quality products.</p>
                <a href="${ctx}/services?category=SKIN" class="btn btn-outline-glam mt-auto">See skin services</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card glam-card h-100 text-center p-4">
                <div class="feature-icon mx-auto mb-3"><i class="bi bi-palette"></i></div>
                <h4>Makeup</h4>
                <p class="text-muted">Party, engagement and bridal makeup by our senior artists.</p>
                <a href="${ctx}/services?category=MAKEUP" class="btn btn-outline-glam mt-auto">See makeup services</a>
            </div>
        </div>
    </div>
</section>

<!-- why choose us -->
<section class="mb-4">
    <div class="card glam-card p-4">
        <div class="row text-center g-4">
            <div class="col-6 col-md-3">
                <i class="bi bi-calendar-check fs-2 gold-text"></i>
                <h6 class="mt-2">Easy online booking</h6>
            </div>
            <div class="col-6 col-md-3">
                <i class="bi bi-people fs-2 gold-text"></i>
                <h6 class="mt-2">Skilled stylists</h6>
            </div>
            <div class="col-6 col-md-3">
                <i class="bi bi-gem fs-2 gold-text"></i>
                <h6 class="mt-2">Premium member discounts</h6>
            </div>
            <div class="col-6 col-md-3">
                <i class="bi bi-star fs-2 gold-text"></i>
                <h6 class="mt-2">Real customer reviews</h6>
            </div>
        </div>
    </div>
</section>

<%@ include file="/includes/footer.jsp" %>
