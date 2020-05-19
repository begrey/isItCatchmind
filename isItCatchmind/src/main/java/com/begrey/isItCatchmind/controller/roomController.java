package com.begrey.isItCatchmind.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.begrey.isItCatchmind.domain.Room;

@Controller
@RequestMapping(value="/room")
public class roomController {
	
	private String roomViewName = "room";	
	private String roomFormName = "create";
	private String lobbyViewName = "lobby";
	private String gameViewName = "game";
	@RequestMapping(value="/view")
	public String showRoom() {
		return roomViewName;
	}
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public String form() {
		return roomFormName;
	}
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public String makeRoom(Room r, HttpSession session) {
		//String userNn = (String)session.getAttribute("userSession");
		return lobbyViewName;
	}
	@RequestMapping(value="/addUser")
	public String gogo(){
		return roomViewName;
	}
	@RequestMapping(value="/game")
	public String playGame() {
		return gameViewName;
	}
	@RequestMapping(value="/lobby")
	public String returnLobby() {
		return lobbyViewName;
	}

}
