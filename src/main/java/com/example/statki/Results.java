package com.example.statki;

public class Results {

    private String login;
    private String numberOfGames;
    private String wonGames;
    private String lostGames;
    private String accuracy;

    public Results( String login, String numberOfGames, String wonGames, String lostGames,String accuracy) {
        this.login = login;
        this.numberOfGames = numberOfGames;
        this.wonGames = wonGames;
        this.lostGames = lostGames;
        this.accuracy = accuracy;
    }

    public String getLogin() {
        return login;
    }

    public String getNumberOfGames() {
        return numberOfGames;
    }

    public String getWonGames() {
        return wonGames;
    }

    public String getLostGames() {
        return lostGames;
    }

    public String getAccuracy() {
        return accuracy;
    }

}
