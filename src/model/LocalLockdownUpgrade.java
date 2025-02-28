package model;

public  class LocalLockdownUpgrade extends Upgrade {

    public LocalLockdownUpgrade() {
        super("Local Lockdown", 60);
    }

    @Override
    public void applyUpgrade(GameModel model) {
        // Reduce infection rate by half
        Virus v = model.getVirus();
        double oldRate = v.getInfectionRate();
        v.setInfectionRate(oldRate * 0.5);
        model.closeAllSeaRoutes();
    }

    @Override
    public double affectSpreadChance(double currentModifier) {
        // no further effect on spread chance
        return currentModifier;
    }

    @Override
    public String getDescription() {
        return "Halves infection rate & closes all SEA routes.";
    }
}