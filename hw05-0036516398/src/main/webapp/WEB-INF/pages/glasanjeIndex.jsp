<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 9.4.2021.
  Time: 15:19
  Voting page.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="candidates" scope="request" type="java.util.Collection"/>
<jsp:useBean id="poll" scope="request" type="hr.fer.oprpp2.hw5.model.Poll"/>
<!DOCTYPE html>
<html>
<head>
    <title>Glasanje</title>
</head>
<body>
<h1>${poll.title}:</h1>
<p>${poll.message}</p>
<ol>
    <c:forEach items="${candidates}" var="candidate" varStatus="status">
        <li><a href="<c:url value="/servleti/glasanje-glasaj">
                    <c:param name="ID" value="${candidate.id}"/>
                    </c:url>">
                ${candidate.optionTitle}</a>
        </li>
    </c:forEach>
</ol>
</body>
</html>
