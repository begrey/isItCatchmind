package com.begrey.isItCatchmind.domain;

public class User {
	private String nickName ="dd";
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public User(String nickName) {
		this.nickName = nickName;
	}

	public User() {
	}
	
}
