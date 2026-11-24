<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<jsp:include page="header.jsp" />

<h2>${product.name}</h2>
<p><fmt:formatNumber value="${product.price}" type="currency"/></p>

<form method="post" action="${pageContext.request.contextPath}/cart/add">
    <input type="hidden" name="productId" value="${product.id}">
    <button type="submit">Add to cart</button>
</form>

<h3>Reviews</h3>

<c:if test="${empty reviews}">
    <p>No reviews yet.</p>
</c:if>

<c:forEach var="review" items="${reviews}">
    <div class="review">
        <strong>${review.rating} / 5</strong>
        <p><c:out value="${review.comment}"/></p>
    </div>
</c:forEach>

<jsp:include page="footer.jsp" />