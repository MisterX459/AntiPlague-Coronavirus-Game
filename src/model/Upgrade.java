package model;

public abstract class Upgrade {
    private String name;
    private int cost;

    public Upgrade(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }


    public void applyUpgrade(GameModel model) {
//        Virus v = model.getVirus();
//
//        double oldRate = v.getCureRate();
//        double newRate = oldRate * 2.0; // double the cure rate
//        v.setCureRate(newRate);

//        System.out.println("Cure Research started. Cure rate was "
//                + oldRate + ", now " + newRate);
    }

    /**
     * each upgrade might affect the spreadChance in trySpread.
     * Default is no effect (return 1.0).
     */
    public double affectSpreadChance(double currentModifier) {
        return currentModifier;
    }
    public abstract String getDescription();
}