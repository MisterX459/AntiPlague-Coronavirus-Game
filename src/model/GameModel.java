package model;

import controller.GameController;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class holds game data: difficulty, time, points, etc.
 */
public class GameModel {



    public enum Difficulty {
        EASY, MEDIUM, HARD
    }


    private Difficulty difficulty;


    private int timeInSeconds;
    private int points;

    private List<Country> countries;

    private Virus virus;
    private Country originCountry;
    private List<TransportRoute> routes;
    private SpreadListener spreadListener;
    private List<Upgrade> purchasedUpgrades;
    private HighScoresManager highScoresManager;
    private GameController gameController;
    public void setSpreadListener(SpreadListener listener) {
        this.spreadListener = listener;
    }
    public GameModel() {
        this.difficulty = null;
        this.timeInSeconds = 0;
        this.points = 0;
        this.countries = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.purchasedUpgrades = new ArrayList<>();
        this.highScoresManager = new HighScoresManager();
        initCountries();
    }
    private void initCountries() {
        countries.add(new Country("Poland",      150_000, 900, 550));
        countries.add(new Country("Germany",     600_000, 650, 600));
        countries.add(new Country("Czech Rep.",  80_000,  780, 640));
        countries.add(new Country("Slovakia",    50_000,  920, 660));
        countries.add(new Country("Ukraine",     500_000, 1200, 600));
        countries.add(new Country("Belarus",     70_000,  1100, 500));
        countries.add(new Country("Lithuania",   20_000,  1000, 460));
        countries.add(new Country("Kaliningrad", 10_000,  900, 480));
        countries.add(new Country("Latvia",      15_000,  1000, 410));
        countries.add(new Country("Romania",     120_000, 1100, 710));
    }
    /**
     *  initialize the virus
     */
    public void initVirusAndOrigin() {
        // Create a Virus with infectionRate / spreadChance  / cureRate depending on difficulty
        switch (difficulty) {
            case EASY:
                // Infect slower, spread chance smaller, cure is bigger
                this.virus = new Virus(0.0002, 0.001, 0.00020);
                break;
            case MEDIUM:
                this.virus = new Virus(0.0010, 0.005, 0.00035);
                break;
            case HARD:
                this.virus = new Virus(0.0020, 0.01, 0.00045);
                break;

        }

        Random rand = new Random();
        int index = rand.nextInt(countries.size());
        this.originCountry = countries.get(index);
        originCountry.setInfectedCount(10);
        JOptionPane.showMessageDialog(
                null,
                "Infection has started in " + originCountry.getName() +
                        " with " + originCountry.getInfectedCount() + " initial infected!",
                "Origin Country Selected",
                JOptionPane.INFORMATION_MESSAGE
        );
       // this.originCountry = countries.get(0);


       // originCountry.setInfectedCount(10);

//        System.out.println("Virus initialized with infectionRate=" + virus.getInfectionRate()
//                + ", spreadChance=" + virus.getSpreadChance());
//        System.out.println("Origin country: " + originCountry.getName()
//                + ", initial infected=" + originCountry.getInfectedCount());
        initRoutes();
    }


    private void initRoutes() {
        Country poland = countries.get(0);
        Country germany = countries.get(1);
        Country czechRepublic = countries.get(2);
        Country slovakia = countries.get(3);
        Country ukraine = countries.get(4);
        Country belarus = countries.get(5);
        Country lithuania = countries.get(6);
        Country kaliningrad = countries.get(7);
        Country latvia = countries.get(8);
        Country romania = countries.get(9);

        // Adding routes (LAND, AIR, SEA)
        routes.add(new TransportRoute(poland, germany, TransportType.LAND, true));
        routes.add(new TransportRoute(poland, czechRepublic, TransportType.LAND, true));
        routes.add(new TransportRoute(poland, slovakia, TransportType.LAND, true));
        routes.add(new TransportRoute(poland, ukraine, TransportType.LAND, true));
        routes.add(new TransportRoute(poland, belarus, TransportType.LAND, true));
        routes.add(new TransportRoute(poland, lithuania, TransportType.LAND, true));
        routes.add(new TransportRoute(lithuania, latvia, TransportType.LAND, true));
        routes.add(new TransportRoute(ukraine, romania, TransportType.LAND, true));
        routes.add(new TransportRoute(kaliningrad, lithuania, TransportType.LAND, true));
        routes.add(new TransportRoute(kaliningrad, poland, TransportType.LAND, true));

        // Adding AIR routes
        routes.add(new TransportRoute(poland, germany, TransportType.AIR, true));
        routes.add(new TransportRoute(poland, ukraine, TransportType.AIR, true));
        routes.add(new TransportRoute(romania, poland, TransportType.AIR, true));

        // Adding SEA routes
        routes.add(new TransportRoute(kaliningrad, germany, TransportType.SEA, true));
        routes.add(new TransportRoute(kaliningrad, lithuania, TransportType.SEA, true));

       // System.out.println("Initialized routes: " + routes.size());
    }
    public void updatePoints() {
        for (Country country : countries) {
            int healthy = country.getPopulation() - country.getInfectedCount();
            int previousPoints = country.getPointsAwarded();
            int newPoints = calculatePoints(healthy);

            // Only award points for newly saved people
            this.points = Math.max(0, this.points + (newPoints - previousPoints));
            country.setPointsAwarded(newPoints);
        }
    }
    private int calculatePoints(int healthy) {
        // Simple formula: 1 point per 5000 healthy people
        return healthy / 5000;
    }

