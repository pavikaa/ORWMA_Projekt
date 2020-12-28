package com.markopavicic.orwma_projekt;

public class Team {
    public String name;
    public String password;

    public Team() {

    }

    public Team(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}