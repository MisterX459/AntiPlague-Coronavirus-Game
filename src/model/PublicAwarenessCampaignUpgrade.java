package model;

public class PublicAwarenessCampaignUpgrade extends Upgrade {

    public PublicAwarenessCampaignUpgrade() {
        super("Public Awareness Campaign", 20);
    }

    @Override
    public void applyUpgrade(GameModel model) {
        // Award immediate 10 points for campaign
        model.addPoints(10);
    }
    @Override
    public String getDescription() {
        return "Instantly awards 10 points and encourages people to stay safe.";
    }
}