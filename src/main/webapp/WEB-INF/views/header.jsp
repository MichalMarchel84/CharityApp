<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 16.06.2021
  Time: 09:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>Header</title>
</head>
<body>
<header class="header--main-page">
    <nav class="container container--70">
        <ul class="nav--actions">
            <sec:authorize access="!isAuthenticated()">
                <li><a href="/login" class="btn btn--small btn--without-border">Zaloguj</a></li>
                <li><a href="/register" class="btn btn--small btn--highlighted">Załóż konto</a></li>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <li class="logged-user">
                    <sec:authentication property="principal.username"/>
                    <ul class="dropdown">
                        <li><a href="/user#profile">Profil</a></li>
                        <li><a href="/user#donations">Moje zbiórki</a></li>
                        <sec:authorize access="hasAnyRole('ADMIN')">
                            <li><a href="/admin">Zarządzanie</a></li>
                        </sec:authorize>
                        <li>
                            <form action="<c:url value="/logout"/>" method="post">
                                <input type="submit" value="Wyloguj" class="btn btn--small btn--without-border">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </form>
                        </li>
                    </ul>
                </li>
            </sec:authorize>
        </ul>

        <ul>
            <li><a href="/" class="btn btn--without-border active">Start</a></li>
            <li><a href="/#info" class="btn btn--without-border">O co chodzi?</a></li>
            <li><a href="/#about" class="btn btn--without-border">O nas</a></li>
            <li><a href="/#inst" class="btn btn--without-border">Fundacje i organizacje</a></li>
            <li><a href="#contact" class="btn btn--without-border">Kontakt</a></li>
        </ul>
    </nav>

    <div class="slogan container container--90">
        <div class="slogan--item">
            <h1>
                <p style="color: red">Uwaga! Strona wyłącznie w celach prezentacyjnych.<br/>Żadne z zamieszczonych treści nie są autentyczne</p>
                ${(empty pageMsg) ?
                "Zacznij pomagać!<br/> Oddaj niechciane rzeczy w zaufane ręce"
                : pageMsg}
            </h1>
        </div>
    </div>
</header>
</body>
</html>
