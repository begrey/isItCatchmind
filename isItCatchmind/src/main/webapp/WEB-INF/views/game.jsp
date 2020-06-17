<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>

   <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/game.css" />">
   <title>GAME</title>

</head>
<body>
   환영한다 <strong>${userSession}</strong>
   <table>
        <thead>
            <tr>
                <th>아이디</th>
                <th>비밀번호</th>
                <th>이름</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${questionList}" var="question">
                <tr>
                    <td>${question.q_id}</td>
                    <td>${question.answer}</td>
                    <td>${question.category}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>


</body>
</html>