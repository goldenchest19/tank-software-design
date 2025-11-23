package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;


public class LevelBounds {

    private final int width;
    private final int height;

    public LevelBounds(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(GridPoint2 point) {
        return point.x >= 0 && point.x < width && point.y >= 0 && point.y < height;
    }
}
