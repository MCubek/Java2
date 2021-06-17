<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 9.4.2021.
  Time: 0:15
  Trigonometric page.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="bgCol" scope="request" type="java.lang.String"/>
<jsp:useBean id="angles" scope="request" type="java.util.List"/>
<jsp:useBean id="sines" scope="request" type="java.util.List"/>
<jsp:useBean id="cosines" scope="request" type="java.util.List"/>
<!DOCTYPE html>
<html>
<style>
    body {
        background-color: ${bgCol};
    }
</style>
<head>
    <title>Trigonometry</title>
</head>
<body>
<h2>Table of trigonometric functions</h2>
<a href="<c:url value="/index.jsp"/>">Home</a>
<table>
    <thead>
    <tr>
        <td>Angle</td>
        <td>Sin</td>
        <td>Cos</td>
    </tr>
    </thead>

    <c:forEach var="i" begin="0" end="${angles.size()-1}" step="1" varStatus="status">
        <tr>
            <td>${angles.get(i)}</td>
            <td>${sines.get(i)}</td>
            <td>${cosines.get(i)}</td>
        </tr>
    </c:forEach>
</table>
<br>
</body>
</html>
