<%@ page import="hr.fer.oprpp2.hw4.GlobalData" %>
<%@ page import="hr.fer.oprpp2.hw4.Keys" %>
<%--
  Created by IntelliJ IDEA.
  User: MatejCubek
  Date: 9.4.2021.
  Time: 10:18
  Funny story page.
--%>
<%!
    private String randomColorGenerator(GlobalData globalData) {
        String letters = "0123456789ABCDEF";
        StringBuilder color = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            color.append(letters.charAt(globalData.createRandomInt(0, 15)));
        }
        return color.toString();
    }
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="bgCol" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<style>
    body {
        background-color: ${bgCol};
    }

    .funny {
        color: <%=randomColorGenerator((GlobalData) request.getServletContext().getAttribute(Keys.KEY_GLOBAL_DATA))%>;
    }
</style>
<head>
    <title>Funny Story</title>
</head>
<body>
<a href="<c:url value="/index.jsp"/>">Home</a>
<pre class="funny">

Mujo odlučio otići u inozemstvo naći posao i završi kao drvosječa u Kanadi.
Prvi dan narezao Mujo 15 kubika drva, ali je bio najgori.
Drugi dan zapne Mujo i nareže 20 kubika, ali je opet bio najgori.
Nikako mu nije bilo jasno kako on toliko radi i svejedno je gori od Kanađanina, pa upita kanadskog kolegu:

- Šta ti radiš da si bolji od mene?

Kanađanin kaže:

- Ja ti se probudim u 6 ..

Mujo klimne glavom, a kanađanin nastavi:

- Doručkujem i odem na posao ...

Mujo opet potvrdno klimne, a kanađanin kaže:

- Obućem radno odijelo, upalim motorku ...

Mujo se pljesne po čelu:

- A upališ motorku?!

</pre>
</body>
</html>
