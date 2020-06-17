package com.begrey.isItCatchmind.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.begrey.isItCatchmind.domain.Question;
import com.begrey.isItCatchmind.service.QuestionService;

import org.json.simple.JSONObject; // JSON객체 생성
import org.json.simple.parser.JSONParser; // JSON객체 파싱
import org.json.simple.parser.ParseException; // 예외처리
//import javax.websocket.RemoteEndpoint.Basic;

@Controller
@ServerEndpoint(value = "/websocketChat/{nickname}")
public class WebSocketChat {

	private boolean overPeople = false;
	private static int game;
	private static final List<Session> sessionList = new ArrayList<Session>();
	private static List<String> userList = new ArrayList<String>();
	private static List<String> readyList = new ArrayList<String>();
	private static final Logger logger = LoggerFactory.getLogger(WebSocketChat.class);

	public WebSocketChat() {
		// TODO Auto-generated constructor stub
		System.out.println("웹소켓(서버) 객체생성");
	}


	public void setGameTurn(Session self) throws Exception {
		logger.info("정답 전");
		// 여기서 정답 호출
//    	List<Question> questionList = service.showAnswer();
//    	Question q = questionList.get(0);
//    	String answer = q.getAnswer();
//    	String category = q.getCategory();

		logger.info("여기로 들어온 사람" + self.getId());
		int turn = sessionList.indexOf(self);
		int gameTurn = game / sessionList.size();
		logger.info(gameTurn + "비교" + turn + "게임" + game);
		if (gameTurn == sessionList.size()) {
			sendText(self, "gameover", "게임이 끝났습니다!", "");

			// 마지막 사람이 들어오면 그때 모든것을 초기화
			if ((game + 1) / sessionList.size() - 1 == sessionList.size()) {
				game = 0;
				initReady();
				logger.info("마지막으로 끝내는 사람" + self.getId());
				return;
			}
			logger.info("끝난 사람" + self.getId());
			game++;
			return;
		} else if (gameTurn == turn) {
			sendText(self, "painter", "그리는 차례입니다!", "");
		} else {
			sendText(self, "viewer", "맞추는 차례입니다!", "");
		}
		game++;
	}

