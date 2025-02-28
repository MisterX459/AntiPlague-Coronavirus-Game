package model;

/**
 * Represents the virus data, e.g. infection rate, spread chance, etc.
 */
public class Virus {
    private double infectionRate;   // how quickly infection grows within a country
    private double spreadChance;    // chance to spread to neighbors (future step)
    private double cureRate;        // how quickly infected individuals recover

    public Virus(double infectionRate, double spreadChance,double cureRate) {
        this.infectionRate = infectionRate;
        this.spreadChance = spreadChance;
        this.cureRate = cureRate;
    }

    public double getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(double infectionRate) {
        this.infectionRate = infectionRate;
    }

    public double getSpreadChance() {
        return spreadChance;
    }

    public void setSpreadChance(double spreadChance) {
        this.spreadChance = spreadChance;
    }
    public double getCureRate() {
        return cureRate;
    }

    public void setCureRate(double cureRate) {
        this.cureRate = cureRate;
    }
}