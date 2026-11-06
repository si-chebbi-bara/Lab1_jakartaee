<%--
  Created by IntelliJ IDEA.
  User: bara
  Date: 11/3/26
  Time: 2:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<jsp:include page="header.jsp" />

<h2>Your Cart</h2>

<c:forEach var="item" items="${cart}">
    <div class="product-card">
        <h3>${item.name}</h3>
        <p><fmt:formatNumber value="${item.price}" type="currency"/></p>
    </div>
</c:forEach>

<a href="${pageContext.request.contextPath}/checkout">Go to checkout</a>

<jsp:include page="footer.jsp" />