package view;

import model.HighScoreEntry;
import model.HighScoresManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A simple window that displays high scores in a JList.
 */
public class HighScoreView extends JDialog {

    private JList<String> scoreList; //store a string representation

    public HighScoreView(Frame owner, HighScoresManager highScoresManager) {
        super(owner, "High Scores", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("High Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Prepare the JList
        scoreList = new JList<>();
        scoreList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(scoreList);
        add(scrollPane, BorderLayout.CENTER);

        // Load and display the scores
        loadScores(highScoresManager.getHighScores());


        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadScores(List<HighScoreEntry> scores) {
        // Convert each HighScoreEntry to a string for display
        DefaultListModel<String> model = new DefaultListModel<>();
        for (HighScoreEntry entry : scores) {
            model.addElement(entry.toString());
        }
        scoreList.setModel(model);
    }
}