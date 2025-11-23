package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Контейнер для данных уровня:
 * - множество координат препятствий
 * - стартовая позиция игрока
 */
public class Level {
    private final Set<GridPoint2> obstacles;
    private final GridPoint2 playerStart;

    public Level(Set<GridPoint2> obstacles, GridPoint2 playerStart) {
        this.obstacles = new HashSet<>(obstacles);
        this.playerStart = new GridPoint2(playerStart);
    }

    public Set<GridPoint2> getObstacles() {
        return Collections.unmodifiableSet(obstacles);
    }

    public GridPoint2 getPlayerStart() {
        return new GridPoint2(playerStart);
    }
}
