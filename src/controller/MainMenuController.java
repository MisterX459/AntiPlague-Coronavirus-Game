package controller;

import model.GameModel;
import view.DifficultySelectionView;
import view.HighScoreView;
import view.MainMenuView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainMenuController {

    private final GameModel model;
    private final MainMenuView view;

    public MainMenuController(GameModel model, MainMenuView view) {
        this.model = model;
        this.view = view;


        initController();
    }

    private void initController() {

        view.getNewGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DifficultySelectionView difficultyView = new DifficultySelectionView(view);

                DifficultySelectionController diffController
                        = new DifficultySelectionController(model, difficultyView);


                difficultyView.setVisible(true);
            }
        });

        view.getHighScoresButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                HighScoreView highScoreView = new HighScoreView(view, model.getHighScoresManager());
                highScoreView.setVisible(true);
            }
        });


        view.getExitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}