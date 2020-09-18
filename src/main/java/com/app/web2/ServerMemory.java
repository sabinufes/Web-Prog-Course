package com.app.web2;

public class ServerMemory {
    private String username;
    private int score;
    private int nr1;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNr1() {
        return nr1;
    }

    public void setNr1(int nr1) {
        this.nr1 = nr1;
    }

    public int getNr2() {
        return nr2;
    }

    public ServerMemory(String username, int score, int nr1, int nr2) {
        this.username = username;
        this.score = score;
        this.nr1 = nr1;
        this.nr2 = nr2;
    }

    public void setNr2(int nr2) {
        this.nr2 = nr2;
    }

    private int nr2;
}

