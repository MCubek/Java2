<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 9.4.2021.
  Time: 15:19
  Voting page.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="bgCol" scope="request" type="java.lang.String"/>
<jsp:useBean id="bands" scope="request" type="java.util.Collection"/>
<!DOCTYPE html>
<html>
<style>
    body {
        background-color: ${bgCol};
    }
</style>
<head>
    <title>Glasanje</title>
</head>
<body>
<h1>Glasanje za omiljeni bend:</h1>
<p>Od sljedećih bendova, koji Van je bend najdraži? Kliknite na link kako biste glasali</p>
<ol>
    <c:forEach items="${bands}" var="band" varStatus="status">
        <li><a href="<c:url value="/glasanje-glasaj">
                    <c:param name="id" value="${band.id}"/>
                    </c:url>">
                ${band.name}</a>
        </li>
    </c:forEach>
</ol>
</body>
</html>
