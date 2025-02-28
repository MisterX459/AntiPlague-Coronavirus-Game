package model;

public class InternationalAidUpgrade extends Upgrade {

    public InternationalAidUpgrade() {
        super("International Aid", 45);
    }

    @Override
    public void applyUpgrade(GameModel model) {
        // Immediately reduce infected in each country by 5%
        for (Country c : model.getCountries()) {
            int infected = c.getInfectedCount();
            int reduced = (int)Math.floor(infected * 0.05); // 5%
            c.setInfectedCount(Math.max(0, infected - reduced));
        }
    }
    @Override
    public String getDescription() {
        return "Reduces the infected count in every country by 5%.";
    }
}