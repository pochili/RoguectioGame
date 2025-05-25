package models;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Coins {
    private int xPos;
    private int yPos;
    private boolean collected = false;
    private final ImageIcon coinImage;
    private final int panelWidth;
    private final int panelHeight;
    private final int tileSize;

    public Coins(String imagePath, int panelWidth, int panelHeight, int tileSize) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.tileSize = tileSize;
        this.coinImage = new ImageIcon(imagePath);
        respawn();
    }

    public void draw(Graphics g, JPanel observer) {
        if (!collected) {
            g.drawImage(coinImage.getImage(), xPos, yPos, tileSize, tileSize, observer);
        }
    }

    public void checkCollision(int playerX, int playerY) {
        Rectangle coinRect = new Rectangle(xPos, yPos, tileSize, tileSize);
        Rectangle playerRect = new Rectangle(playerX, playerY, tileSize, tileSize);
        if (coinRect.intersects(playerRect)) {
            collected = true;
        }
    }

    public void respawn() {
        Random random = new Random();
        int cols = panelWidth / tileSize;
        int rows = panelHeight / tileSize;
        xPos = random.nextInt(cols - 2) * tileSize + tileSize;
        yPos = random.nextInt(rows - 2) * tileSize + tileSize;
        collected = false;
    }

    public boolean isCollected() {
        return collected;
    }
}
