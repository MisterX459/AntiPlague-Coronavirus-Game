package model;

public class FasterTestingUpgrade extends Upgrade {

    public FasterTestingUpgrade() {
        super("Faster Testing", 35);
    }

    @Override
    public void applyUpgrade(GameModel model) {
        Virus v = model.getVirus();
        double oldCureRate = v.getCureRate();
        v.setCureRate(oldCureRate * 1.25); // +25%
    }
    @Override
    public String getDescription() {
        return "Increases cure rate by 25%.";
    }
}