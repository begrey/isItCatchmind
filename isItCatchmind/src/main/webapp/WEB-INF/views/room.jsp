<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/room.css" />">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/game.css" />">
<title>채팅방이란다!</title>
</head>
<body>
	반가워! ${userSession}
	<div id="total" style="display: none"></div>
	<c:forEach items="${questionList}" var="question">
		<div id="answer" style="display: none">${question.answer}</div>
		<div id="category" style="display: none">${question.category}</div>
	</c:forEach>
	<div class="show">
		<div id="showAns"></div>
		<div id="showCategory"></div>
	</div>
	<button class="exitBt" type="button" onclick="closeSocket();">
			<img class ="exit" src="<c:url value="/resources/img/나가기.png" />" />
	</button>
	<div class="write" id="writeChat">
		<input type="text" id="sender" value="${userSession}" style="display: none;"> 
		<input class="textForm" type="text" id="messageinput">
		<button class="button" type="submit" id="chatSend" onclick="sendChat();">
		<img class="submit" src="<c:url value="/resources/img/입력.png" />" />
		</button>	
	</div>
	<button class="RdButton" onclick="ready();" type="button">
	<img class="readyBt" id="ready" src="<c:url value="/resources/img/readyBt.png" />" />
	</button>
	<!-- Server responses get written here -->
	<div class="grid">
		<div class="rBox">
			<img class="ready" id="r0"
				src="<c:url value="/resources/img/unready.png" />" />
		</div>
		<div class="rBox">
			<img class="ready" id="r1"
				src="<c:url value="/resources/img/unready.png" />" />
		</div>
		<div class="rBox">
			<img class="ready" id="r2"
				src="<c:url value="/resources/img/unready.png" />" />
		</div>
		<div class="rBox">
			<img class="ready" id="r3"
				src="<c:url value="/resources/img/unready.png" />" />
		</div>
	</div>
	<div class="grid">
		<div class="box">
			<img class="nick" src="<c:url value="/resources/img/유저1.png" />" />
			<div id="00" class="user"></div>
		</div>
		<div class="box">
			<img class="nick" src="<c:url value="/resources/img/유저2.png" />" />
			<div id="01" class="user"></div>
		</div>
		<div class="box">
			<img class="nick" src="<c:url value="/resources/img/유저3.png" />" />
			<div id="02" class="user"></div>
		</div>
		<div class="box">
			<img class="nick" src="<c:url value="/resources/img/유저4.png" />" />
			<div id="03" class="user"></div>
		</div>
	</div>
	<div class="grid">
		<div class="box">
			<img class="nick" src="<c:url value="/resources/img/말풍선1.png" />" />
			<div id="0" class="chat"></div>
		</div>
		<div class="box">
			<img class="nick" src="<c:url value="/resources/img/말풍선2.png" />" />
			<div id="1" class="chat"></div>
		</div>
		<div class="box">
			<img class="nick" src="<c:url value="/resources/img/말풍선3.png" />" />
			<div id="2" class="chat"></div>
		</div>
		<div class="box">
			<img class="nick" src="<c:url value="/resources/img/말풍선4.png" />" />
			<div id="3" class="chat"></div>
		</div>
	</div>
	<div id="paintingBoard">
		<div id="color" class="radioSelect">
			<input type="radio" name="red" id="red" class="red"
				onclick="selectColor(this.name)" /> <label for="red"></label> <input
				type="radio" name="blue" id="blue" class="blue"
				onclick="selectColor(this.name)" /> <label for="blue"></label> <input
				type="radio" name="yellow" id="yellow" class="yellow"
				onclick="selectColor(this.name)" /> <label for="yellow"></label> <input
				type="radio" name="green" id="green" class="green"
				onclick="selectColor(this.name)" /> <label for="green"></label> <input
				type="radio" name="black" id="black" class="black"
				onclick="selectColor(this.name)" /> <label for="black"></label> <input
				type="range" name="thick" id="thick" class="thick" value="1" min="1"
				max="10" onchange="selectThick(this)" />
			<button id="eraser">지우개</button>
			<button id="e">전체지우기</button>
			<button id="summit" onclick="sendDrawing()">제출</button>
		</div>
		<div>
			<canvas id="canvas">
         </canvas>
			<img id="myImage">
		</div>
	</div>
	<script type="text/javascript"
		src="<c:url value="/resources/js/game.js"/>"></script>

	<!-- websocket javascript -->
	<script type="text/javascript">             
		var url = "ws://172.30.52.82:8091/isItCatchmind/websocketChat/"
        var nickname= "<%=(String) session.getAttribute("userSession")%>"
		var ws = new WebSocket(url + nickname);
		var gameOn = false;

		ws.onopen = function(event) {
			//alert(nickname+"님! 입장을 환영합니다요~ send를 눌러 채팅을 이용해보세요! :)");          
			if (event.data === undefined) {
				return;
			}
		};
		ws.onmessage = function(event) {
			var msg = JSON.parse(event.data)
			writeResponse(msg);
		};
		ws.onclose = function(event) {
			alert("잘 가시라요~^^");
			location.href = "<c:url value="/room/lobby" />";
		}
		function ready() {
			var readyBt = document.getElementById("ready");
			if (readyBt.src.includes("<c:url value="/resources/img/readyBt.png" />")) {
				readyBt.src = "<c:url value="/resources/img/unreadyBt.png" />";
				var ready = {
					type : "ready",
					text : "readyOn"
				};
			} else {
				readyBt.src = "<c:url value="/resources/img/readyBt.png" />"
				var ready = {
					type : "ready",
					text : "readyOff"
				};
			}
			ws.send(JSON.stringify(ready));
		}

		function sendDrawing() {
			var canvas = document.getElementById("canvas");
			var dataURL = canvas.toDataURL();
			var myImage = document.getElementById('myImage');
			myImage.src = dataURL;
			var drawing = {
				type : "drawing",
				text : dataURL
			};
			ws.send(JSON.stringify(drawing));
		}

		function sendChat() {
			if (gameOn == true) {
				var correct = document.getElementById("showAns").innerHTML;
				var answer = document.getElementById("messageinput").value;
				if (answer == correct) {
					var type = "correct";
					var text = "정답입니다!!";
				} else {
					var type = "chat";
					var text = document.getElementById("messageinput").value;
				}
			} else {
				var type = "chat";
				var text = document.getElementById("messageinput").value;
			}
			var chat = {
				type : type,
				text : text
			};
			ws.send(JSON.stringify(chat));
			document.getElementById("messageinput").value = "";
		}

		function closeSocket() {
			ws.close();
		}
		function writeResponse(msg) {
			var div = msg.div
			var user = document.getElementById("0" + div);
			var messages = document.getElementById(div);
			var myImage = document.getElementById('myImage');
			var total = document.getElementById('total');
			var turn = document.getElementById('myTurn');
			var ready = document.getElementById("r" + div);
			var readyBt = document.getElementById("ready");			
			switch (msg.type) {
			case "message": //채팅방 메세지
				messages.innerHTML = msg.text;
				break;
			case "show": //닉네임 보여주기
				user.innerHTML = msg.text;
				break;
			case "alert": //종료
				alert(msg.text);
				ws.close();
				break;
			case "image": //그림판 보여주기
				myImage.src = "";
				myImage.src = msg.text;
				break;
			case "total": //총 인원 보여주기
				total.innerHTML = msg.text;
				break;
			case "ready":
				var readyBt = document.getElementById("ready");
				if (msg.text.includes("on")) {
					ready.src = "<c:url value="/resources/img/ready.png" />";
					if (!msg.text.includes("Other"))
						readyBt.src = "<c:url value="/resources/img/unreadyBt.png" />";
					//레디 누른 사용자에게만 버튼 내용 바뀌기
				} else {
					ready.src = "<c:url value="/resources/img/unready.png" />";
					if (!msg.text.includes("Other"))
						readyBt.src = "<c:url value="/resources/img/readyBt.png" />";
				}
				break;
			case "start":
				alert("시자악~하겠습니다~ *^_^*");
				var ready = {
					type : "start",
					text : "start"
				};
				ws.send(JSON.stringify(ready));
				gameOn = true;
				break;
			case "gameover":
				alert(msg.text);
				endTurn();
				gameOn = false;
				location.href = "<c:url value="/game" />";
				break;
			case "viewer":				
				alert(msg.text);
				viewerTurn();
				break;
			case "painter":
				alert(msg.text);
				var answer = {
					type : "answer",
					text : document.getElementById("answer").innerHTML + "&"
							+ document.getElementById("category").innerHTML
				};
				ws.send(JSON.stringify(answer));
				painterTurn();
				break;
			case "correct":
				myImage.src = "";
				alert(msg.text);
				break;
			case "answer":
				//test:문제 정답 div:카테고리
				var show_ans = document.getElementById("showAns");
				var show_ctg = document.getElementById("showCategory");
				show_ans.innerHTML = msg.text;
				show_ctg.innerHTML = div;
				break;
			}

		}
	</script>
</body>
</html>