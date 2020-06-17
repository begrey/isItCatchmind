<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/room.css" />">
	<title>LOBBY</title>
</head>
<body>

	<img src="<c:url value="/resources/img/로비일까.png" />" />

	<br />
	환영한다 <strong>${userSession}</strong>
	
    <div>        
        
    </div>
	<br/>

	<div id="messages"></div>
		방에 입장할까? <a href="<c:url value="/game" />">ROOM 입장</a>
		<br />
		게임 <a href="<c:url value="/game" />">게임화면</a>

	
</body>
</html>