<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 3.6.2021.
  Time: 10:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="currentID" scope="request" type="java.lang.Long"/>
<jsp:useBean id="currentFN" scope="request" type="java.lang.String"/>
<jsp:useBean id="currentLN" scope="request" type="java.lang.String"/>
<jsp:useBean id="currentNick" scope="request" type="java.lang.String"/>
<jsp:useBean id="errorMessage" scope="request" type="java.lang.String"/>
<jsp:useBean id="entryForm" scope="request" type="hr.fer.oprpp2.hw6.blog.model.form.BlogEntryForm"/>
<!DOCTYPE html>
<html>
<head>
    <title>Edit ${entryForm.title}</title>
</head>

<link rel="stylesheet" href="<c:url value="/styles.css"/>">

<body>
<div class="topnav">
    <a href="<c:url value="/servleti/main"/>">Home/Login</a>
    <a href="<c:url value="/servleti/author/${currentNick}"/>">${currentNick}'s blogs</a>
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
    <h2>Edit ${entryForm.title}:</h2>
    <form action="<c:url value="/servleti/author/${currentNick}/edit"/>" method="post">
        <label for="title">Title:</label>
        <input required type="text" id="title" name="entryTitle" value="<c:if test="${entryForm.title!=null}">${entryForm.title}</c:if>"><br><br>
        <label for="text">Text:</label>
        <textarea rows="5" cols="60" id="text" required name="entryText"><c:if test="${entryForm.text!=null}">${entryForm.text}</c:if></textarea><br><br>
        <input required type="hidden" id="entryId" name="entryId" value="${entryForm.id}">
        <input type="submit" value="Edit Entry">
    </form>
    <br>
    <a href="<c:url value="/servleti/author/${currentNick}/${entryForm.id}"/>">Go Back</a>
    <c:if test="${!errorMessage.blank}">
        <br>
        <pre style="color: red">${errorMessage}</pre>
    </c:if>
</div>
</body>
</html>