    /**
     * This method is called every second (from the GameController).
     * 1) Infect more people in origin country (existing logic).
     * 2) Attempt to spread to neighbors via open routes.
     */
    public void updateInfection() {
        if (virus == null) return;

        // Infect more people in the origin country

        if (originCountry != null) {
            localInfectionUpdate(originCountry);
        }

        //Attempt to spread infection along open routes
        spreadInfectionAlongRoutes();
        // Check if any route should close automatically (infection > 30%).
        closeRoutesIfCriteriaMet();
        //  Cure some fraction of infected each tick
        updateCure();
    }
    /**
     * Infect more people in a single infected country
     */
    private void localInfectionUpdate(Country c) {
        int uninfected = c.getPopulation() - c.getInfectedCount();
        if (uninfected > 0) {
            int newlyInfected = (int) Math.ceil(uninfected * virus.getInfectionRate());
            c.infectPeople(newlyInfected);
//            System.out.println("Infection update in " + c.getName()
//                    + ": newlyInfected=" + newlyInfected
//                    + ", totalInfected=" + c.getInfectedCount());
        }
    }
    /**
     * Check each route. If it's open and either source or destination
     * has some infected people, attempt to infect the other side
     * based on 'virus.getSpreadChance()'.
     */
    private void spreadInfectionAlongRoutes() {
        for (TransportRoute route : routes) {
            if (!route.isOpen()) continue; // skip closed routes

            Country src = route.getSource();
            Country dst = route.getDestination();
            double neighboringBoost = 1.5; // Increases infection speed if neighbors are infected
            // If src is infected, chance to infect dst (and vice versa).
            // We will do a symmetrical spread attempt for both directions.

            if (src.getInfectedCount() > 0 && dst.getInfectedCount() < dst.getPopulation()) {
                trySpread(src, dst, neighboringBoost);
            }
            if (dst.getInfectedCount() > 0 && src.getInfectedCount() < src.getPopulation()) {
                trySpread(dst, src, neighboringBoost);
            }
        }
    }
    /**
     * Attempt to spread infection from "fromCountry" to "toCountry".
     * If random chance < virus.spreadChance, we infect some portion
     * of "toCountry" (like localInfectionUpdate)
     */
    private void trySpread(Country fromCountry, Country toCountry, double neighboringBoost) {

        if (toCountry.getInfectedCount() == toCountry.getPopulation()) {
            return;
        }

        Random rand = new Random();
        double roll = rand.nextDouble();

        // Potentially reduce spreadChance if certain upgrades are purchased:
        double actualSpreadChance = virus.getSpreadChance() * getSpreadChanceModifier()* neighboringBoost;

        if (roll < virus.getSpreadChance()) {
//            System.out.println("Spread attempt from " + fromCountry.getName()
//                    + " to " + toCountry.getName() + " via " + getTransportType(fromCountry, toCountry));


            localInfectionUpdate(toCountry);


            TransportType routeType = getTransportType(fromCountry, toCountry);
            if (spreadListener != null) {
                switch (routeType) {
                    case AIR -> spreadListener.onAirSpread(fromCountry, toCountry);
                    case LAND -> spreadListener.onLandSpread(fromCountry, toCountry);
                    case SEA -> spreadListener.onSeaSpread(fromCountry, toCountry);
                }
            }
        }
    }
    private void closeRoutesIfCriteriaMet() {
        //  if either country in a route has infected > 30% => close AIR routes
        for (TransportRoute route : routes) {
            if (!route.isOpen()) continue;
            if (route.getTransportType() != TransportType.AIR) continue;

            Country src = route.getSource();
            Country dst = route.getDestination();
            double srcPercent = (double) src.getInfectedCount() / src.getPopulation();
            double dstPercent = (double) dst.getInfectedCount() / dst.getPopulation();

            if (srcPercent > 0.30 || dstPercent > 0.30) {
                route.setOpen(false);
                JOptionPane.showMessageDialog(null,
                        "Auto-closing AIR route due to high infection: "
                                + src.getName() + " <-> " + dst.getName(),
                        "Route Closure Alert",
                        JOptionPane.WARNING_MESSAGE);

            }
        }
    }
    public void setGameController(GameController controller) {
        this.gameController = controller;
    }

