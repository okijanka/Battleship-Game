package com.example.statki;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class Game {

    protected Player leftPlayer;
    protected Player rightPlayer;
    protected AIPlayer bot;
    protected AIPlayer bot1;
    protected AIPlayer bot2;
    protected String time;
    protected boolean isGameOver;
    protected Player winner;
    protected Player loser;
    protected AIPlayer winnerAI;
    protected AIPlayer loserAI;
    protected int idGame;
    public String leftPlayerNick;
    public String rightPlayerNick;
    public int leftPlayerID;
    public int rightPlayerID;
    public String winnerNick;
    public String loserNick;
    public int winnerID;
    public int loserID;

    public Game(Player leftPlayer, Player rightPlayer) {
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
        this.leftPlayerID = leftPlayer.getIdUser();
        this.rightPlayerID = rightPlayer.getIdUser();
        this.isGameOver = false;
    }

    public Game(Player leftPlayer, AIPlayer rightPlayer) {
        this.leftPlayer = leftPlayer;
        this.bot = rightPlayer;
        this.leftPlayerID = leftPlayer.getIdUser();
        this.rightPlayerID = rightPlayer.getIdUser();
        this.isGameOver = false;
    }

    public Game(AIPlayer leftPlayer, AIPlayer rightPlayer) {
        this.bot1 = leftPlayer;
        this.bot2 = rightPlayer;
        this.leftPlayerID = leftPlayer.getIdUser();
        this.rightPlayerID = rightPlayer.getIdUser();
        this.isGameOver = false;
    }

    public Game(int idGame, String leftPlayer, String rightPlayer, String gameTime) {
        this.idGame = idGame;
        this.time = gameTime;
        this.leftPlayerNick = leftPlayer;
        this.rightPlayerNick = rightPlayer;
        this.isGameOver = false;
    }

    public void startGame() {
        getCurrentTime();
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection conn = connectNow.getConnection();
        String insertFields = "INSERT INTO games(idWhoWon, idWhoLost, gameTime) VALUES ('" +
                leftPlayerID + "','" +
                rightPlayerID + "','" +
                time + "'); ";
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(insertFields);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getClass();
        }
        System.out.println(leftPlayer);
        System.out.println(rightPlayer);
        setIdGame();
        System.out.println(idGame);
    }

    public void deleteGame() {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection conn = connectNow.getConnection();
        String insertFields = "DELETE FROM games WHERE idGame = '" +
                idGame + "'; ";
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(insertFields);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getClass();
        }
    }

    public void getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        this.time = dtf.format(now);
        System.out.println(time);
    }

    public void setWinner(Player winner) {
        this.winner = winner;
        this.winnerNick = winner.getNick();
        this.winnerID = winner.getIdUser();
    }

    public void setLoser(Player loser) {
        this.loser = loser;
        this.loserNick = loser.getNick();
        this.loserID = loser.getIdUser();
    }

    public void setWinner(AIPlayer winner) {
        this.winnerAI = winner;
        this.winnerNick = winner.getNick();
        this.winnerID = winner.getIdUser();
    }

    public void setLoser(AIPlayer loser) {
        this.loserAI = loser;
        this.loserNick = loser.getNick();
        this.loserID = loser.getIdUser();
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public Player getLeftPlayer() {
        return leftPlayer;
    }

    public String getWinnerNick() {
        return winnerNick;
    }

    public String getLoserNick() {
        return loserNick;
    }

    public int getWinnerID() {
        return winnerID;
    }

    public int getLoserID() {
        return loserID;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getLoser() {
        return loser;
    }

    public void setIdGame() {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String StringQuery = "SELECT idGame FROM games WHERE gameTime = '" + time + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(StringQuery);
            if (queryResult.next()) {
                idGame = queryResult.getInt("idGame");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getClass();
        }
    }


    public void setMove(int xRow, int yColumn, int idUser, boolean hit, boolean shipDestroyed) {
        DatabaseConnector connectNow = new DatabaseConnector();
        String insertFields = "INSERT INTO moves(idGame, xRow, yColumn, idWhoMoved, hit, shipDestroyed) VALUES ('" +
                idGame + "','" +
                xRow + "','" +
                yColumn + "','" +
                idUser + "','" +
                changeBoolToInt(hit) + "','" +
                changeBoolToInt(shipDestroyed) + "')";

        try (Connection conn = connectNow.getConnection(); PreparedStatement pstmt = conn.prepareStatement(insertFields)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateWinnerAndLoser() {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection conn = connectNow.getConnection();
        String updateGames = "UPDATE games SET idWhoWon = " + winnerID + ", idWhoLost = " + loserID + " WHERE idGame = " + idGame + "";
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(updateGames);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getClass();
        }
    }

    public void setStatistics(Player player) {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection conn = connectNow.getConnection();
        String updateGames = "UPDATE statistics SET numberOfGames = " + player.getNumberOfGames() + ", wonGames = "
                + player.getWonGames() + ", lostGames = " + player.getLostGames() + ", accuracy = " + player.getAccuracy() + ", numberOfAllShots = " + player.getNumberOfAllShots() +
                ", numberOfHitShots = " + player.getNumberOfHitShots() + " WHERE idUser = " + player.getIdUser() + "";
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(updateGames);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getClass();
        }
    }

    public int changeBoolToInt(boolean data) {
        if (data) {
            return 1;
        } else {
            return 0;
        }
    }
}
