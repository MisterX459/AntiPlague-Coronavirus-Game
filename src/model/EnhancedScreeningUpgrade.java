package model;

public class EnhancedScreeningUpgrade extends Upgrade {

    public EnhancedScreeningUpgrade() {
        super("Enhanced Screening", 30);
    }

    @Override
    public void applyUpgrade(GameModel model) {

    }

    @Override
    public double affectSpreadChance(double currentModifier) {
        // reduce spread chance by 20%
        return currentModifier * 0.8;
    }
    @Override
    public String getDescription() {
        return "Reduces infection spread chance by 20%.";
    }
}