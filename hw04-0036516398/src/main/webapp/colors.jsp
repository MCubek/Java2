<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 8.4.2021.
  Time: 22:44
  Color picker page.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="bgCol" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<style>
    body {
        background-color: ${bgCol};
    }
</style>
<head>
    <title>Colors</title>
</head>
<body>
<h2>Background color picker</h2>
<a href="<c:url value="/setcolor/FFFFFF"/>">White</a>
<br>
<a href="<c:url value="/setcolor/FF0000"/>">Red</a>
<br>
<a href="<c:url value="/setcolor/00FF00"/>">Green</a>
<br>
<a href="<c:url value="/setcolor/00FFFF"/>">Cyan</a>
<br>
<br>
<a href="<c:url value="/index.jsp"/>">Home</a>
</body>
</html>
