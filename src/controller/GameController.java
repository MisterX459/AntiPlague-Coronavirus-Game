package controller;

import model.*;
import view.GameView;
import view.MainMenuView;
import view.ShopView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Manages the GameView and updates the time counter in the model,
 * plus a global shortcut Ctrl+Shift+Q to return to the main menu.
 */
public class GameController implements SpreadListener{

    private final GameModel model;
    private final GameView view;
    private Timer timer;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        model.setGameController(this);
        // 1) TELL THE MODEL that GameController are the spreadListener
        this.model.setSpreadListener(this);
        updateView();
        setupGlobalShortcut(); // Ctrl+Shift+Q
        startTimer();
        startFiveMinutePrompt();
        view.getShopButton().addActionListener(e -> openShop());
        setupCountryButtonListeners();

        setupCountryButtons();
        startGameLoop();

    }
    private void setupCountryButtons() {
        List<Country> countries = model.getCountries();
        view.addCountryButtons(countries, country -> {
            // Show dialog with infection details
            JOptionPane.showMessageDialog(view,
                    "Country: " + country.getName() +
                            "\nInfected: " + country.getInfectedCount() +
                            "\nHealthy: " + (country.getPopulation() - country.getInfectedCount()),
                    "Country Details",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void startGameLoop() {
        timer = new Timer(1000, e -> {
            model.incrementTime();
            model.updatePoints();
            view.setTimeLabel(model.getTimeInSeconds());
            view.setPointsLabel(model.getPoints());
            view.setTotalInfectedLabel(model.getTotalInfected());
            view.setTotalHealthyLabel(model.getTotalHealthy());
            model.updateInfection();
            checkWinLose();
        });
        timer.start();
    }
    private void checkWinLose() {
        // Win?
        if (model.isAllCured()) {
           
            // stop the game
            stopTimer();
            handleWin();
        }
        // Lose?
        else if (model.isAllInfected()) {
            // stop the game
            stopTimer();
            handleLose();
        }
    }
    public void handleWin() {
       stop();
        JOptionPane.showMessageDialog(
                view,
                "You saved humanity!",
                "Victory",
                JOptionPane.INFORMATION_MESSAGE
        );

        promptForHighScoreAndReturn();
    }

    private void handleLose() {
        JOptionPane.showMessageDialog(
                view,
                "Game Over! Everyone is infected.",
                "Defeat",
                JOptionPane.ERROR_MESSAGE
        );
        promptForHighScoreAndReturn();
    }
    private void promptForHighScoreAndReturn() {
        // 1) Ask player name
        String playerName = JOptionPane.showInputDialog(
                view,
                "Enter your name for the high scores:",
                "High Scores",
                JOptionPane.QUESTION_MESSAGE
        );
        if (playerName != null && !playerName.trim().isEmpty()) {
            // 2) Create HighScoreEntry and store it
            int finalScore = model.getPoints();
            int time = model.getTimeInSeconds();
            GameModel.Difficulty diff = model.getDifficulty();
            HighScoreEntry entry = new HighScoreEntry(playerName, finalScore, time, diff);


            model.getHighScoresManager().addHighScore(entry);


            JOptionPane.showMessageDialog(
                    view,
                    "Score saved! (" + finalScore + " points)",
                    "High Scores",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }


        view.dispose(); // close the game window
        openMainMenu();
    }
    private void openMainMenu() {
        MainMenuView mainMenuView = new MainMenuView();
        MainMenuController mainMenuController = new MainMenuController(new GameModel(), mainMenuView);
        mainMenuView.setVisible(true);
    }




    private void openShop() {

        ShopView shopView = new ShopView(view, model);
        shopView.setVisible(true);
    }
    public void stop() {
        if (timer != null) timer.stop();
    }
    @Override
    public void onAirSpread(Country fromCountry, Country toCountry) {
        animateTransport(fromCountry, toCountry, TransportType.AIR);
    }

    @Override
    public void onLandSpread(Country fromCountry, Country toCountry) {
        animateTransport(fromCountry, toCountry, TransportType.LAND);
    }

    @Override
    public void onSeaSpread(Country fromCountry, Country toCountry) {
        animateTransport(fromCountry, toCountry, TransportType.SEA);
    }
    private final Map<TransportType, String> transportImagePaths = Map.of(
            TransportType.AIR, "/images/plane2.0.png",
            TransportType.LAND, "/images/car2.0.png",
            TransportType.SEA, "/images/boat.png"
    );

    private void animateTransport(Country from, Country to, TransportType type) {

        String transportImagePath = transportImagePaths.get(type);
        if (transportImagePath == null) {
            System.err.println("Unknown transport type: " + type);
            return;
        }


        URL resource = getClass().getResource(transportImagePath);
        if (resource == null) {
            System.err.println("Resource not found: " + transportImagePath);
            return;
        }

        JLabel transportLabel = new JLabel(new ImageIcon(resource));
        transportLabel.setSize(32, 32);

        // 2) Starting position
        int startX = from.getXCoord();
        int startY = from.getYCoord();
        transportLabel.setLocation(startX, startY);

        // 3) Add to the layeredPane, above the country buttons
        view.getLayeredPane().add(transportLabel, Integer.valueOf(2));

        // 4) Animate in ~50 steps, 20ms each => ~1 second total
        final int steps = 50;
        final int delayMs = 20;
        final int dx = to.getXCoord() - startX;
        final int dy = to.getYCoord() - startY;

        Timer animTimer = new Timer(delayMs, null);
        final int[] count = {0};
        animTimer.addActionListener(e -> {
            count[0]++;
            double progress = (double) count[0] / steps; // from 0.0 to 1.0
            int newX = (int) (startX + dx * progress);
            int newY = (int) (startY + dy * progress);

            transportLabel.setLocation(newX, newY);

            if (count[0] >= steps) {
                animTimer.stop();
                // Remove the transport icon from the pane
                view.getLayeredPane().remove(transportLabel);
                // Force a redraw
                view.getLayeredPane().repaint();
            }
        });
        animTimer.start();
    }

    /**
     * Initialize a Swing Timer to tick every 1000 ms (1 second).
     */
    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Increment the time in model
                model.incrementTime();
                // Update the GameView
                view.setTimeLabel(model.getTimeInSeconds());
                // 2) Update infection in origin country
                model.updateInfection();
            }
        });
        timer.start();
    }

    /**
     * Stop the timer if needed (e.g., on game exit).
     */
    public void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        maybeSaveHighScore();
    }
    /**
     * Prompt the user to enter a name and save to highscores if they choose.
     */
    private void startFiveMinutePrompt() {
        Timer promptTimer = new Timer(300_000, e -> { // 300_000 ms = 5 minutes
            int response = JOptionPane.showConfirmDialog(
                    view,
                    "Five minutes have passed. Do you want to continue playing?",
                    "Continue Playing?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(
                        view,
                        "You chose not to continue. Game over!",
                        "Game Over",
                        JOptionPane.INFORMATION_MESSAGE
                );
                handleLose();
            }
        });
        promptTimer.setRepeats(false);
        promptTimer.start();
    }
    private void maybeSaveHighScore() {
        int response = JOptionPane.showConfirmDialog(
                view,
                "Do you want to save your high score?",
                "Save Score",
                JOptionPane.YES_NO_OPTION
        );
        if (response == JOptionPane.YES_OPTION) {
            String name = JOptionPane.showInputDialog(view, "Enter your name:");
            if (name != null && !name.trim().isEmpty()) {
                // Gather data
                int finalScore = model.getPoints();
                int timePlayed = model.getTimeInSeconds();
                GameModel.Difficulty diff = model.getDifficulty();


                HighScoreEntry entry = new HighScoreEntry(name, finalScore, timePlayed, diff);

                model.getHighScoresManager().addHighScore(entry);

                JOptionPane.showMessageDialog(view,
                        "Your score has been saved!",
                        "High Scores",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Update the GameView's labels based on the model (time, points, etc.).
     */
    private void updateView() {
        view.setTimeLabel(model.getTimeInSeconds());
        view.setPointsLabel(model.getPoints());
    }

    /**
     * Sets up a global shortcut Ctrl+Shift+Q.
     * When pressed, it stops the game, disposes the GameView,
     * and reopens the Main Menu.
     */
    private void setupGlobalShortcut() {

        JRootPane rootPane = view.getRootPane();

        // Input map / action map for "WHEN_IN_FOCUSED_WINDOW"
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        //  KeyStroke for "control shift Q"
        KeyStroke keyStroke = KeyStroke.getKeyStroke("control shift Q");

        // Bind the KeyStroke to a command string RETURN_TO_MENU
        inputMap.put(keyStroke, "RETURN_TO_MENU");

        // Create the action that will be executed
        actionMap.put("RETURN_TO_MENU", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                stopTimer();


                view.dispose();


                MainMenuView mainMenuView = new MainMenuView();
                MainMenuController mainMenuController =
                        new MainMenuController(new GameModel(), mainMenuView);


                mainMenuView.setVisible(true);

                System.out.println("Ctrl+Shift+Q pressed. Returning to Main Menu...");
            }
        });
    }

    private void setupCountryButtonListeners() {
        List<JButton> buttons = view.getCountryButtons();
        List<Country> countries = model.getCountries();


        if (buttons.size() != countries.size()) {
            return;
        }

        for (int i = 0; i < buttons.size(); i++) {
            final int index = i;
            JButton btn = buttons.get(i);
            Country c = countries.get(i);

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    JOptionPane.showMessageDialog(
                            view,
                            "Country: " + c.getName()
                                    + "\nInfected: " + c.getInfectedCount()
                                    + "\nHealthy: " + (c.getPopulation() - c.getInfectedCount()),
                            "Country Details",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            });
        }
    }

}