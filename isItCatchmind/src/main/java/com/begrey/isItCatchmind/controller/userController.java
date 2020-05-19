  package com.begrey.isItCatchmind.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.begrey.isItCatchmind.domain.User;

@Controller
@RequestMapping(value="/user")
public class userController {
	
	private String lobbyViewName = "lobby";	
	@RequestMapping(value="/lobby", method = RequestMethod.GET)
	public String form() {
		return lobbyViewName;
	}
	
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public String makeName(User u, HttpSession session) {
		userSession uSession = new userSession(u);
		session.setAttribute("userSession", uSession.getUser().getNickName());
		//System.out.println( org.springframework.core.SpringVersion.getVersion() );
		return lobbyViewName;
	}
}
