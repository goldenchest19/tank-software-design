package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Генератор случайного уровня.
 * Заполняет поле деревьями с указанной плотностью и ставит игрока в случайную свободную клетку.
 */
public class RandomLevelGenerator implements LevelGenerator {

    private final float obstacleProbability; // например 0.15f = 15% клеток будут заняты

    private final Random random = new Random();

    public RandomLevelGenerator(float obstacleProbability) {
        if (obstacleProbability < 0f || obstacleProbability > 1f) {
            throw new IllegalArgumentException("obstacleProbability must be in [0,1]");
        }
        this.obstacleProbability = obstacleProbability;
    }

    @Override
    public Level generate(int width, int height) {
        Set<GridPoint2> obstacles = new HashSet<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (random.nextFloat() < obstacleProbability) {
                    obstacles.add(new GridPoint2(x, y));
                }
            }
        }

        // Выбираем случайную стартовую позицию, не попадающую в препятствие.
        GridPoint2 playerStart;
        int attempts = 0;
        do {
            int sx = random.nextInt(width);
            int sy = random.nextInt(height);
            playerStart = new GridPoint2(sx, sy);
            attempts++;
            // На случай, если поле полностью заполнено — сделаем резервную позицию (0,0)
            if (attempts > width * height + 5) {
                playerStart = new GridPoint2(0, 0);
                break;
            }
        } while (obstacles.contains(playerStart));

        return new Level(obstacles, playerStart);
    }
}
