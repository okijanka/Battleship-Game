package com.example.statki;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Player {

    protected String nick;
    protected int idUser;
    protected boolean isMyTurn;
    protected int numberOfGames;
    protected int wonGames;
    protected int lostGames;
    protected int accuracy;
    protected int numberOfAllShots;
    protected int numberOfHitShots;
    protected Battlefield battlefield;

    public Player(String nick, int idUser) {
        this.nick = nick;
        this.idUser = idUser;
        this.isMyTurn = true;
        setNumberOfHitShotsAndAllShots();
        setAccuracy();
        System.out.println(accuracy);
        System.out.println(numberOfHitShots);
        System.out.println(numberOfAllShots);
    }

    public Player(int idUser) {
        this.nick = getPlayerNick(idUser);
        this.idUser = idUser;
        this.isMyTurn = true;
        setNumberOfHitShotsAndAllShots();
        setAccuracy();
        System.out.println(accuracy);
        System.out.println(numberOfHitShots);
        System.out.println(numberOfAllShots);
    }


    public void setNumberOfHitShotsAndAllShots() {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String StringQuery = "SELECT numberOfGames, wonGames, lostGames, numberOfAllShots, numberOfHitShots FROM statistics WHERE idUser = '" + idUser + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(StringQuery);
            if (queryResult.next()) {
                numberOfGames = queryResult.getInt("numberOfGames");
                wonGames = queryResult.getInt("wonGames");
                lostGames = queryResult.getInt("lostGames");
                numberOfHitShots = queryResult.getInt("numberOfAllShots");
                numberOfAllShots = queryResult.getInt("numberOfHitShots");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getClass();
        }
    }

    public static String getPlayerNick(int idUser) {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();

        String StringQuery = "SELECT login FROM Users WHERE idUser = " + idUser + "";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(StringQuery);
            if (queryResult.next()) {
                String result = queryResult.getString("login");
                return  result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            e.getClass();
            return null;
        }
    }

    public String getNick() {
        return nick;
    }

    public int getIdUser() {
        return idUser;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public int getWonGames() {
        return wonGames;
    }

    public int getLostGames() {
        return lostGames;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getNumberOfAllShots() {
        return numberOfAllShots;
    }

    public int getNumberOfHitShots() {
        return numberOfHitShots;
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public void setAccuracy() {
        if (numberOfAllShots != 0) {
            accuracy = (int) Math.round(100 * numberOfHitShots/numberOfAllShots);
        } else {
            accuracy = 0;
        }
        System.out.println(accuracy);
    }

    public void incrementNumberOfAllShots() {
        this.numberOfAllShots += 1;
    }

    public void incrementNumberOfHitShots() {
        this.numberOfHitShots += 1;
    }

    public void incrementNumberOfGames() {
        this.numberOfGames += 1;
    }

    public void incrementWonGames() {
        this.wonGames += 1;
    }

    public void incrementLostGames() {
        this.lostGames += 1;
    }


}
