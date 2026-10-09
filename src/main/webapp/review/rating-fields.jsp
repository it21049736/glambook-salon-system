<%-- Rating and comment fields shared by the submit and edit review pages.
     Values come from ${review} on the edit page, or from the submitted form after an error. --%>
<c:set var="f_rating" value="${not empty review ? review.rating : param.rating}"/>
<c:set var="f_comment" value="${not empty review ? review.comment : param.comment}"/>

<div class="mb-3">
    <label class="form-label d-block">Rating *</label>
    <c:forEach begin="1" end="5" var="i">
        <input type="radio" class="btn-check" name="rating" id="rating${i}" value="${i}" ${f_rating == i ? 'checked' : ''} required>
        <label class="btn btn-outline-glam btn-sm me-1" for="rating${i}">${i} <i class="bi bi-star-fill"></i></label>
    </c:forEach>
</div>
<div class="mb-3">
    <label class="form-label" for="comment">Your comment *</label>
    <textarea class="form-control" id="comment" name="comment" rows="4" maxlength="500" required><c:out value="${f_comment}"/></textarea>
    <div class="form-text">Maximum 500 characters.</div>
</div>
