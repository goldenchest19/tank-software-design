package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @Test
    void shouldReturnCopiesOfData() {
        Set<GridPoint2> obstacles = new HashSet<>();
        obstacles.add(new GridPoint2(1, 1));
        Level level = new Level(obstacles, new GridPoint2(2, 2));

        Set<GridPoint2> returnedObstacles = level.getObstacles();
        assertThrows(UnsupportedOperationException.class, () -> returnedObstacles.add(new GridPoint2(3, 3)));
        obstacles.add(new GridPoint2(4, 4));
        assertEquals(1, returnedObstacles.size());

        GridPoint2 playerStart = level.getPlayerStart();
        playerStart.set(0, 0);
        assertNotSame(playerStart, level.getPlayerStart());
        assertEquals(new GridPoint2(2, 2), level.getPlayerStart());
    }
}
