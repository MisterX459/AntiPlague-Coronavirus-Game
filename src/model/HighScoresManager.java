package model;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manages a list of HighScoreEntry, loading/saving via serialization.
 */
public class HighScoresManager {

    private static final String HIGHSCORES_FILE = "highscores.txt";
    private List<HighScoreEntry> highScores;

    public HighScoresManager() {
        // Load existing high scores if file exists; otherwise create an empty list
        highScores = loadHighScores();
    }

    /**
     * Adds a new HighScoreEntry, then saves to disk.
     */
    public void addHighScore(HighScoreEntry entry) {
        highScores.add(entry);
        saveHighScores(highScores);
    }

    /**
     * Returns the list of high scores, optionally sorted by some logic.
     */
    public List<HighScoreEntry> getHighScores() {
        // Sort by score descending, then time ascending (example)
        highScores.sort(Comparator
                .comparingInt(HighScoreEntry::getScore).reversed()
                .thenComparingInt(HighScoreEntry::getTimeInSeconds));
        return highScores;
    }

    /**
     * Saves the list of HighScoreEntry to a file (highscores.txt).
     */
    private void saveHighScores(List<HighScoreEntry> scores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(HIGHSCORES_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error saving high scores:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Loads the list of HighScoreEntry from a file, or returns an empty list if none found.
     */

    private List<HighScoreEntry> loadHighScores() {
        File file = new File(HIGHSCORES_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (List<HighScoreEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Error loading high scores:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
}