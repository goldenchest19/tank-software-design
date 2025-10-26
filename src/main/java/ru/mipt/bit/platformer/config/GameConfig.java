package ru.mipt.bit.platformer.config;


public class GameConfig {

    public enum LevelMode {
        FILE,
        RANDOM
    }

    private static final LevelMode levelMode = LevelMode.FILE;
    private static final String levelFilePath = "level.txt";
    private static final float randomObstacleDensity = 0.12f;

    public static LevelMode getLevelMode() {
        return levelMode;
    }

    public static String getLevelFilePath() {
        return levelFilePath;
    }

    public static float getRandomObstacleDensity() {
        return randomObstacleDensity;
    }
}
