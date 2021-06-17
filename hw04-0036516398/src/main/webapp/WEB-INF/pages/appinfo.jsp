<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 9.4.2021.
  Time: 13:38
  App info page
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="bgCol" scope="request" type="java.lang.String"/>
<jsp:useBean id="since_start" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<style>
    body {
        background-color: ${bgCol};
    }
</style>
<head>
    <title>Info</title>
</head>
<body>
<p>Time since start: ${since_start}. </p>
</body>
</html>
