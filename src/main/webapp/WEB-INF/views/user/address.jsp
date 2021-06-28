<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 16.06.2021
  Time: 11:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"/>
    <title>Dane adresowe</title>
    <script src="/resources/js/address.js" defer></script>
</head>
<body>
<%@include file="../header.jsp" %>
<section id="address" class="steps">
    <h2>Twoje adresy</h2>
    <div class="address-cont" data-pn="${_csrf.parameterName}" data-pt="${_csrf.token}">
        <c:forEach items="${addresses}" var="address">
            <div class="address">
                <form method="post" action="/user/address">
                    <input type="hidden" name="id" value="${address.id}">
                    <input type="text" name="street" value="${fn:escapeXml(address.street)}" placeholder="Ulica">
                    <input type="text" name="city" value="${fn:escapeXml(address.city)}" placeholder="Miasto">
                    <input type="text" name="postCode" value="${address.postCode}" placeholder="Kod pocztowy">
                    <input type="text" name="phone" value="${address.phone}" placeholder="Telefon">
                    <div>
                        <input type="submit" value="Zapisz" class="btn btn--small">
                        <button name="delete" class="btn btn--small">Usuń</button>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </c:forEach>
        <div class="add-address">
            <div id="add" class="steps--item btn btn--large" style="margin-top: 0">
                <span class="icon icon--add"></span>
                <h3></h3>
                <p>Dodaj</p>
            </div>
        </div>
    </div>
    <p class="errMsg">${errMsg}</p>
</section>
<%@include file="../footer.jsp" %>
</body>
</html>
