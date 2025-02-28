package model;

public class TravelRestrictionsUpgrade extends Upgrade {

    public TravelRestrictionsUpgrade() {
        super("Travel Restrictions", 40);
    }

    @Override
    public void applyUpgrade(GameModel model) {
        // Immediately close all LAND routes:
        model.closeAllLANDRoutes();
    }

    @Override
    public double affectSpreadChance(double currentModifier) {
        //  cut spread chance in half
        return currentModifier * 0.5;
    }
    @Override
    public String getDescription() {
        return "Halves infection spread chance & closes all LAND routes.";
    }
}