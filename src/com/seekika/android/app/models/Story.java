package com.seekika.android.app.models;

public class Story {
	
	private int id;
	private String title;
	private String userId;
	private String created_on;
	private String status;
	private String lat;
	private String lon;
	
	public Story(int id, String title, String userId, String created_on,
			String status) {
		super();
		this.id = id;
		this.title = title;
		this.userId = userId;
		this.created_on = created_on;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCreated_on() {
		return created_on;
	}
	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
	
}
