package models;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Skeleton {
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
            case 0:  newX -= speed;
            case 1: newX += speed;
            case 2: newY -= speed;
            case 3: newY += speed;
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