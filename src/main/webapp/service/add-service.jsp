<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Add Service" scope="request"/>
<%@ include file="/includes/header.jsp" %>

<div class="card glam-card form-card">
    <div class="card-header py-3">
        <h3 class="mb-0"><i class="bi bi-plus-circle"></i> Add a new service</h3>
    </div>
    <div class="card-body p-4">
        <form action="${ctx}/admin/services/add" method="post">
            <div class="mb-3">
                <label class="form-label" for="category">Category *</label>
                <select class="form-select" id="category" name="category" required>
                    <option value="HAIR" ${category == 'HAIR' ? 'selected' : ''}>Hair</option>
                    <option value="SKIN" ${category == 'SKIN' ? 'selected' : ''}>Skin</option>
                    <option value="MAKEUP" ${category == 'MAKEUP' ? 'selected' : ''}>Makeup</option>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label" for="name">Service name *</label>
                <input type="text" class="form-control" id="name" name="name" value="<c:out value='${name}'/>" required>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label" for="basePrice">Base price (LKR) *</label>
                    <input type="number" step="0.01" min="1" class="form-control" id="basePrice" name="basePrice" value="<c:out value='${basePrice}'/>" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label" for="duration">Duration (minutes) *</label>
                    <input type="number" min="1" max="480" class="form-control" id="duration" name="duration" value="<c:out value='${duration}'/>" required>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label" for="description">Description</label>
                <textarea class="form-control" id="description" name="description" rows="2"><c:out value="${description}"/></textarea>
            </div>

            <%-- only the box for the selected category is shown (see script below) --%>
            <div class="mb-4 extra-box" data-category="HAIR">
                <label class="form-label">Hair length <small class="text-muted">(medium +LKR 500, long +LKR 1000)</small></label>
                <select class="form-select" name="extra_HAIR">
                    <option value="SHORT">Short</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="LONG">Long</option>
                </select>
            </div>
            <div class="mb-4 extra-box" data-category="SKIN">
                <label class="form-label">Product brand <small class="text-muted">(10% product charge is added)</small></label>
                <input type="text" class="form-control" name="extra_SKIN" placeholder="e.g. Dermalogica">
            </div>
            <div class="mb-4 extra-box" data-category="MAKEUP">
                <label class="form-label">Occasion <small class="text-muted">(party +20%, bridal +50%)</small></label>
                <select class="form-select" name="extra_MAKEUP">
                    <option value="CASUAL">Casual</option>
                    <option value="PARTY">Party</option>
                    <option value="BRIDAL">Bridal</option>
                </select>
            </div>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-glam flex-fill">Save service</button>
                <a href="${ctx}/services" class="btn btn-outline-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<script>
    // show only the extra field that belongs to the chosen category
    function showExtraBox() {
        const category = document.getElementById('category').value;
        document.querySelectorAll('.extra-box').forEach(function (box) {
            box.style.display = box.dataset.category === category ? 'block' : 'none';
        });
    }
    document.getElementById('category').addEventListener('change', showExtraBox);
    showExtraBox();
</script>

<%@ include file="/includes/footer.jsp" %>
