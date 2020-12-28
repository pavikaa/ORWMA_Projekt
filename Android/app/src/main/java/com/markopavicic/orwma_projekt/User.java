package com.markopavicic.orwma_projekt;

public class User {
    public String fullName;
    public String username;
    public String password;

    public User() {

    }

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
