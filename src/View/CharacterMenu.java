package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CharacterMenu extends JFrame {


    public CharacterMenu() {
        setTitle("Character Menu");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BorderLayout());
        add(panel);


        JLabel titleLabel = new JLabel("¿Qué personaje deseas jugar?", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel characterPanel = new JPanel();
        characterPanel.setBackground(Color.BLACK);
        characterPanel.setLayout(new GridLayout(1, 3, 20, 0));
        characterPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));


        characterPanel.add(createCharacterLabel("Warrior"));
        characterPanel.add(createCharacterLabel("Wizard"));
        characterPanel.add(createCharacterLabel("Priest"));

        panel.add(characterPanel, BorderLayout.CENTER);
    }

    private JLabel createCharacterLabel(String name) {
        JLabel label = new JLabel(name, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Selected: " + name);

                dispose();

                MainScreen mainScreen = new MainScreen(name);
                mainScreen.setVisible(true);
            }
        });

        return label;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CharacterMenu().setVisible(true);
        });
    }
}
