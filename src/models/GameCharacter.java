package models;

import java.awt.Image;

public class GameCharacter {
    private int x, y;
    private int speed;
    private Image image;

    public GameCharacter(int x, int y, int speed, Image image) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.image = image;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getSpeed() { return speed; }
    public Image getImage() { return image; }
}
