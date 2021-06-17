<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 2.6.2021.
  Time: 23:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<jsp:useBean id="currentID" scope="request" type="java.lang.Long"/>
<jsp:useBean id="currentFN" scope="request" type="java.lang.String"/>
<jsp:useBean id="currentLN" scope="request" type="java.lang.String"/>
<jsp:useBean id="authorId" scope="request" type="java.lang.Long"/>
<jsp:useBean id="authorNick" scope="request" type="java.lang.String"/>
<jsp:useBean id="blog" scope="request" type="hr.fer.oprpp2.hw6.blog.model.BlogEntry"/>
<jsp:useBean id="comments" scope="request" type="java.util.Collection"/>
<jsp:useBean id="brojSrca" scope="request" type="java.lang.Integer"/>
<!DOCTYPE html>
<html>
<head>
    <title>${blog.title}</title>
</head>

<link rel="stylesheet" href="<c:url value="/styles.css"/>">

<body>
<div class="topnav">
    <a href="<c:url value="/servleti/main"/>">Home/Login</a>
    <a href="<c:url value="/servleti/author/${authorNick}"/>">${authorNick}'s blogs</a>
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
<div>
    <h1>
        ${authorNick}'s blog: ${blog.title}
    </h1>
    <h4>
        Created: ${blog.createdAt} Last Modified: ${blog.lastModifiedAt}
    </h4>
    <p>
        ${fn:replace(blog.text, newLineChar, "<br>")}
    </p>
    <p>
        Srca: ${brojSrca}
    </p>
    <c:if test="${currentID>=0}">
        <a href="<c:url value="/servleti/heartPost/${blog.id}"/>">
            Dodaj Srce
        </a>
    </c:if>
</div>
<c:if test="${currentID.equals(authorId)}">
    <a href="<c:url value="/servleti/author/${authorNick}/edit">
        <c:param name="entryId" value="${blog.id}"/>
        </c:url>">
        Edit Blog Entry
    </a>
</c:if>
<hr>
<div>
    <ol>
        <c:forEach items="${comments}" var="comment">
            <li>
                <div>
                    Author: <c:choose>
                    <c:when test="${comment.user!=null}">${comment.user.nick}</c:when>
                    <c:otherwise>Anonymous</c:otherwise>
                </c:choose>
                    <br>
                    Date: ${comment.postedOn}<br>
                    Comment:
                        <p>
                            ${fn:replace(comment.message, newLineChar, "<br>")}
                        </p>
                </div>
            </li>
        </c:forEach>
    </ol>
</div>
<div>
    <h4>Add comment:</h4>
    <form action="<c:url value="/servleti/addComment"/>" method="post">
        <label for="message">Message:</label>
        <textarea rows="5" cols="60" id="message" required name="message"></textarea><br><br>
        <input type="hidden" id="entryId" name="entryId" value="${blog.id}">
        <input type="hidden" id="authorNick" name="authorNick" value="${authorNick}">
        <input type="submit" value="Submit Comment">
    </form>
</div>
</body>
</html>
