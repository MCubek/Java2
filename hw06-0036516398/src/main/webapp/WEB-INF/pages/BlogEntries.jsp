<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 2.6.2021.
  Time: 22:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="currentID" scope="request" type="java.lang.Long"/>
<jsp:useBean id="currentFN" scope="request" type="java.lang.String"/>
<jsp:useBean id="currentLN" scope="request" type="java.lang.String"/>
<jsp:useBean id="authorId" scope="request" type="java.lang.Long"/>
<jsp:useBean id="authorNick" scope="request" type="java.lang.String"/>
<jsp:useBean id="blogEntries" scope="request" type="java.util.Collection"/>
<!DOCTYPE html>
<html>
<head>
    <title>${authorNick}'s blogs</title>
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
    <h1>${authorNick}'s blogs</h1>
    <ol>
        <c:forEach items="${blogEntries}" var="entry">
            <li>
                <a href="<c:url value="/servleti/author/${authorNick}${'/'}${entry.id}"/>">
                        ${entry.title}
                </a>
            </li>
        </c:forEach>
    </ol>
</div>
<c:if test="${currentID.equals(authorId)}">
    <a href="<c:url value="/servleti/author/${authorNick}/new"/>">
        New Blog Entry
    </a>
</c:if>
</body>
</html>
