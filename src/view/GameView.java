package view;
import model.Country;
import model.GameModel;
import model.TransportRoute;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.ArrayList;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Main game window after difficulty is selected.
 */

public class GameView extends JFrame {

    private JLabel timeLabel;
    private JLabel pointsLabel;
    private JButton shopButton;
    private JLayeredPane layeredPane;
    private JLabel mapLabel;
    private ImageIcon mapIcon;
    private List<JButton> countryButtons;
    private JLabel totalInfectedLabel;
    private JLabel totalHealthyLabel;
    private JPanel countryPanel;


    public GameView(GameModel model) {
        super("AntiPlague");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        setLayout(new BorderLayout());

        //Top panel for time and points
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        timeLabel = new JLabel("Time: 0s");
        pointsLabel = new JLabel("Points: 0");
        totalInfectedLabel = new JLabel("Infected: 0");
        totalHealthyLabel = new JLabel("Healthy: 0");


        // 1) Load the original icon
        ImageIcon shopIcon = new ImageIcon(getClass().getResource("/images/shop.png"));
        Image originalImage = shopIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        shopIcon = new ImageIcon(scaledImage);
        shopButton = new JButton("Shop", shopIcon);
        shopButton.setHorizontalTextPosition(SwingConstants.RIGHT);



// in a row, with some spacing:
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(timeLabel);
        topPanel.add(Box.createHorizontalStrut(50));
        topPanel.add(pointsLabel);
        topPanel.add(Box.createHorizontalStrut(50));
        topPanel.add(totalInfectedLabel);
        topPanel.add(Box.createHorizontalStrut(50));
        topPanel.add(totalHealthyLabel);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(Box.createHorizontalStrut(50));
        topPanel.add(shopButton);
        topPanel.add(Box.createHorizontalStrut(20));

        add(topPanel, BorderLayout.NORTH);

        countryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JScrollPane scrollPane = new JScrollPane(countryPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Layered Pane for the map and country markers
        layeredPane = new JLayeredPane();
        //set the preferred size to the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        layeredPane.setPreferredSize(screenSize);


        ImageIcon originalMapIcon = new ImageIcon(getClass().getResource("/images/1200px-Poland_in_Europe.png"));


        Image scaledMap = originalMapIcon.getImage().getScaledInstance(
                screenSize.width, screenSize.height, Image.SCALE_SMOOTH
        );

        mapIcon = new ImageIcon(scaledMap);


        mapLabel = new JLabel(mapIcon);
        mapLabel.setBounds(0, 0, screenSize.width, screenSize.height);


        layeredPane.setLayout(null);

        layeredPane.add(mapLabel, Integer.valueOf(0));


        buildCountryMarkers(model.getCountries());


        add(layeredPane, BorderLayout.CENTER);


        pack();
        setVisible(true);
    }

    /**
     * Creates a button (or label) for each Country and places it at (xCoord, yCoord) on the map.
     */
    private void buildCountryMarkers(List<Country> countries) {
        countryButtons = new ArrayList<>();

        for (Country country : countries) {
            JButton button = new JButton(country.getName());
            button.setSize(100, 30);
            button.setLocation(country.getXCoord(), country.getYCoord());
            layeredPane.add(button, Integer.valueOf(1));
            countryButtons.add(button);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            button.setOpaque(false);

            Font defaultFont = button.getFont();


            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {

                    Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) defaultFont.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    button.setFont(defaultFont.deriveFont(attributes));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Reset to the default font
                    button.setFont(defaultFont);
                }
            });
        }
    }



    public void addCountryButtons(List<Country> countries, CountryClickListener listener) {
        for (Country country : countries) {
            JButton button = new JButton(country.getName());
            button.setToolTipText("Click for details");
            button.addActionListener(e -> listener.onCountryClicked(country));
            countryPanel.add(button);
        }
        revalidate();
        repaint();
    }



    public List<JButton> getCountryButtons() {
        return countryButtons;
    }

    public void setTimeLabel(int seconds) {
        timeLabel.setText("Time: " + seconds + "s");
    }

    public void setPointsLabel(int points) {
        pointsLabel.setText("Points: " + points);
    }

    public void setTotalInfectedLabel(int totalInfected) {
        totalInfectedLabel.setText("Infected: " + totalInfected);
    }


    public void setTotalHealthyLabel(int totalHealthy) {
        totalHealthyLabel.setText("Healthy: " + totalHealthy);
    }

    public JButton getShopButton() {
        return shopButton;
    }

    public interface CountryClickListener {
        void onCountryClicked(Country country);
    }
} 