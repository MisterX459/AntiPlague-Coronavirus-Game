package model;

public class CureResearchUpgrade extends Upgrade {

    public CureResearchUpgrade() {
        super("Cure Research", 70);
    }

    @Override
    public void applyUpgrade(GameModel model) {
//          // Double the cure rate
//        Virus v = model.getVirus();
//        double oldCureRate = v.getCureRate();
//        v.setCureRate(oldCureRate * 2.0);
        Virus v = model.getVirus();
        double oldRate = v.getCureRate();
        v.setCureRate(oldRate * (model.getDifficulty() == GameModel.Difficulty.EASY ? 3.0 : 2.0)); // Triple cure rate in Easy mode
        model.addPoints(50); // Award points for using the upgrade
    }
    @Override
    public String getDescription() {
        return "Doubles cure rate, speeding up recovery of infected people.";
    }
}