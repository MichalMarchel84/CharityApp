<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 16.06.2021
  Time: 11:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="pl">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"/>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.25/css/jquery.dataTables.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript" charset="utf8"
            src="https://cdn.datatables.net/1.10.25/js/jquery.dataTables.js"></script>
    <script>
        $(document).ready(function () {
            $('#table').DataTable({
                language: {
                    url: '//cdn.datatables.net/plug-ins/1.10.25/i18n/Polish.json'
                },
                "order": [[0, "desc"]]
            });
        });
    </script>
    <script src="/resources/js/profile.js" defer></script>
    <title>Profil</title>
</head>
<body>
<%@include file="../header.jsp" %>
<section id="profile" class="steps" style="position: relative">
    <div class="dialog-cont" style="display: none">
        <div class="dialog-box">
            <h2></h2>
            <form method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            <p class="errMsg"></p>
        </div>
    </div>
    <h2>Twoje konto</h2>
    <div class="steps--container">
        <div id="changeEmail" class="steps--item btn btn--large" style="padding: 10px">
            <span class="icon icon--mail-change"></span>
            <h3></h3>
            <p>Zmień email</p>
        </div>
        <div id="changePass" class="steps--item btn btn--large" style="padding: 10px">
            <span class="icon icon--pass-change"></span>
            <h3></h3>
            <p>Zmień hasło</p>
        </div>
        <div class="steps--item btn btn--large" style="padding: 10px" onclick="location.href='/user/address#address'">
            <span class="icon icon--edit"></span>
            <h3></h3>
            <p>Edytuj adresy</p>
        </div>
        <div class="steps--item btn btn--large" style="padding: 10px" onclick="location.href='/donate'">
            <span class="icon icon--donate"></span>
            <h3></h3>
            <p>Przekaż</p>
        </div>
        <div class="steps--item btn btn--large" style="padding: 10px" onclick="location.href='#contact'">
            <span class="icon icon--contact"></span>
            <h3></h3>
            <p>Kontakt</p>
        </div>
    </div>
</section>
<section id="donations" class="steps">
    <h2>Twoje zbiórki</h2>

    <c:if test="${donations.size() eq 0}">
        <h2>Nie oddałeś jeszcze żadnych przedmiotów</h2>
    </c:if>

    <c:if test="${donations.size() > 0}">
        <div class="table-cont">
            <table id="table" class="table">
                <thead>
                <tr>
                    <th style="width: 10vw">Data odbioru</th>
                    <th style="width: 20vw">Zawartość</th>
                    <th style="width: 5vw">Ilość worków</th>
                    <th style="width: 25vw">Instytucja</th>
                    <th style="width: 15vw">Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${donations}" var="donation">
                    <tr>
                        <td>${donation.pickUpDate.toString()} ${donation.pickUpTime.toString()}</td>
                        <td>
                            <ul>
                                <c:forEach items="${donation.categories}" var="categoty">
                                    <li>${categoty.name}</li>
                                </c:forEach>
                            </ul>
                        </td>
                        <td>${donation.quantity}</td>
                        <td>${fn:escapeXml(donation.institution.name)}</td>
                        <td>${donation.status.name}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</section>
<%@include file="../footer.jsp" %>
</body>
</html>
