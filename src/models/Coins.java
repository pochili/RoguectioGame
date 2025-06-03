package models;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Coins {
    private int x;
    private int y;
    private final int tileSize = 64;
    private final Image coinImage;

    public Coins() {
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