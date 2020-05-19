<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/room.css" />">
	<title>Home</title>
</head>
<body>

<img src="<c:url value="/resources/img/배너일까.png" />" />
<br />
<P> 사용할 닉네임을 입력하시던가 </P>
<div class="write">
<form action="<c:url value="/user/create" />" method="post">
	<input class="textForm" name="nickName" type="text"/>
	<button class="button" type="submit" name="submit" ><img src="<c:url value="/resources/img/입력.png" />" /></button>
</form>
</div>
<img src="<c:url value="/resources/img/캐릭터누끼.png" />" />


</body>
</html>
