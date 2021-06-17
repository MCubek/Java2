<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 2.6.2021.
  Time: 17:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="authors" scope="request" type="java.util.Collection"/>
<jsp:useBean id="currentID" scope="request" type="java.lang.Long"/>
<jsp:useBean id="currentFN" scope="request" type="java.lang.String"/>
<jsp:useBean id="currentLN" scope="request" type="java.lang.String"/>
<jsp:useBean id="errorMessage" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>

<script type="text/javascript"><!--

function getStatistics() {
    var xmlhttp;

    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var text = xmlhttp.responseText;
            var entries = JSON.parse(text);
            var html = `
             <table style="border: 1px solid black">
  <tr>
    <th>Author</th>
    <th>Blog Number</th>
  </tr>
            `;
            for (var i = 0; i < entries.length; i++) {
                html += '<tr>' +
                    '<td>' + entries[i].author + '</td> <td>' + entries[i].blogs + '</td>' +
                    '</tr>';
            }
            html += '</table>';
            document.getElementById("table").innerHTML = html;
        }
    }
    xmlhttp.open("GET", "statistics?dummy=" + Math.random(), true);
    xmlhttp.send();
}

//--></script>

<link rel="stylesheet" href="<c:url value="/styles.css"/>">

<body>
<div class="topnav">
    <a class="active" href="<c:url value="/servleti/main"/>">Home/Login</a>
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
<c:if test="${currentID<0}">
    <div>
        <a>Login:</a>
        <form action="<c:url value="/servleti/main"/>" method="post">
            <label for="nick">Nickname:</label>
            <input required type="text" id="nick" name="nick"><br><br>
            <label for="password">Password:</label>
            <input required type="password" id="password" name="password"><br><br>
            <input type="submit" value="Login">
        </form>
        <c:if test="${!errorMessage.blank}">
            <a style="color: red">${errorMessage}</a>
        </c:if>
    </div>
    <div>
        <a href="<c:url value="/servleti/register"/>">Register</a>
    </div>
</c:if>
<div>
    <a>Authors:</a>
    <ol>
        <c:forEach items="${authors}" var="author">
            <li>
                <a href="<c:url value="/servleti/author/${author.nick}"/>">
                        ${author.nick}
                </a>
            </li>
        </c:forEach>
    </ol>
</div>
<button onclick="getStatistics();">Statistika</button>
<div id="table">
    Empty
</div>
</body>
</html>