<%--
  Created by IntelliJ IDEA.
  User: bara
  Date: 11/3/26
  Time: 2:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="header.jsp" />

<h2>Checkout</h2>

<c:if test="${not empty confirmationName}">
    <p>Thank you, ${confirmationName}! Your order has been placed.</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/checkout">
    <p>Name: <input type="text" name="name"></p>
    <p>Email: <input type="text" name="email"></p>
    <p>Address: <input type="text" name="address"></p>
    <button type="submit">Place order</button>
</form>

<jsp:include page="footer.jsp" />