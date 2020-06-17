package com.begrey.isItCatchmind.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.begrey.isItCatchmind.domain.Question;

@Repository
public class QuestionDAOImpl implements QuestionDAO {
   @Inject
   private SqlSession sqlSession;
   
   private static final String namespace = "com.begrey.isItCatchmind.questionMapper";
   
   @Override
   public List<Question> showAnswer() {
      return sqlSession.selectList(namespace + ".showAnswer");
   }
   
}