<%--suppress HtmlDeprecatedAttribute --%>
<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 9.4.2021.
  Time: 15:19
  Voting results page.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="candidates" scope="request" type="java.util.List"/>
<jsp:useBean id="winners" scope="request" type="java.util.List"/>
<jsp:useBean id="pollId" scope="request" type="java.lang.Long"/>
<!DOCTYPE html>
<html>
<style>
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
        <th>Kandidat</th>
        <th>Broj glasova</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${candidates}" var="entry" varStatus="status">
        <tr>
            <td>
                    ${entry.optionTitle}
            </td>
            <td>
                    ${entry.votesCount}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<h2>Grafički prikaz rezultata</h2>
<img alt="Pie-chart" src="<c:url value="/servleti/glasanje-grafika">
                    <c:param name="pollID" value="${pollId}"/>
                    </c:url>" width="500" height="500"/>
<h2>Rezultati u XLS formatu</h2>
<p>Rezultati u XLS formatu dostupni su <a href="<c:url value="/servleti/glasanje-xls">
                    <c:param name="pollID" value="${pollId}"/>
                    </c:url>">ovdje</a></p>
<h2>Razno</h2>
<p>Primjeri pobjedničkih linkova:</p>
<ul>
    <c:forEach items="${winners}" var="winner" varStatus="status">
        <li>
            <a href="${winner.optionLink}" target="_blank">${winner.optionTitle}</a>
        </li>
    </c:forEach>
</ul>
<br>
<a href="<c:url value="/index.html"/>">Home</a>
</body>
</html>
