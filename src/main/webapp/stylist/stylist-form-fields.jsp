<%-- Form fields shared by the add and edit stylist pages.
     On the edit page the values come from ${stylist}; on the add page they come from
     the submitted form (${param...}) so nothing is lost after a validation error. --%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="f_name" value="${not empty stylist ? stylist.name : param.name}"/>
<c:set var="f_phone" value="${not empty stylist ? stylist.phone : param.phone}"/>
<c:set var="f_email" value="${not empty stylist ? stylist.email : param.email}"/>
<c:set var="f_level" value="${not empty stylist ? stylist.level : param.level}"/>
<c:set var="f_specialty" value="${not empty stylist ? stylist.specialty : param.specialty}"/>
<c:set var="f_start" value="${not empty stylist ? stylist.shiftStart : (empty param.shiftStart ? '09:00' : param.shiftStart)}"/>
<c:set var="f_end" value="${not empty stylist ? stylist.shiftEnd : (empty param.shiftEnd ? '17:00' : param.shiftEnd)}"/>
<c:set var="f_exp" value="${not empty stylist ? stylist.experienceYears : param.experienceYears}"/>
<c:set var="f_days" value="${not empty stylist ? stylist.workingDays : (empty paramValues.workingDays ? '' : fn:join(paramValues.workingDays, ','))}"/>

<div class="mb-3">
    <label class="form-label" for="name">Full name *</label>
    <input type="text" class="form-control" id="name" name="name" value="<c:out value='${f_name}'/>" required>
</div>
<div class="row">
    <div class="col-md-6 mb-3">
        <label class="form-label" for="phone">Phone *</label>
        <input type="text" class="form-control" id="phone" name="phone" maxlength="10" value="<c:out value='${f_phone}'/>" required>
    </div>
    <div class="col-md-6 mb-3">
        <label class="form-label" for="email">Email *</label>
        <input type="email" class="form-control" id="email" name="email" value="<c:out value='${f_email}'/>" required>
    </div>
</div>
<div class="row">
    <div class="col-md-4 mb-3">
        <label class="form-label" for="level">Level *</label>
        <select class="form-select" id="level" name="level">
            <option value="JUNIOR" ${f_level == 'JUNIOR' ? 'selected' : ''}>Junior (10% commission)</option>
            <option value="SENIOR" ${f_level == 'SENIOR' ? 'selected' : ''}>Senior (20% commission)</option>
        </select>
    </div>
    <div class="col-md-4 mb-3">
        <label class="form-label" for="specialty">Specialty *</label>
        <select class="form-select" id="specialty" name="specialty">
            <option value="HAIR" ${f_specialty == 'HAIR' ? 'selected' : ''}>Hair</option>
            <option value="SKIN" ${f_specialty == 'SKIN' ? 'selected' : ''}>Skin</option>
            <option value="MAKEUP" ${f_specialty == 'MAKEUP' ? 'selected' : ''}>Makeup</option>
        </select>
    </div>
    <div class="col-md-4 mb-3">
        <label class="form-label" for="experienceYears">Experience (years) *</label>
        <input type="number" min="0" max="50" class="form-control" id="experienceYears" name="experienceYears" value="<c:out value='${f_exp}'/>" required>
    </div>
</div>

<h6 class="mt-2">Schedule</h6>
<div class="mb-3">
    <label class="form-label d-block">Working days *</label>
    <c:forEach var="day" items="MON,TUE,WED,THU,FRI,SAT,SUN">
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="checkbox" name="workingDays" value="${day}" id="day_${day}"
                ${fn:contains(f_days, day) ? 'checked' : ''}>
            <label class="form-check-label" for="day_${day}">${day}</label>
        </div>
    </c:forEach>
</div>
<div class="row mb-4">
    <div class="col-6">
        <label class="form-label" for="shiftStart">Shift starts</label>
        <select class="form-select" id="shiftStart" name="shiftStart">
            <c:forEach var="h" begin="7" end="21">
                <c:set var="time" value="${h lt 10 ? '0' : ''}${h}:00"/>
                <option value="${time}" ${time == f_start ? 'selected' : ''}>${time}</option>
            </c:forEach>
        </select>
    </div>
    <div class="col-6">
        <label class="form-label" for="shiftEnd">Shift ends</label>
        <select class="form-select" id="shiftEnd" name="shiftEnd">
            <c:forEach var="h" begin="7" end="21">
                <c:set var="time" value="${h lt 10 ? '0' : ''}${h}:00"/>
                <option value="${time}" ${time == f_end ? 'selected' : ''}>${time}</option>
            </c:forEach>
        </select>
    </div>
</div>
