<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 16.06.2021
  Time: 11:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"/>
    <script src="/resources/js/admin.js" defer></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.25/css/jquery.dataTables.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript" charset="utf8"
            src="https://cdn.datatables.net/1.10.25/js/jquery.dataTables.js"></script>
    <script>
        $(document).ready(function () {
            $('#users').DataTable({
                language: {
                    url: '//cdn.datatables.net/plug-ins/1.10.25/i18n/Polish.json'
                }
            });
            $('#donations').DataTable({
                language: {
                    url: '//cdn.datatables.net/plug-ins/1.10.25/i18n/Polish.json'
                }
            });
        });
    </script>
    <title>Oddam w dobre ręce</title>
</head>
<body>
<%@include file="../header.jsp" %>
<section id="institutions" class="steps">
    <h2>Instytucje</h2>
    <div class="address-cont" data-pn="${_csrf.parameterName}" data-pt="${_csrf.token}">
        <c:forEach items="${institutions}" var="institution">
            <div class="address">
                <form method="post" action="/admin/institutions" id="id${institution.id}">
                    <input type="hidden" name="id" value="${institution.id}">
                    <input type="text" name="name" value="${fn:escapeXml(institution.name)}" placeholder="Nazwa">
                    <textarea name="description" form="id${institution.id}" placeholder="Opis" rows="4"><c:out
                            value="${institution.description}"/></textarea>
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
</section>
<section class="steps">
    <h2>Użytkownicy</h2>
    <div class="table-cont">
        <table id="users" class="table">
            <thead>
            <tr>
                <th style="width: 30vw">Email</th>
                <th style="width: 10vw">Status</th>
                <th style="width: 10vw">Rola</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="user">
                <tr data-id="${user.id}">
                    <td>${fn:escapeXml(user.email)}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.enabled == 1}">
                                Aktywny
                            </c:when>
                            <c:when test="${user.enabled == 0}">
                                Zablokowany
                            </c:when>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.roles.toArray()[0].name.equals('ROLE_ADMIN')}">
                                Administrator
                            </c:when>
                            <c:when test="${user.roles.toArray()[0].name.equals('ROLE_USER')}">
                                Użytkownik
                            </c:when>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</section>
<section class="steps">
    <h2>Darowizny</h2>
    <div class="table-cont">
        <table id="donations" class="table" style="font-size: 15px">
            <thead>
            <tr>
                <th style="width: 8vw">Data odbioru</th>
                <th style="width: 12vw">Zawartość</th>
                <th style="width: 4vw">Ilość</th>
                <th style="width: 15vw">Instytucja</th>
                <th style="width: 15vw">Adres</th>
                <th style="width: 10vw">Kontakt</th>
                <th style="width: 10vw">Wiadomość</th>
                <th style="width: 10vw">Status</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${donations}" var="donation">
                <tr data-id="${donation.id}">
                    <td>${donation.pickUpDate.toString()} ${donation.pickUpTime.toString()}</td>
                    <td>
                        <ul>
                            <c:forEach items="${donation.categories}" var="categoty">
                                <li>${fn:escapeXml(categoty.name)}</li>
                            </c:forEach>
                        </ul>
                    </td>
                    <td>${donation.quantity}</td>
                    <td>${fn:escapeXml(donation.institution.name)}</td>
                    <td>
                        ${fn:escapeXml(donation.address.street)}<br/>
                        ${donation.address.postCode} ${fn:escapeXml(donation.address.city)}
                    </td>
                    <td>
                        ${fn:escapeXml(donation.email)}
                        <c:if test="${!empty donation.address.phone}">
                            <br/>${donation.address.phone}
                        </c:if>
                    </td>
                    <td>${fn:escapeXml(donation.pickUpComment)}</td>
                    <td>${donation.status.name}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="dialog-cont" style="display: none; position: fixed; top: 0">
        <div class="dialog-box">
            <h2></h2>
            <form method="post">
                <input id="csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            <p class="errMsg"></p>
        </div>
    </div>
</section>

<%@include file="../footer.jsp" %>
</body>
</html>
