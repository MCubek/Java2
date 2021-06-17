<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 14.5.2021.
  Time: 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="polls" scope="request" type="java.util.List"/>
<html>
<head>
    <title>Polls</title>
</head>
<body>
    <h1>List of polls:</h1>
    <ol>
        <c:forEach items="${polls}" var="poll" varStatus="status">
            <li>
                <a href="<c:url value="/servleti/glasanje">
                    <c:param name="pollID" value="${poll.id}"/>
                    </c:url>">
                        ${poll.title}</a>
            </li>
        </c:forEach>
    </ol>
</body>
</html>
