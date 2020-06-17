package com.begrey.isItCatchmind.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.begrey.isItCatchmind.dao.QuestionDAO;
import com.begrey.isItCatchmind.domain.Question;

@Service
public class QuestionServiceImpl implements QuestionService {

   @Inject
   private QuestionDAO dao;
   
   @Override
   public List<Question> showAnswer() throws Exception {
      return dao.showAnswer();
   }
}