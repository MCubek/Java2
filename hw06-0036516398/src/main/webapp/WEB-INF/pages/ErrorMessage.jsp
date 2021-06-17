<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="MESSAGE" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
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
<h1>Error has occurred!</h1>
<p><c:out value="${MESSAGE}"/></p>
</body>
</html>