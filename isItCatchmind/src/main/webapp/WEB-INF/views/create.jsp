<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	방을 만들어방
</h1>

<P> 소맥마시고싶어 </P>
<form action="<c:url value="/room/create" />" method="post">
	방 제목<input name="roomName" type="text"/>
	<button type="submit" name="submit" >생성항당</button>
</form>
</body>
</html>
