package model;

/**
 * Increases cure speed or eventually reduces infection.
 */
public class MedicalSupportUpgrade extends Upgrade {

    public MedicalSupportUpgrade() {
        super("Medical Support", 50);
    }

    @Override
    public void applyUpgrade(GameModel model) {
        // reduce infection rate by 25%:
        Virus v = model.getVirus();
        double oldRate = v.getInfectionRate();
        v.setInfectionRate(oldRate * 0.75);
    }
    @Override
    public String getDescription() {
        return "Reduces the virus infection rate by 25%.";
    }
}