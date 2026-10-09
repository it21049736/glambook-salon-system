<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Manage Users" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="d-flex flex-wrap justify-content-between align-items-center mb-3 gap-2">
    <h2 class="mb-0"><i class="bi bi-people"></i> Users</h2>
    <form class="d-flex gap-2" action="${ctx}/admin/users" method="get">
        <input type="text" class="form-control" name="q" value="<c:out value='${q}'/>" placeholder="Search name, email, phone or ID">
        <button class="btn btn-glam" type="submit"><i class="bi bi-search"></i></button>
        <c:if test="${not empty q}"><a class="btn btn-outline-secondary" href="${ctx}/admin/users">Clear</a></c:if>
    </form>
</div>

<div class="card glam-card">
    <div class="table-responsive">
        <table class="table table-hover table-glam align-middle mb-0">
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Membership / Position</th>
                <th class="text-end">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="u" items="${users}">
                <tr>
                    <td>${u.userId}</td>
                    <td><c:out value="${u.fullName}"/></td>
                    <td><c:out value="${u.email}"/></td>
                    <td>${u.phone}</td>
                    <td>
                        <span class="badge ${u.role == 'ADMIN' ? 'bg-dark' : 'bg-secondary'}">${u.role}</span>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${u.role == 'CUSTOMER'}">
                                <span class="badge ${u.premium ? 'btn-gold' : 'bg-light text-dark'}">${u.membership}</span>
                            </c:when>
                            <c:otherwise><c:out value="${u.position}"/></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-end">
                        <c:if test="${u.userId != sessionScope.loggedUser.userId}">
                            <form action="${ctx}/admin/users/delete" method="post" class="d-inline"
                                  data-confirm="Delete user ${u.userId}?">
                                <input type="hidden" name="userId" value="${u.userId}">
                                <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i> Delete</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty users}">
                <tr><td colspan="7" class="text-center text-muted py-4">No users found.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>
