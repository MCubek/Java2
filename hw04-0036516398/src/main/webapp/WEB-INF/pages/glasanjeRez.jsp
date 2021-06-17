<%--suppress HtmlDeprecatedAttribute --%>
<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 9.4.2021.
  Time: 15:19
  Voting results page.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="bgCol" scope="request" type="java.lang.String"/>
<jsp:useBean id="bands_votes" scope="request" type="java.util.Map"/>
<jsp:useBean id="winners" scope="request" type="java.util.List"/>
<!DOCTYPE html>
<html>
<style>
    body {
        background-color: ${bgCol};
    }

    table.rez td {
        text-align: center;
    }
</style>
<head>
    <title>Rezultati glasanja</title>
</head>
<body>
<h1>Rezultati glasanja</h1>
<p>Ovo su rezultati glasanja.</p>
<table border="1" cellspacing="0" class="rez">
    <thead>
    <tr>
        <th>Bend</th>
        <th>Broj glasova</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${bands_votes}" var="entry" varStatus="status">
        <tr>
            <td>
                    ${entry.key.name}
            </td>
            <td>
                    ${entry.value}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<h2>Grafički prikaz rezultata</h2>
<img alt="Pie-chart" src="<c:url value="/glasanje-grafika"/>" width="500" height="500"/>
<h2>Rezultati u XLS formatu</h2>
<p>Rezultati u XLS formatu dostupni su <a href="<c:url value="/glasanje-xls"/>">ovdje</a></p>
<h2>Razno</h2>
<p>Primjeri pjesama pobjedničkih bendova/pobjedničkog benda:</p>
<ul>
    <c:forEach items="${winners}" var="winner" varStatus="status">
        <li>
            <a href="${winner.url}" target="_blank">${winner.name}</a>
        </li>
    </c:forEach>
</ul>
<br>
<a href="<c:url value="/index.jsp"/>">Home</a>
</body>
</html>
