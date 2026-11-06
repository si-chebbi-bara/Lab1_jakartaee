<%--
  Created by IntelliJ IDEA.
  User: bara
  Date: 11/3/26
  Time: 1:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Catalog</title>
    <style>
      body { font-family: Arial, sans-serif; margin: 0; }
      nav { background: #222; padding: 12px 24px; }
      nav a { color: #fff; margin-right: 20px; text-decoration: none; }
      main { padding: 24px; }
      .product-card { border: 1px solid #ccc; padding: 12px; margin-bottom: 12px; width: 300px; }
    </style>
  </head>
  <body>
  <nav>
    <a href="${pageContext.request.contextPath}/catalog/products">Products</a>
    <a href="${pageContext.request.contextPath}/cart">Cart</a>
    <a href="${pageContext.request.contextPath}/checkout">Checkout</a>
  </nav>
  <main>