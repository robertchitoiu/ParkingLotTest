<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:pageTemplate pageTitle="Countries">
    <h1>Countries</h1>
    <form method="POST" action="${pageContext.request.contextPath}/Countries">
        <c:if test="${pageContext.request.isUserInRole('WRITE_COUNTRIES')}">
            <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/AddCountry">Add Country</a>
            <button class="btn btn-danger" type="submit">Delete Country</button>
        </c:if>
        <div class="container text-center">
            <c:forEach var="country"  items="${countries}">
                <div class="row">
                    <div class="col">
                        <input type="checkbox"   name="car_ids" value="${country.id}">
                    </div>
                    <div class="col">
                            ${car.name}
                    </div>
                </div>
            </c:forEach>
        </div>
    </form>
</t:pageTemplate>