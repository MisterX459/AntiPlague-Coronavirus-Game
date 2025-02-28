package controller;

import model.GameModel;
import view.DifficultySelectionView;
import model.GameModel.Difficulty;

import view.GameView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles user input in the DifficultySelectionView.
 * Stores the chosen difficulty in the GameModel,
 * closes the dialog, and opens the GameView.
 */
public class DifficultySelectionController {

    private final GameModel model;
    private final DifficultySelectionView difficultyView;

    public DifficultySelectionController(GameModel model, DifficultySelectionView difficultyView) {
        this.model = model;
        this.difficultyView = difficultyView;
        initController();
    }

    private void initController() {
        difficultyView.getConfirmButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1) Determine selected difficulty
                Difficulty chosenDifficulty = getSelectedDifficulty();
                model.setDifficulty(chosenDifficulty);
                // 2) Initialize the virus and origin
                model.initVirusAndOrigin();

                // 3) Close the difficulty selection dialog
                difficultyView.dispose();

                // 4) Open the Game Window (and start the game controller)
                openGameWindow();
            }
        });
    }

    private Difficulty getSelectedDifficulty() {
        if (difficultyView.isEasySelected()) {
            return Difficulty.EASY;
        } else if (difficultyView.isMediumSelected()) {
            return Difficulty.MEDIUM;
        } else {
            return Difficulty.HARD;
        }
    }

    private void openGameWindow() {

        GameView gameView = new GameView(model);
        gameView.setVisible(true);

        // Create the GameController so time starts updating
        GameController gameController = new GameController(model, gameView);

    }
}