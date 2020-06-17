package com.begrey.isItCatchmind.controller;

import com.begrey.isItCatchmind.domain.User;

public class userSession {
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public userSession(User user) {
		this.user = user;
	}
}
