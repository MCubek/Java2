<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="MESSAGE" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
</head>
<body>
	<h1>Error has occurred!</h1>
	<p><c:out value="${MESSAGE}"/></p>
</body>
</html>