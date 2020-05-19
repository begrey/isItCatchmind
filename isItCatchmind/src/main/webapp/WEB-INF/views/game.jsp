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
   <div class="select">
   <button id="e">삭제</button>
   </div>
   <div id="color" class="radioSelect">
     <input type="radio" name="red" id="red" class="red" onclick="selectColor(this.name)"/>
     <label for="red"></label>
     <input type="radio" name="blue" id="blue" class="blue" onclick="selectColor(this.name)"/>
     <label for="blue"></label>
     <input type="radio" name="yellow" id="yellow" class="yellow" onclick="selectColor(this.name)"/>
     <label for="yellow"></label>
     <input type="radio" name="green" id="green" class="green" onclick="selectColor(this.name)"/>
     <label for="green"></label>
     <input type="radio" name="black" id="black" class="black" onclick="selectColor(this.name)"/>
     <label for="black"></label>
     <input type="range" name="thick" id="thick" class="thick" value="1" min="1" max="10" onchange="selectThick(this)" />
     <button id="eraser">지우개</button>
     <button id="showDrawing" onclick="toDataURL()">제출</button>
    </div>
   <div>
      <canvas id="canvas">
      </canvas>
   </div>
   <img id="myImage">
   <script type="text/javascript" src="<c:url value="/resources/js/game.js"/>"></script>
</body>
</html>