    public GameController getGameController() {
        return gameController;
    }
    private double getSpreadChanceModifier() {
        // If the player purchased TravelRestrictions, or EnhancedScreening, etc.,multiply the spread chance by some factor.
        double modifier = 1.0;
        for (Upgrade up : purchasedUpgrades) {
            modifier *= up.affectSpreadChance(modifier);
        }
        return modifier;
    }
    public boolean buyUpgrade(Upgrade upgrade) {
        int cost = upgrade.getCost();
        if (points >= cost) {
            points -= cost;
            purchasedUpgrades.add(upgrade);
            upgrade.applyUpgrade(this);
           // System.out.println("Purchased upgrade: " + upgrade.getName());
            return true;
        } else {
          //  System.out.println("Not enough points to buy: " + upgrade.getName());
            return false;
        }
    }
    public void closeAllLANDRoutes() {
        for (TransportRoute route : routes) {
            if (route.getTransportType() == TransportType.LAND) {
                route.setOpen(false);
            }
        }
    }
    public void closeAllSeaRoutes() {
        for (TransportRoute route : routes) {
            if (route.getTransportType() == TransportType.SEA) {
                route.setOpen(false);
            }
        }
    }

    /**
     * Attempt to cure a fraction of infected individuals in all countries.
     * For each country, reduce infectedCount by newlyCured, and award points.
     */
    private void updateCure() {
        if (virus == null) return;

        double rate = virus.getCureRate();
        if (rate <= 0) return; // no curing if cureRate is 0 or negative

        for (Country country : countries) {
            int infected = country.getInfectedCount();
            if (infected > 0) {
                // Cure a fraction of the infected
                int newlyCured = (int) Math.ceil(infected * rate);
                if (newlyCured > 0) {
                    country.setInfectedCount(Math.max(0, infected - newlyCured));

                    // Award points for these cures (example: 1 point per 1000 cured)
                    int curePoints = newlyCured / 1000;
                    if (curePoints > 0) {
                        this.points += curePoints;
//                        System.out.println("Cured " + newlyCured + " in " + country.getName()
//                                + "; awarding " + curePoints + " points.");
                    }
                }
            }
        }
    }


    public List<Upgrade> getPurchasedUpgrades() {
        return purchasedUpgrades;
    }
    private TransportType getTransportType(Country a, Country b) {
        // Just for logging: find the route that connects a->b
        for (TransportRoute route : routes) {
            if ((route.getSource() == a && route.getDestination() == b) ||
                    (route.getSource() == b && route.getDestination() == a)) {
                return route.getTransportType();
            }
        }
        return null;
    }
    public boolean isAllInfected() {
        int totalPop = countries.stream().mapToInt(Country::getPopulation).sum();
        int totalInf = getTotalInfected();
        return (totalInf >= totalPop);
    }

    public boolean isAllCured() {

       // return (getTotalInfected() == 0);
        return countries.stream().allMatch(country -> country.getInfectedCount() == 0);
    }
    public int getTotalInfected() {
        return countries.stream().mapToInt(Country::getInfectedCount).sum();
    }

    public int getTotalHealthy() {
        return countries.stream()
                .mapToInt(c -> c.getPopulation() - c.getInfectedCount())
                .sum();
    }

    public List<Country> getCountries() {
        return countries;
    }


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }


    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void incrementTime() {
        this.timeInSeconds++;
    }


    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public Virus getVirus() {
        return this.virus;
    }



    public void addPoints(int amount) {
        this.points += amount;
    }
    public HighScoresManager getHighScoresManager() {
        return this.highScoresManager;
    }
}