const canvas = document.getElementById("canvas");
const eraseAll = document.getElementById("e");
const color = document.getElementById("color");
const thick = document.getElementById("thick");
const eraser = document.getElementById("eraser");
const show = document.getElementById("showDrawing");
const timer = document.getElementById("timer");
const start = document.getElementById("start");
const paintingBoard = document.getElementById("paintingBoard");
const chat = document.getElementById("chatFetch");
var ctx = canvas.getContext("2d");
var pos = {
      drawable: false,
      x: -1,
      y: -1
};

window.onload = function() {
   canvas.addEventListener("mousedown", drawing);
   canvas.addEventListener("mousemove", drawing);
   canvas.addEventListener("mouseup", drawing);
   canvas.addEventListener("mouseout", drawing);
   eraseAll.addEventListener("click", removeAll);
   eraser.addEventListener("click", remove);
   color.addEventListener("onclick", selectColor);
   thick.addEventListener("onchange", selectThick);
}

function showReady(r) {
	r.style.visibility = "visible";
}
function hideReady(r) {
	r.style.visibility = "hidden";
}


function gameStart() {
	var total = document.getElementById("total").innerText; //인원수
	var chat = document.getElementById("chatSend"); 
	var myTurn = document.getElementById("myTurn").innerText; //내 차례 (턴)
	var painting = true; //그리는 턴과 맞추는 턴을 구분
	var time = 5;
	var turn = 0;
	var round = total* 2; // 게임은 인원수 * 2판 즉, 두바퀴
	var t = setInterval(function() {
		time--;
		timer.innerHTML = time + "초 남았습니다.";
		if (time == 0) {
			if (round == 1) {
				alert("종료");
				clearInterval(t);
				timer.innerHTML = "";
			}
			else {
				alert("다음게임");
				round--;
				time = 5;
			}
		}		
		
	}, 1000);
}


function selectThick(thick) {
   ctx.lineWidth = thick.value;
}

function selectColor(color) {
   ctx.strokeStyle= color;
}
 
function drawing(event){
   switch(event.type) {
      case "mousedown":
         initDraw(event);
         break;
      case "mousemove":
         if(pos.drawable)
            draw(event);
         break;
      case "mouseout":
      case "mouseup" :
         finishDraw();
         break;
   }

}   

function initDraw(event) {
   ctx.beginPath();
   pos.drawable = true;
   var coors = getPosition(event);
   pos.X = coors.X;
   pos.Y = coors.Y;
   ctx.arc(pos.X, pos.Y,1,0, 2*Math.PI);
   ctx.fill();
   ctx.stroke();
}

function draw(event) {
   var coors = getPosition(event);
   ctx.lineTo(coors.X, coors.Y);
   pos.X = coors.X;
   pos.Y = coors.Y;
   ctx.stroke();
}
function remove() {
	ctx.strokeStyle= "white";
}
function removeAll() {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
}

function finishDraw() {
   pos.drawable = false;
   pos.X = -1;
   pos.Y = -1;
}
function getPosition(event){
   var bound = canvas.getBoundingClientRect();
   var x = (event.clientX - bound.left) * (canvas.width / bound.width);
   var y = (event.clientY - bound.top) * (canvas.height / bound.height);
   //console.log(event.pageX + "<->"+x+" "+y+"<->" + event.pageY);
   return {X:x, Y:y};
}