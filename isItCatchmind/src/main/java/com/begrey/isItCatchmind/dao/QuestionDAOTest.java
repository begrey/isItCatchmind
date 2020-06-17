package com.begrey.isItCatchmind.dao;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.begrey.isItCatchmind.dao.QuestionDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/*.xml" })
public class QuestionDAOTest {
	@Inject
	private QuestionDAO dao;

	@Test
	public void testTime() throws Exception {
		System.out.println("Á¤´ä : " + dao.showAnswer());
	}
}
