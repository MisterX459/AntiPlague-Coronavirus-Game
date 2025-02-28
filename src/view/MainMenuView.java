package view;

import javax.swing.*;
import java.awt.*;

/**
 * Main Menu window with three buttons: New Game, High Scores, and Exit.
 */
public class MainMenuView extends JFrame {

    private JButton newGameButton;
    private JButton highScoresButton;
    private JButton exitButton;

    public MainMenuView() {
        super("AntiPlague - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        // ----- Components -----
        JLabel titleLabel = new JLabel("Welcome to AntiPlague!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        newGameButton = new JButton("New Game");
        highScoresButton = new JButton("High Scores");
        exitButton = new JButton("Exit");

        // ----- Layout -----
        setLayout(new BorderLayout());

        // Top panel for title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for buttons
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        centerPanel.setBackground(Color.LIGHT_GRAY);
        centerPanel.add(newGameButton);
        centerPanel.add(highScoresButton);
        centerPanel.add(exitButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JButton getHighScoresButton() {
        return highScoresButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}