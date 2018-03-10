package com.todo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RefreshToken {

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false, unique = true)
	private String token;

	public RefreshToken() {
	}

	public RefreshToken(String username, String token) {
		this.username = username;
		this.token = token;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
