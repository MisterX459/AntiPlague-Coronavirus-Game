package model;

import controller.GameController;

import javax.swing.*;

public class VaccineUpgrade extends Upgrade {

    public VaccineUpgrade() {
        super("Vaccine Release", 75);
    }

    @Override
    public void applyUpgrade(GameModel model) {
        // Heal up to 100 infected people total, across all countries.
        int remainingToCure = 100;

        for (Country country : model.getCountries()) {
            if (remainingToCure <= 0) break;

            int infected = country.getInfectedCount();
            if (infected > 0) {
                // Cure as many as we can in this country
                int cureAmount = Math.min(remainingToCure, infected);
                country.setInfectedCount(infected - cureAmount);
                remainingToCure -= cureAmount;
            }
        }
        if (model.isAllCured()) {
            // Notify the controller or UI about the win
            JOptionPane.showMessageDialog(
                    null,
                    "Congratulations! You saved humanity with the vaccine!",
                    "Victory",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // You may need to invoke a method in the controller to stop the game
            GameController gameController = model.getGameController(); // Ensure this reference exists
            if (gameController != null) {
                gameController.handleWin();
            }
        }
    }
    @Override
    public String getDescription() {
        return "Releases a vaccine that instantly cures 100 total infected people worldwide.";
    }
}