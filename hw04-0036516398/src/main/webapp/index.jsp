<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 8.4.2021.
  Time: 22:09
  Index page
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
    <title>Home</title>
</head>
<body>
<h1>Home</h1>
<a href="<c:url value="/colors.jsp"/>">Background color chooser</a>
<br>
<a href="<c:url value="/trigonometric"><c:param name="a" value="0"/><c:param name="b"
                                                                            value="90"/></c:url>">
    Predefined trigonometry</a>
<br>
<form action="trigonometric" method="GET">
    Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
    Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
    <input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
</form>
<a href="<c:url value="/stories/funny.jsp"/>">Funny Story</a>
<br>
<a href="<c:url value="/report.jsp"/>">Report</a>
<br>
<a href="<c:url value="/powers">
    <c:param name="a" value="1"/>
    <c:param name="b" value="100"/>
    <c:param name="n" value="3"/>
</c:url>">
    Powers XML </a>
<br>
<a href="<c:url value="/appinfo.jsp"/>">AppInfo</a>
<br>
<a href="<c:url value="/glasanje"/>">Voting</a>
<br>
<a href="<c:url value="/glasanje-rezultati"/>">Voting Results</a>
</body>
</html>
