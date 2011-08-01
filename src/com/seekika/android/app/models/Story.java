package com.seekika.android.app.models;

import java.util.Date;

public class Story {
	
	private int id;
	private String title;
	private String description;
	private String userKey;
	private String created_on;
	private String status;
	private String lat;
	private String lon;
	private String deviceID;
	private String fileName;
	private String createdOn;
	private String fileSize;
	private int uploaded;
	private String storyKey;
	
	public Story(){}
	
	
	
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
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public int getUploaded() {
		return uploaded;
	}



	public void setUploaded(int uploaded) {
		this.uploaded = uploaded;
	}



	public String getStoryKey() {
		return storyKey;
	}



	public void setStoryKey(String storyKey) {
		this.storyKey = storyKey;
	}
	
}
