package view;

import javax.swing.*;
import java.awt.*;

/**
 * This dialog lets the user select a difficulty (Easy, Medium, Hard)
 * and confirms the choice.
 */
public class DifficultySelectionView extends JDialog {

    private JRadioButton easyRadio;
    private JRadioButton mediumRadio;
    private JRadioButton hardRadio;
    private JButton confirmButton;

    public DifficultySelectionView(Frame owner) {

        super(owner, "Select Difficulty", true);

        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());


        JLabel label = new JLabel("Choose Your Difficulty", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);


        easyRadio = new JRadioButton("Easy");
        mediumRadio = new JRadioButton("Medium");
        hardRadio = new JRadioButton("Hard");


        ButtonGroup group = new ButtonGroup();
        group.add(easyRadio);
        group.add(mediumRadio);
        group.add(hardRadio);


        mediumRadio.setSelected(true);


        JPanel radioPanel = new JPanel(new FlowLayout());
        radioPanel.add(easyRadio);
        radioPanel.add(mediumRadio);
        radioPanel.add(hardRadio);

        add(radioPanel, BorderLayout.CENTER);


        confirmButton = new JButton("Confirm");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(confirmButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public boolean isEasySelected() {
        return easyRadio.isSelected();
    }

    public boolean isMediumSelected() {
        return mediumRadio.isSelected();
    }

    public boolean isHardSelected() {
        return hardRadio.isSelected();
    }
}