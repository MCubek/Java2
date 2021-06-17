<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 2.6.2021.
  Time: 19:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="currentID" scope="request" type="java.lang.Long"/>
<jsp:useBean id="currentFN" scope="request" type="java.lang.String"/>
<jsp:useBean id="currentLN" scope="request" type="java.lang.String"/>
<jsp:useBean id="errorMessage" scope="request" type="java.lang.String"/>
<jsp:useBean id="form" scope="request" type="hr.fer.oprpp2.hw6.blog.model.form.BlogUserForm"/>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
</head>

<link rel="stylesheet" href="<c:url value="/styles.css"/>">

<body>
<div class="topnav">
    <a href="<c:url value="/servleti/main"/>">Home/Login</a>
    <c:choose>
        <c:when test="${currentID>=0}">
            <a href="<c:url value="/servleti/logout"/>">Logout</a>
            <a class="name">${currentFN}${' '}${currentLN}</a>
        </c:when>
        <c:otherwise>
            <a class="name">Not Logged In</a>
        </c:otherwise>
    </c:choose>
</div>
<div>
    <a>Register:</a>
    <form action="<c:url value="/servleti/register"/>" method="post">
        <label for="fname">First Name:</label>
        <input required type="text" id="fname" name="fname" value="<c:if test="${form.firstName!=null}">${form.firstName}</c:if>"><br><br>
        <label for="lname">Last Name:</label>
        <input required type="text" id="lname" name="lname" value="<c:if test="${form.lastName!=null}">${form.lastName}</c:if>"><br><br>
        <label for="email">Email:</label>
        <input required type="email" id="email" name="email" value="<c:if test="${form.email!=null}">${form.email}</c:if>"><br><br>
        <label for="nick">Nickname:</label>
        <input required type="text" id="nick" name="nick" value="<c:if test="${form.nick!=null}">${form.nick}</c:if>"><br><br>
        <label for="password">Password:</label>
        <input required type="password" id="password" name="password"><br><br>
        <input type="submit" value="Register">
    </form>
</div>
<c:if test="${!errorMessage.blank}">
    <br>
    <pre style="color: red">${errorMessage}</pre>
</c:if>
</body>
</html>