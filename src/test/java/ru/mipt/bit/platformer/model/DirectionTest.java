package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void testDeltaAndRotation() {
        assertEquals(new GridPoint2(0, 1), Direction.UP.getDelta());
        assertEquals(90f, Direction.UP.getRotation());

        assertEquals(new GridPoint2(1, 0), Direction.RIGHT.getDelta());
        assertEquals(0f, Direction.RIGHT.getRotation());
    }

    @Test
    void testAllDirectionsUnique() {
        long uniqueCount = java.util.Arrays.stream(Direction.values())
                .map(Direction::getDelta)
                .distinct()
                .count();
        assertEquals(Direction.values().length, uniqueCount);
    }
}
