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
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <li>
                    <a href="#" class="btn btn--small btn--without-border">
                        <sec:authentication property="principal.username"/>
                    </a>
                </li>
                <li>
                    <form action="<c:url value="/logout"/>" method="post">
                        <input type="submit" value="Wyloguj" class="btn btn--small btn--without-border">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                </li>
            </sec:authorize>
            <li><a href="/register" class="btn btn--small btn--highlighted">Załóż konto</a></li>
        </ul>

        <ul>
            <li><a href="/" class="btn btn--without-border active">Start</a></li>
            <li><a href="#" class="btn btn--without-border">O co chodzi?</a></li>
            <li><a href="#" class="btn btn--without-border">O nas</a></li>
            <li><a href="#" class="btn btn--without-border">Fundacje i organizacje</a></li>
            <li><a href="#contact" class="btn btn--without-border">Kontakt</a></li>
        </ul>
    </nav>

    <div class="slogan container container--90">
        <div class="slogan--item">
            <h1>
                Zacznij pomagać!<br/>
                Oddaj niechciane rzeczy w zaufane ręce
            </h1>
        </div>
    </div>
</header>
</body>
</html>
