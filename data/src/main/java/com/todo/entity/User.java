package com.todo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
public class User {

	@Id
	@GeneratedValue
	private int id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@OneToMany(mappedBy = "user")
	@JsonManagedReference
	private List<Todo> todos;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<RefreshToken> refreshTokens;

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(String username, String password, List<Todo> todos) {
		this.username = username;
		this.password = password;
		this.todos = todos;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Todo> getTodos() {
		return todos;
	}

	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}

	public List<RefreshToken> getRefreshTokens() {
		return refreshTokens;
	}

	public void setRefreshTokens(List<RefreshToken> refreshTokens) {
		this.refreshTokens = refreshTokens;
	}
}
