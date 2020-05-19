package com.begrey.isItCatchmind.domain;

public class Room {
	private String roomName="error";
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public Room(String roomName) {
		this.roomName = roomName;
	}
	public Room() {
	}
	
	
	
}
