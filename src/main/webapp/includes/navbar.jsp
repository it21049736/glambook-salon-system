<%-- Shared navigation bar. Links change depending on who is logged in. --%>
<nav class="navbar navbar-expand-lg glam-navbar sticky-top">
    <div class="container">
        <a class="navbar-brand" href="${ctx}/"><i class="bi bi-scissors"></i> GlamBook</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${ctx}/">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="${ctx}/services">Services</a></li>
                <li class="nav-item"><a class="nav-link" href="${ctx}/stylists">Stylists</a></li>
                <li class="nav-item"><a class="nav-link" href="${ctx}/reviews">Reviews</a></li>

                <%-- pages for any logged in user --%>
                <c:if test="${not empty sessionScope.loggedUser}">
                    <li class="nav-item"><a class="nav-link" href="${ctx}/appointments/book">Book Now</a></li>
                    <li class="nav-item"><a class="nav-link" href="${ctx}/appointments/my">Appointments</a></li>
                    <li class="nav-item"><a class="nav-link" href="${ctx}/payments/history">Payments</a></li>
                </c:if>

                <%-- admin only menu --%>
                <c:if test="${sessionScope.loggedUser.role == 'ADMIN'}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">Admin</a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="${ctx}/admin/dashboard">Dashboard</a></li>
                            <li><a class="dropdown-item" href="${ctx}/admin/users">Users</a></li>
                            <li><a class="dropdown-item" href="${ctx}/admin/services/add">Add Service</a></li>
                            <li><a class="dropdown-item" href="${ctx}/admin/stylists/add">Add Stylist</a></li>
                            <li><a class="dropdown-item" href="${ctx}/admin/reviews">Review Moderation</a></li>
                        </ul>
                    </li>
                </c:if>
            </ul>

            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedUser}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle"></i> <c:out value="${sessionScope.loggedUser.fullName}"/>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="${ctx}/profile">My Profile</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${ctx}/logout">Logout</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/login">Login</a></li>
                        <li class="nav-item ms-lg-2"><a class="btn btn-glam" href="${ctx}/register">Register</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
