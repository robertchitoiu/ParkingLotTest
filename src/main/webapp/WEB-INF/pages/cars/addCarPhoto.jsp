<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:pageTemplate pageTitle="Add car photo">
    <h1>Add Car Photo</h1>
    <form class="needs-validation" novalidate method="POST" action="${pageContext.request.contextPath}/AddCarPhoto" enctype="multipart/form-data">
        <div class="row">
            <div class="col-md-6 mb-3">
                License Plate: ${car.licensePlate}
            </div>
        </div>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="file">Photo</label>
                <input type="file"   name="file" id="file" required>
                <div class="invalid-feedback">Photo is required.</div>
            </div>
        </div>
        <input type="hidden" name="car_id" value="${car.id}">
        <input type="submit" class="btn btn-primary btn-lg" value="Save">
    </form>
</t:pageTemplate>