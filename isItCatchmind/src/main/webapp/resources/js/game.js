const canvas = document.getElementById("canvas");
const eraseAll = document.getElementById("e");
const color = document.getElementById("color");
const thick = document.getElementById("thick");
const eraser = document.getElementById("eraser");
const show = document.getElementById("showDrawing");
const paintingBoard = document.getElementById("paintingBoard");
const showPainting = document.getElementById("myImage");
const sendBt = document.getElementById("chatSend");
const readyBy = document.getElementById("ready");
const chat = document.getElementById("writeChat");

var show_ans = document.getElementById("showAns");
var show_ctg = document.getElementById("showCategory");
var answer = document.getElementById("answer");
var category = document.getElementById("category");

var ctx = canvas.getContext("2d");
var pos = {
	drawable : false,
	x : -1,
	y : -1
};

window.onload = function() {
	// 컴퓨터 마우스
	canvas.addEventListener("mousedown", drawing);
	canvas.addEventListener("mousemove", drawing);
	canvas.addEventListener("mouseup", drawing);
	canvas.addEventListener("mouseout", drawing);
	// 스마트폰 터치
	canvas.addEventListener("touchstart", touchdraw);
	canvas.addEventListener("touchmove", touchdraw);
	canvas.addEventListener("touchcancel", touchdraw);
	canvas.addEventListener("touchend", touchdraw);
	// 그림판 옵션
	eraseAll.addEventListener("click", removeAll);
	eraser.addEventListener("click", remove);
	color.addEventListener("onclick", selectColor);
	thick.addEventListener("onchange", selectThick);
	//채팅 엔터인식
	chat.addEventListener("keydown", enterToSend);
	
}
function enterToSend(event) {
	if (event.keyCode == 13) {
		sendChat();
	}
}
// 맞추는 사람 : 그림판 및 채색도구 가림
function viewerTurn() {
	color.style.visibility = "hidden";
	canvas.style.display = "none";
	sendBt.style.visibility = "visible";
	readyBy.style.visibility = "hidden";
	show_ans.style.visibility = "hidden";
	// 정답부분 가리고 카테고리 보여주기
}
// 문제내는 사람 : 채팅 제출 버튼 가림
function painterTurn() {
	color.style.visibility = "visible";
	canvas.style.visibility = "visible";
	sendBt.style.visibility = "hidden";
	readyBy.style.visibility = "hidden";
	// 정답부분 보여주고 카테고리 보여주기
	show_ans.style.visibility = "visible";
	show_ans.innerHTML = answer.innerHTML;
	show_ctg.innerHTML = category.innerHTML;
}
// 게임 끝났을 때 : 상태 초기화 (정답, 카테고리 비우기도 추가)
function endTurn() {
	answer.style.visibility="hidden";
	category.style.visibility="hidden";
	color.style.visibility = "visible";
	canvas.style.visibility = "visible";
	sendBt.style.visibility = "visible";
	readyBy.style.visibility = "visible";
}

function selectThick(thick) {
	ctx.lineWidth = thick.value;
	ctx.fillWidth = thick.value;
}

function selectColor(color) {
	ctx.strokeStyle = color;
	ctx.fillStyle = color;
}


function touchdraw(event) {
	function getPosition() {
		var bound = canvas.getBoundingClientRect();
		var x = (event.targetTouches[0].clientX - bound.left) * (canvas.width / bound.width);
		var y = (event.targetTouches[0].clientY - bound.top) * (canvas.height / bound.height);
		// console.log(event.pageX + "<->"+x+" "+y+"<->" + event.pageY);
		return {
			X : x,
			Y : y
		};
	}
	switch (event.type) {
	case "touchstart": {
		ctx.beginPath();
		pos.drawable = true;
		var coors = getPosition();
		pos.X = coors.X;
		pos.Y = coors.Y;
		ctx.fillRect(pos.X, pos.Y, ctx.lineWidth, ctx.lineWidth);
		ctx.stroke();
		break;
		break;
	}
		break;
	case "touchmove": {
		if (pos.drawable) {
			// 스크롤 이동등 이벤트 중지..
			event.preventDefault();
			var coors = getPosition();
			ctx.lineTo(coors.X, coors.Y);
			pos.X = coors.X;
			pos.Y = coors.Y;
			ctx.stroke();
		}
	}
		break;
	case "touchend":
	case "touchcancel": {
		drawable = false;
		ctx.closePath();
	}
		break;
	}
}

function drawing(event) {
	function getPosition() {
		var bound = canvas.getBoundingClientRect();
		var x = (event.clientX - bound.left) * (canvas.width / bound.width);
		var y = (event.clientY - bound.top) * (canvas.height / bound.height);
		// console.log(event.pageX + "<->"+x+" "+y+"<->" + event.pageY);
		return {
			X : x,
			Y : y
		};
	}
	switch (event.type) {
	case "mousedown":
		ctx.beginPath();
		pos.drawable = true;
		var coors = getPosition();
		pos.X = coors.X;
		pos.Y = coors.Y;
		ctx.fillRect(pos.X, pos.Y, ctx.lineWidth, ctx.lineWidth);
		ctx.stroke();
		break;
	case "mousemove":
		if (pos.drawable){
			var coors = getPosition();
			ctx.lineTo(coors.X, coors.Y);
			pos.X = coors.X;
			pos.Y = coors.Y;
			ctx.stroke();
		}
		break;
	case "mouseout":
	case "mouseup":
		pos.drawable = false;
		pos.X = -1;
		pos.Y = -1;
		break;
	}

}

function remove() {
	ctx.strokeStyle = "white";
}
function removeAll() {
	ctx.clearRect(0, 0, canvas.width, canvas.height);
}