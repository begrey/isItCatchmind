package com.begrey.isItCatchmind.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import org.json.simple.JSONObject; // JSON��ü ����
import org.json.simple.parser.JSONParser; // JSON��ü �Ľ�
import org.json.simple.parser.ParseException; // ����ó��
//import javax.websocket.RemoteEndpoint.Basic;
 
@Controller
@ServerEndpoint(value="/websocketChat/{nickname}")
public class WebSocketChat {
    
	private boolean overPeople = false;
	private static int turn;
    private static final List<Session> sessionList=new ArrayList<Session>();
    private static List<String> userList = new ArrayList<String>();
    private static List<String> readyList = new ArrayList<String>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketChat.class);
    public WebSocketChat() {
        // TODO Auto-generated constructor stub
        System.out.println("������(����) ��ü����");
    }
    
    //���� ���� �������ֱ�
    public void setGameTurn() {
    	logger.info("�����ϵ���");
    	if (turn == sessionList.size()) {
    		for(Session s : WebSocketChat.sessionList) {
    			sendText(s, "gameover", "������ ����Ǿ����̴�!", " ");  			
    		}
    		turn = 0;
    		initReady();
    		return;
    	}
    	Session self = sessionList.get(turn);
    	for(Session session : WebSocketChat.sessionList) {
    		if(!self.getId().equals(session.getId())) {
    			sendText(session, "viewer", "���ߴ�����", " ");  	
    		}
    		else {
    			sendText(session, "painter", "�׸�������", " ");  	
    		}
    	}
    	turn++; 	
    }

    //json�������� string�� �����͸� ���� jsp���� �뵵�� �°� �з��Ͽ� ���
    public void sendText(Session s, String type, String text, String div) {
    	try {
			s.getBasicRemote().sendText("{\"type\":\""+type+"\",\"text\":\""+text+"\",\"div\":\""+div+"\"}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @OnOpen
    public void onOpen(Session session, @PathParam("nickname") String nickname) throws Exception {
       logger.info("Open session id:"+Integer.parseInt(session.getId(), 16));
     
        try {
        	//�����ʰ��϶�, alert������ js���� ����������
           if(sessionList.size() == 4) {
        	   overPeople = true;
        	   throw new Exception();
           }
        }catch (Exception e) {
            // TODO: handle exception
        	sendText(session, "alert", "�̹̼��õ��¼��Դϴ�.(�̼���)", "");           

            System.out.println(e.getMessage());
        }
        
        sessionList.add(session);
        userList.add(nickname);
        
        //�� �ο� ���� �����ڿ��� �����ֱ�
        sendText(session, "total", Integer.toString(sessionList.size()), "-1");
        
        //����� ���� : �������� ��ǳ���� �г��� ǥ��
        sendText(session, "show", nickname, Integer.toString(sessionList.indexOf(session)));
        
        for(Session other : WebSocketChat.sessionList) {
            if (!session.getId().equals(other.getId())) {
               logger.info("���� �����ߴ� �̵�:"+userList.get(sessionList.indexOf(other)));
               //�ٸ� ����ڵ鿡�� �� �г��� �����ֱ�
               sendText(other, "show", nickname, Integer.toString(sessionList.indexOf(session)));
               //����� ���� : �ٸ� ����ڵ��� �г��� �����ֱ�
               sendText(session, "show", userList.get(sessionList.indexOf(other)), Integer.toString(sessionList.indexOf(other)));
               //�ٲ� �ο� ��ο��� �����ֱ�
               sendText(other, "total", Integer.toString(sessionList.size()), "-1");
            }
         }
        initReady();
    }
    //� ����ڰ� ������ ��, �������� ���� ����� �о�α�
    private void showNickname() {   	   
        for(Session s : WebSocketChat.sessionList) {    
        	for (int i = 0; i < WebSocketChat.sessionList.size(); i++) {
        		//���� �����鿡�� ���� �ڽ��� �г��Ӱ� ������ �г��� �����ֱ�, ä�� ����� �� ����
        		sendText(s, "show", userList.get(i), Integer.toString(i));
        		sendText(s, " ", userList.get(i), Integer.toString(i));
        	}
        }	    
      
    }
    //� ����ڰ� ������ ��, ���� �ʱ�ȭ�ϱ�
    private void initReady() {
    	for(Session s : WebSocketChat.sessionList) {
    		sendText(s, "ready", "off", Integer.toString(sessionList.indexOf(s)));
    		readyList.remove(s.getId());
    	}
    }
    private void readyState(Session self, String text) {
    	String session = self.getId();
    	switch(text) {
    	case "readyOn" :
    		readyList.add(session);
    		logger.info("���𴩸������:"+readyList.size());
			/*
			 * for (String i : readyList) { logger.info(i); }
			 */
    		sendAllSessionToMessage(self, "on", "ready");
    		sendText(self, "ready", "on", Integer.toString(sessionList.indexOf(self))); 
    		if (readyList.size() == userList.size() && readyList.size() != 1) {
    			sendAllSessionToMessage(self, "start", "ready");
    			sendText(self, "start", "�����մϴ�!", "-1");
    		}
    		
    		break;
    	case "readyOff" :
    		readyList.remove(session);
    		logger.info("���𴩸������:"+readyList.size());
    		sendAllSessionToMessage(self, "off", "ready");
    		sendText(self, "ready", "off", Integer.toString(sessionList.indexOf(self)));
    		break;
    	}
    }
    
    //�ڽ��� ������ ����ڵ鿡�� �� ä�ó��� �����ֱ�
    private void sendAllSessionToMessage(Session self,String text,String type) {      
    	for(Session session : WebSocketChat.sessionList) {
    		if(!self.getId().equals(session.getId())) {
    			switch(type) {
        		case "chat":
        			sendText(session, "message", text, Integer.toString(sessionList.indexOf(self)));                  			
        	        break;
        		case "drawing":
        			sendText(session, "image", text, "-1");
        			break;
        		case "ready":
        			if (text.equals("on"))
        				sendText(session, "ready", "on", Integer.toString(sessionList.indexOf(self))); 
        			else if (text.equals("off")) 	
        				sendText(session, "ready", "off", Integer.toString(sessionList.indexOf(self))); 
        			else
        				sendText(session, "start", "�����մϴ�!", "-1"); 
        			break;
    			}
    		}	
        }
    }
    @OnMessage
    public void onMessage(String message,Session session) throws ParseException {
    	//string ������ �ٽ� json����
    	JSONParser parser = new JSONParser();
    	Object obj = parser.parse(message);
    	JSONObject jsonObj = (JSONObject) obj;
    	
    	String type = (String) jsonObj.get("type");
    	String text = (String) jsonObj.get("text");

    	switch(type) {
    		case "chat":
    			sendText(session, "message", text, Integer.toString(sessionList.indexOf(session)));
    	        sendAllSessionToMessage(session, text, type);
    	        break;
    		case "drawing":
    			sendAllSessionToMessage(session, text, type);
    			break;
    		case "ready" :  
    			readyState(session, text);
    			break;
    		case "start":
    			setGameTurn();
    	}
    }
    
    @OnError
    public void onError(Throwable e,Session session) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@����@@@@@@@@@@@@@@");
    }
    @OnClose
    public void onClose(Session session) {
        logger.info("Session "+session.getId()+" has ended");
        try {
        	//������ �ʰ��Ͽ� ���� ������ �÷��� ���󺹱�
        	if (overPeople == true) {
        		overPeople = false;
              throw new Exception();
        	}
            for(Session s : WebSocketChat.sessionList) {
                if(!session.getId().equals(s.getId())) {
                	//�����, �������� �о�ְ�, ������ �����ִ� ������ �ڸ��� �ʱ�ȭ
                	sendText(s, "show", "�� �����", Integer.toString(sessionList.size()-1));
                	sendText(s, "message", " ", Integer.toString(sessionList.size()-1));
                }
            }
            
        }catch (Exception e) {
            // TODO: handle exception       	
            System.out.println(e.getMessage());
        }

        String s = session.getId();
        if (readyList.contains(s)) readyList.remove(s);
        userList.remove(sessionList.indexOf(session));
        //������ä�� �����ϴ� ��쵵 readyList���� �������ش�
        
        sessionList.remove(session); 
        showNickname(); 
        initReady();
    }
    
}