	// json형식으로 string형 데이터를 보내 jsp에서 용도에 맞게 분류하여 사용
	public void sendText(Session s, String type, String text, String div) {
		try {
			s.getBasicRemote()
					.sendText("{\"type\":\"" + type + "\",\"text\":\"" + text + "\",\"div\":\"" + div + "\"}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("nickname") String nickname) throws Exception {
		logger.info("Open session id:" + Integer.parseInt(session.getId(), 16));

		try {
			// 정원초과일때, alert보내고 js에서 연결종료함
			if (sessionList.size() == 4) {
				overPeople = true;
				throw new Exception();
			}
		} catch (Exception e) {
			// TODO: handle exception
			sendText(session, "alert", "이미선택된좌석입니다.(이선좌)", "");

			System.out.println(e.getMessage());
		}

		sessionList.add(session);
		userList.add(nickname);

		// 총 인원 현재 접속자에게 보여주기
		sendText(session, "total", Integer.toString(sessionList.size()), "-1");

		// 사용자 시점 : 배정받은 말풍선에 닉네임 표시
		sendText(session, "show", nickname, Integer.toString(sessionList.indexOf(session)));

		for (Session other : WebSocketChat.sessionList) {
			if (!session.getId().equals(other.getId())) {
				logger.info("먼저 입장했던 이들:" + userList.get(sessionList.indexOf(other)));
				// 다른 사용자들에게 내 닉네임 보여주기
				sendText(other, "show", nickname, Integer.toString(sessionList.indexOf(session)));
				// 사용자 시점 : 다른 사용자들의 닉네임 보여주기
				sendText(session, "show", userList.get(sessionList.indexOf(other)),
						Integer.toString(sessionList.indexOf(other)));
				// 바뀐 인원 모두에게 보여주기
				sendText(other, "total", Integer.toString(sessionList.size()), "-1");
			}
		}
		initReady();
	}

	// 어떤 사용자가 퇴장할 때, 왼쪽으로 남은 사용자 밀어두기
	private void showNickname() {
		for (Session s : WebSocketChat.sessionList) {
			for (int i = 0; i < WebSocketChat.sessionList.size(); i++) {
				// 남은 유저들에게 각각 자신의 닉네임과 상대방의 닉네임 보여주기, 채팅 기록은 다 삭제
				sendText(s, "show", userList.get(i), Integer.toString(i));
				sendText(s, " ", userList.get(i), Integer.toString(i));
			}
		}

	}

	// 어떤 사용자가 퇴장할 때, 레디 초기화하기
	private void initReady() throws Exception {
		for (Session s : WebSocketChat.sessionList) {
			sendText(s, "ready", "off", Integer.toString(sessionList.indexOf(s)));
			sendAllSessionToMessage(s, "off", "ready");
			readyList.remove(s.getId());
		}
	}

	private void readyState(Session self, String text) throws Exception {
		String session = self.getId();
		switch (text) {
		case "readyOn":
			readyList.add(session);
			logger.info("레디누른사람수:" + readyList.size());
			/*
			 * for (String i : readyList) { logger.info(i); }
			 */
			sendAllSessionToMessage(self, "on", "ready");
			sendText(self, "ready", "on", Integer.toString(sessionList.indexOf(self)));
			if (readyList.size() == userList.size() && readyList.size() != 1) {
				sendAllSessionToMessage(self, "start", "ready");
				sendText(self, "start", "시작합니다!", "-1");
			}

			break;
		case "readyOff":
			readyList.remove(session);
			logger.info("레디누른사람수:" + readyList.size());
			sendAllSessionToMessage(self, "off", "ready");
			sendText(self, "ready", "off", Integer.toString(sessionList.indexOf(self)));
			break;
		}
	}

	// 자신을 제외한 사용자들에게 내 채팅내용 보여주기
	private void sendAllSessionToMessage(Session self, String text, String type) throws Exception {
		for (Session session : WebSocketChat.sessionList) {
			if (!self.getId().equals(session.getId())) {
				switch (type) {
				case "chat":
					sendText(session, "message", text, Integer.toString(sessionList.indexOf(self)));
					break;
				case "drawing":
					sendText(session, "image", text, "-1");
					break;
				case "ready":
					if (text.equals("on"))
						sendText(session, "ready", "onOther", Integer.toString(sessionList.indexOf(self)));
					else if (text.equals("off"))
						sendText(session, "ready", "offOther", Integer.toString(sessionList.indexOf(self)));
					else
						sendText(session, "start", "시작합니다!", "-1");
					break;
				case "correct":
					sendText(session, "correct", text, "-1");
					setGameTurn(session);
					break;
				case "answer":
					String answer = text.split("&")[0];
					String category = text.split("&")[1];
					sendText(session, "answer", answer, category);

				}
			}
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) throws Exception {
		// string 형식을 다시 json으로
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(message);
		JSONObject jsonObj = (JSONObject) obj;

		String type = (String) jsonObj.get("type");
		String text = (String) jsonObj.get("text");

		switch (type) {
		case "chat":
			sendText(session, "message", text, Integer.toString(sessionList.indexOf(session)));
			sendAllSessionToMessage(session, text, type);
			break;
		case "drawing":
			sendAllSessionToMessage(session, text, type);
			break;
		case "ready":
			readyState(session, text);
			break;
		case "start":
			setGameTurn(session);
			break;
		case "correct":
			sendText(session, "correct", text, "-1");
			sendAllSessionToMessage(session, text, type);
			setGameTurn(session);
			break;
		case "answer":
			sendAllSessionToMessage(session, text, type);
			break;
		}
	}

	@OnError
	public void onError(Throwable e, Session session) {
		System.out.println(e);
	}

	@OnClose
	public void onClose(Session session) throws Exception {
		logger.info("Session " + session.getId() + " has ended");
		try {
			// 정원이 초과하여 연결 해제시 플래그 원상복귀
			if (overPeople == true) {
				overPeople = false;
				throw new Exception();
			}
			for (Session s : WebSocketChat.sessionList) {
				if (!session.getId().equals(s.getId())) {
					// 퇴장시, 왼쪽으로 밀어넣고, 기존에 남아있는 오른편 자리를 초기화
					sendText(s, "show", "빈 사용자", Integer.toString(sessionList.size() - 1));
					sendText(s, "message", " ", Integer.toString(sessionList.size() - 1));
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		String s = session.getId();
		if (readyList.contains(s))
			readyList.remove(s);
		userList.remove(sessionList.indexOf(session));
		// 레디한채로 종료하는 경우도 readyList에서 제외해준다

		sessionList.remove(session);
		showNickname();
		initReady();
	}

}