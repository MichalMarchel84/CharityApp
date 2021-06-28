<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 16.06.2021
  Time: 11:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"/>
    <title>Rejestracja</title>
</head>
<body>
<%@include file="header.jsp"%>
<section class="login-page" id="register">
    <h2>Załóż konto</h2>

    <form:form modelAttribute="user">
        <div class="form-group" style="display: flex; flex-direction: column; align-items: center">
            <form:input path="email" type="email" placeholder="Email" cssStyle="max-width: 300px; text-align: center"/><br/>
            <span class="errMsg">${emailError}</span>
            <form:errors path="email" cssClass="errMsg"/>
        </div>

        <div class="form-group" style="display: flex; flex-direction: column; align-items: center">
            <form:password path="password" placeholder="Hasło" cssStyle="max-width: 300px; text-align: center"/><br/>
            <form:errors path="password" cssClass="errMsg"/>
        </div>

        <div class="form-group" style="display: flex; flex-direction: column; align-items: center">
            <input type="password" name="repeat" placeholder="Powtórz hasło" style="max-width: 300px; text-align: center"/><br/>
            <span class="errMsg">${repeatError}</span>
        </div>

        <div class="form-group form-group--buttons">
            <a href="/login" class="btn btn--without-border">Zaloguj się</a>
            <button class="btn" type="submit">Załóż konto</button>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form:form>
</section>
<%@include file="footer.jsp"%>
<script>
    location.href="#register";
</script>
</body>
</html>
