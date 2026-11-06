<%--
  Created by IntelliJ IDEA.
  User: bara
  Date: 11/3/26
  Time: 2:38 PM
  To change this template use File | Settings | File Templates.
--%>
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

<jsp:include page="footer.jsp" />