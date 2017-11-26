package ohtu;

import java.util.HashMap;
import java.util.Map;

public class TennisGame {

    private Player player1;
    private Player player2;

    public TennisGame(String player1Name, String player2Name) {
        this.player1 = new Player(player1Name);
        this.player2 = new Player(player2Name);
    }

    public void wonPoint(String playerName) {
        getPlayer(playerName).addPoint();
    }

    public Player getPlayer(String name) {
        return (player1.getName().equals(name)) ? player1 : player2;
    }

    public String getScore() {
        if (gameIsTied()) {
            return tiedGameScore();
        } else if (gameInTieBreaker()) {
            return tieBreakerScore();
        } else {
            return onGoingScore();
        }
    }

    public boolean gameInTieBreaker() {
        return (player1.getPoints()>=4 || player2.getPoints()>=4);
    }

    public boolean gameIsTied() {
        return (player1.getPoints() == player2.getPoints());
    }

    public String tiedGameScore() {
        return TennisScoreNames.tiedScore(player1.getPoints());
    }

    public Player leadingPlayer() {
        return (player1.getPoints() > player2.getPoints()) ? player1 : player2;
    }

    public String tieBreakerScore() {
        boolean isAdvantage = (Math.abs(player1.getPoints() - player2.getPoints()) == 1);
        String message = isAdvantage ? "Advantage " : "Win for ";
        return message + leadingPlayer().getName();
    }

    public String onGoingScore() {
        String playerOneScore = TennisScoreNames.getScore(player1.getPoints());
        String playerTwoScore = TennisScoreNames.getScore(player2.getPoints());
        return playerOneScore + "-" + playerTwoScore;
    }

}

class Player {

    private String name;
    private int points;

    public Player(String name) {
        this.name = name;
        points = 0;
    }

    public String getName() {
        return this.name;
    }

    public void addPoint() {
        this.points++;
    }

    public int getPoints() {
        return this.points;
    }

}

class TennisScoreNames {

    private static final Map<Integer, String> scoreNames;
    static
    {
        scoreNames = new HashMap<>();
        scoreNames.put(0, "Love");
        scoreNames.put(1, "Fifteen");
        scoreNames.put(2, "Thirty");
        scoreNames.put(3, "Forty");
    }

    public static String getScore(int points) {
        return scoreNames.get(points);
    }

    public static String tiedScore(int points) {
        if (points < 4) {
            return scoreNames.get(points) + "-All";
        } else {
            return "Deuce";
        }
    }
}
