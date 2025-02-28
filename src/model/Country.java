package model;

/**
 * Represents a country in the game world.
 */
public class Country {
    private String name;
    private int population;
    private int infectedCount;
    private boolean lockedDown;
    private int xCoord;
    private int yCoord;
    private int pointsAwarded;

    public Country(String name, int population, int xCoord, int yCoord) {
        this.name = name;
        this.population = population;
        this.infectedCount = 0;
        this.lockedDown = false;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.pointsAwarded = 0;
    }


    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }
    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public int getInfectedCount() {
        return infectedCount;
    }

    public void setInfectedCount(int infectedCount) {
        this.infectedCount = infectedCount;
    }

    public boolean isLockedDown() {
        return lockedDown;
    }

    public void setLockedDown(boolean lockedDown) {
        this.lockedDown = lockedDown;
    }


    public void infectPeople(int amount) {
        this.infectedCount = Math.min(this.infectedCount + amount, this.population);
    }

    public int getPointsAwarded() {
        return pointsAwarded;
    }
    public void setPointsAwarded(int pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }
    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", population=" + population +
                ", infectedCount=" + infectedCount +
                ", lockedDown=" + lockedDown +
                '}';
    }


}