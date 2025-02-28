package model;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A single high score record, storing player's name, score, time, difficulty, etc.
 */
public class HighScoreEntry implements Serializable {
    private static final long serialVersionUID = 1L; // for serialization

    private String playerName;
    private int score;
    private int timeInSeconds;
    private GameModel.Difficulty difficulty;
    private LocalDateTime dateAchieved;

    public HighScoreEntry(String playerName, int score, int timeInSeconds,
                          GameModel.Difficulty difficulty) {
        this.playerName = playerName;
        this.score = score;
        this.timeInSeconds = timeInSeconds;
        this.difficulty = difficulty;
        this.dateAchieved = LocalDateTime.now();
    }


    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public GameModel.Difficulty getDifficulty() {
        return difficulty;
    }

    public LocalDateTime getDateAchieved() {
        return dateAchieved;
    }

    @Override
    public String toString() {
        return String.format("%s | Score: %d | Time: %ds | Diff: %s",
                playerName, score, timeInSeconds, difficulty);
    }
}