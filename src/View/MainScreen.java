package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Constants.Constants;
import models.Skeleton;
import models.Coins;
import BD.BD;


public class MainScreen extends JFrame {

    private final Image floorImage;
    private final Image wallImage;
    private Image characterImage;

    private final int tileSize = 64;
    private final List<Skeleton> skeletons;
    private Timer skeletonTimer;
    private final JPanel gamePanel;

    private int playerX;
    private int playerY;
    private int playerSpeed;
    private int playerHealth;

    private final Coins coin;
    private final String characterName;

    private int score = 0;
    private int highScore = 0;
    private final String RECORD_FILE = "record.txt";

    public MainScreen(String characterName) {
        this.characterName = characterName;
        setTitle("Game Screen - " + characterName);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        floorImage = new ImageIcon("src/assets/dungeon/tile001.png").getImage();
        wallImage = new ImageIcon("src/assets/dungeon/tile004.png").getImage();

        switch (characterName.toLowerCase()) {
            case "warrior" -> {
                characterImage = new ImageIcon("src/assets/warrior/warrior_down.gif").getImage();
                playerSpeed = Constants.WARRIOR_SPEED;
                playerHealth = Constants.WARRIOR_HEALTH;
            }
            case "wizard" -> {
                characterImage = new ImageIcon("src/assets/wizard/wizard_down.gif").getImage();
                playerSpeed = Constants.WIZZARD_SPEED;
                playerHealth = Constants.WIZZARD_HEALTH;
            }
            case "priest" -> {
                characterImage = new ImageIcon("src/assets/priest/priest_down.gif").getImage();
                playerSpeed = Constants.PRIEST_SPEED;
                playerHealth = Constants.PRIEST_HEALTH;
            }
            default -> {
                characterImage = null;
                playerSpeed = Constants.WARRIOR_SPEED;
                playerHealth = Constants.WARRIOR_HEALTH;
            }
        }

        playerX = tileSize;
        playerY = 5 * tileSize;

        coin = new Coins();

        skeletons = new ArrayList<>();
        skeletons.add(new Skeleton(5 * tileSize, 3 * tileSize));
        skeletons.add(new Skeleton(15 * tileSize, 6 * tileSize));
        skeletons.add(new Skeleton(10 * tileSize, 10 * tileSize));
        skeletons.add(new Skeleton(16 * tileSize, 8 * tileSize));

        loadHighScore(); //

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int cols = getWidth() / tileSize;
                int rows = getHeight() / tileSize;

                for (int y = 0; y < rows; y++) {
                    for (int x = 0; x < cols; x++) {
                        g.drawImage(floorImage, x * tileSize, y * tileSize, tileSize, tileSize, this);
                    }
                }

                for (int x = 0; x < cols; x++) {
                    g.drawImage(wallImage, x * tileSize, 0, tileSize, tileSize, this);
                    g.drawImage(wallImage, x * tileSize, (rows - 1) * tileSize, tileSize, tileSize, this);
                }

                for (Skeleton skeleton : skeletons) {
                    skeleton.draw(g, this);
                }

                coin.draw(g, this);

                if (characterImage != null) {
                    g.drawImage(characterImage, playerX, playerY, tileSize, tileSize, this);
                }

                g.setColor(Color.white);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Lives: " + playerHealth, 20, 40);
                g.drawString("Puntos: " + score, 20, 70);
                g.drawString("Récord: " + highScore, 20, 100);
            }
        };

        add(gamePanel);

        skeletonTimer = new Timer(75, e -> {
            for (Skeleton s : skeletons) {
                s.move(getWidth(), getHeight());
                if (s.checkCollision(playerX, playerY)) {
                    playerHealth--;

                    playerX = tileSize;
                    playerY = 5 * tileSize;

                    if (playerHealth <= 0) {
                        skeletonTimer.stop();

                        if (score > highScore) {
                            saveHighScore(score);
                            JOptionPane.showMessageDialog(MainScreen.this, "Game Over! " + characterName + " died.\nNuevo récord: " + score + " puntos.");
                        } else {
                            JOptionPane.showMessageDialog(MainScreen.this, "Game Over! " + characterName + " died.\nPuntos: " + score + "\nRécord actual: " + highScore);
                        }

                        dispose();
                        return;
                    }
                }
            }
            gamePanel.repaint();
        });
        skeletonTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int direction = -1;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> direction = Constants.LEFT;
                    case KeyEvent.VK_RIGHT -> direction = Constants.RIGHT;
                    case KeyEvent.VK_UP -> direction = Constants.UP;
                    case KeyEvent.VK_DOWN -> direction = Constants.DOWN;
                }

                if (direction != -1) {
                    movePlayer(direction);
                    if (coin.checkCollision(playerX, playerY)) {
                        score++;
                        coin.respawn();
                    }
                    repaint();
                }
            }
        });

        setFocusable(true);
    }

    private void movePlayer(int direction) {
        int panelHeight = getHeight();
        int panelWidth = getWidth();

        switch (direction) {
            case Constants.LEFT -> {
                if (playerX - playerSpeed >= 0) {
                    playerX -= playerSpeed;
                }
            }
            case Constants.RIGHT -> {
                if (playerX + playerSpeed + tileSize <= panelWidth) {
                    playerX += playerSpeed;
                }
            }
            case Constants.UP -> {
                if (playerY - playerSpeed >= tileSize) {
                    playerY -= playerSpeed;
                }
            }
            case Constants.DOWN -> {
                if (playerY + playerSpeed + tileSize <= panelHeight - tileSize) {
                    playerY += playerSpeed;
                }
            }
        }
    }

    private void loadHighScore() {
        try {
            File file = new File(RECORD_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                if (line != null) {
                    highScore = Integer.parseInt(line.trim());
                }
                reader.close();
            }
        } catch (Exception e) {
            System.err.println("No se pudo leer el récord: " + e.getMessage());
        }
    }

    private void saveHighScore(int newHighScore) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(RECORD_FILE));
            writer.println(newHighScore);
            writer.close();
        } catch (Exception e) {
            System.err.println("No se pudo guardar el récord: " + e.getMessage());
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            BD.GestorRecords.insertarNuevoRecord(score);
            System.exit(0);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainScreen screen = new MainScreen("Warrior"); // Puedes cambiar a "wizard" o "priest"
            screen.setVisible(true);
        });
    }
}
