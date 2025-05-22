package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class SelectCharacterScreen extends JFrame {


    public SelectCharacterScreen() {
        setTitle("Select your Character");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 10, 10));
        add(panel);

        ImageIcon warrior = new ImageIcon("src/assets/warrior/warrior_down.gif");
        ImageIcon wizard = new ImageIcon("src/assets/wizard/wizard_down.gif");
        ImageIcon priest = new ImageIcon("src/assets/priest/priest_down.gif");

        JLabel warriorLabel = new JLabel(warrior);
        JLabel wizardLabel = new JLabel(wizard);
        JLabel priestLabel = new JLabel(priest);

        warriorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        wizardLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        priestLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        warriorLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                StartGame("Warrior");
            }
        });

        wizardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                StartGame("Wizard");
            }
        });

        priestLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                StartGame("Priest");
            }
        });


        panel.add(warriorLabel);
        panel.add(wizardLabel);
        panel.add(priestLabel);


    }



    private void StartGame(String NameCharacter){

        dispose();

    }

    public static void main(String[] args) {
        SelectCharacterScreen mainScrew=  new SelectCharacterScreen();
        mainScrew.setVisible(true);
    }


}
