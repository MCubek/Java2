<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 9.4.2021.
  Time: 10:48
  Report page.
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
    <title>OS Usage</title>
</head>
<body>
<h1>OS Usage</h1>
<p>Here are the results of OS usage in survey that we completed.</p>
<img alt="Chart" src="<c:url value="/reportImage"/>"/>
<br>
<a href="<c:url value="/index.jsp"/>">Home</a>
</body>
</html>
