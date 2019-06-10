package com.todo.entity;

import javax.persistence.*;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    public RefreshToken() {
    }

    public RefreshToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
