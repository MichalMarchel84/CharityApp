<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 16.06.2021
  Time: 10:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"/>
    <title>Logowanie</title>
</head>
<body>
<%@include file="header.jsp" %>
<section class="login-page" id="login">
    <h2>Zaloguj się</h2>
    <p class="newAccMsg">${newAccount}</p>
    <form method="post" action="/login">
        <div class="form-group" style="display: flex; flex-direction: column; align-items: center">
            <p style="font-size: 20px; text-align: center">Administrator: admin@admin.pl<br/>Hasło: admin1</p>
            <input type="email" name="username" value="${fn:escapeXml(email)}" placeholder="Email" style="max-width: 300px; text-align: center"/>
        </div>
        <div class="form-group" style="display: flex; flex-direction: column; align-items: center">
            <input type="password" name="password" placeholder="Hasło" style="max-width: 300px; text-align: center"/>
            <p class="errMsg">${errMsg}</p>
        </div>
        <div class="form-group form-group--buttons">
            <a href="/register" class="btn btn--without-border">Załóż konto</a>
            <button class="btn" type="submit">Zaloguj się</button>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</section>
<%@include file="footer.jsp" %>
<script>
    location.href="#login";
</script>
</body>
</html>
