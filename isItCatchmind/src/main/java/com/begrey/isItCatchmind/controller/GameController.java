package com.begrey.isItCatchmind.controller;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import java.util.List;
import java.util.Locale;
 
import javax.inject.Inject;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.begrey.isItCatchmind.domain.Question;
import com.begrey.isItCatchmind.service.QuestionService;

@Controller
public class GameController {
	private String roomViewName = "room";
	private static final Logger logger = LoggerFactory.getLogger(GameController.class);
	@Inject
	private QuestionService service;
	
	
	@RequestMapping(value="/game")
	public String playGame(Model model) throws Exception {
		
		
		List<Question> questionList = service.showAnswer();
    	Question q = questionList.get(0);
    	String answer = q.getAnswer();
    	String category = q.getCategory();
		logger.info("«Ï«Ï" + answer + " " + category);
		model.addAttribute("questionList", questionList);
		return roomViewName;
	}

}
