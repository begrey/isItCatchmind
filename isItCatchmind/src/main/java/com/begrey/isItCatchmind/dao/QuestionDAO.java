package com.begrey.isItCatchmind.dao;
import java.util.List;

import com.begrey.isItCatchmind.domain.Question;

public interface QuestionDAO {

   public List<Question> showAnswer() throws Exception;

}