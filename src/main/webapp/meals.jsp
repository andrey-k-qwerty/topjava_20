<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 08.06.2020
  Time: 11:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Еда</h2>
<table>
    <tr>
        <th>ID</th>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan=2>Action</th>
    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>
    <c:forEach var="meal" items="${meals}">
        <tr bgcolor="${meal.excess == true ? "red": "green" }">
            <td><c:out value="${meal.ID}"/></td>
            <fmt:parseDate value="${ meal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <td type="datetime-local"> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="meals?action=edit&Id=<c:out value="${meal.ID}"/>">Update</a></td>
            <td><a href="meals?action=delete&Id=<c:out value="${meal.ID}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<hr>
<h3><a href="meals?action=add">Добавить</a></h3>
</body>
</html>
