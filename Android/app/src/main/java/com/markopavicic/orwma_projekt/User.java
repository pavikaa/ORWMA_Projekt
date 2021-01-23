package com.markopavicic.orwma_projekt;

public class User {
    public String fullName;
    public String username;

    public User() {

    }

    public User(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
