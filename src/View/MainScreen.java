package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Constants {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;

    public static final int WARRIOR_SPEED = 3;
    public static final int WIZZARD_SPEED = 7;
    public static final int PRIEST_SPEED = 4;

    public static final int WARRIOR_HEALTH = 5;
    public static final int WIZZARD_HEALTH = 3;
    public static final int PRIEST_HEALTH = 5;
}

class Coin {
    private int x;
    private int y;
    private final int tileSize = 64;
    private final Image coinImage;

    public Coin() {
        coinImage = new ImageIcon("src/assets/dungeon/dollar.png").getImage();
        respawn();
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(coinImage, x, y, tileSize, tileSize, panel);
    }

    public void respawn() {
        Random rand = new Random();
        int cols = 20;
        int rows = 10;
        int randomCol = rand.nextInt(cols);
        int randomRow = rand.nextInt(rows - 2) + 1;

        x = randomCol * tileSize;
        y = randomRow * tileSize;
    }

    public boolean checkCollision(int playerX, int playerY) {
        Rectangle playerRect = new Rectangle(playerX, playerY, tileSize, tileSize);
        Rectangle coinRect = new Rectangle(x, y, tileSize, tileSize);
        return playerRect.intersects(coinRect);
    }
}

class Skeleton {
    private int x, y;
    private final int speed = 3;
    private final int tileSize = 64;
    private final Image image = new ImageIcon("src/assets/skeleton/skeleton_down.gif").getImage();
    private int direction;
    private final Random rand = new Random();

    public Skeleton(int x, int y) {
        this.x = x;
        this.y = y;
        this.direction = rand.nextInt(4);
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(image, x, y, tileSize, tileSize, panel);
    }

    public void move(int panelWidth, int panelHeight) {
        int newX = x, newY = y;
        switch (direction) {
            case 0 -> newX -= speed;
            case 1 -> newX += speed;
            case 2 -> newY -= speed;
            case 3 -> newY += speed;
        }

        if (newX >= 0 && newX + tileSize <= panelWidth && newY >= tileSize && newY + tileSize <= panelHeight - tileSize) {
            x = newX;
            y = newY;
        } else {
            direction = rand.nextInt(4);
        }

        if (rand.nextInt(20) == 0) {
            direction = rand.nextInt(4);
        }
    }

    public boolean checkCollision(int playerX, int playerY) {
        Rectangle playerRect = new Rectangle(playerX, playerY, tileSize, tileSize);
        Rectangle skeletonRect = new Rectangle(x, y, tileSize, tileSize);
        return playerRect.intersects(skeletonRect);
    }
}

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

    private final Coin coin;
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

        coin = new Coin();

        skeletons = new ArrayList<>();
        skeletons.add(new Skeleton(5 * tileSize, 3 * tileSize));
        skeletons.add(new Skeleton(15 * tileSize, 6 * tileSize));
        skeletons.add(new Skeleton(10 * tileSize, 10 * tileSize));
        skeletons.add(new Skeleton(30 * tileSize, 25 * tileSize));

        loadHighScore(); // Leer récord al inicio

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

                g.setColor(Color.BLACK);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainScreen screen = new MainScreen("Warrior"); // Puedes cambiar a "wizard" o "priest"
            screen.setVisible(true);
        });
    }
}
