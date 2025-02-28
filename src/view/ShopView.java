package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShopView extends JDialog {

    private GameModel model;
    private List<Upgrade> availableUpgrades;

    public ShopView(Frame owner, GameModel model) {
        super(owner, "Shop", true);
        this.model = model;

        setSize(400, 300);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Upgrade Shop", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel upgradesPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        availableUpgrades = createUpgrades();

        for (Upgrade upgrade : availableUpgrades) {
            //  panel that holds a Buy button plus a description label in one row
            JPanel upgradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));


            JButton buyButton = new JButton(upgrade.getName() + " (" + upgrade.getCost() + " points)");
            buyButton.addActionListener(e -> buyUpgrade(upgrade));


            JLabel descLabel = new JLabel(upgrade.getDescription());


            upgradePanel.add(buyButton);
            upgradePanel.add(descLabel);

            upgradesPanel.add(upgradePanel);
        }


        add(new JScrollPane(upgradesPanel), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private List<Upgrade> createUpgrades() {
        List<Upgrade> upgrades = new ArrayList<>();
        upgrades.add(new TravelRestrictionsUpgrade());
        upgrades.add(new EnhancedScreeningUpgrade());
        upgrades.add(new MedicalSupportUpgrade());
        upgrades.add(new CureResearchUpgrade());
        upgrades.add(new FasterTestingUpgrade());
        upgrades.add(new InternationalAidUpgrade());
        upgrades.add(new PublicAwarenessCampaignUpgrade());
        upgrades.add(new VaccineUpgrade());
        upgrades.add(new LocalLockdownUpgrade());
        return upgrades;
    }

    private void buyUpgrade(Upgrade upgrade) {
        if (model.buyUpgrade(upgrade)) {
            JOptionPane.showMessageDialog(this,
                    "Purchased: " + upgrade.getName()
                            + "\n\n" + upgrade.getDescription(),
                    "Shop",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Not enough points to buy: " + upgrade.getName(),
                    "Shop",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

}