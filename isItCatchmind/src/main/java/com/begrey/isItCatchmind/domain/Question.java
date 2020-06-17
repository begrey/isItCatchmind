package com.begrey.isItCatchmind.domain;

public class Question {
	private int q_id;
	private String answer;
	private String category;
	public int getQ_id() {
		return q_id;
	}
	public void setQ_id(int q_id) {
		this.q_id = q_id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "Question [q_id=" + q_id + ", answer=" + answer + ", category=" + category + "]";
	}
	
	
}
