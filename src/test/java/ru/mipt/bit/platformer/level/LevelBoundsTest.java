package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelBoundsTest {

    @Test
    void shouldCheckPointInsideBounds() {
        LevelBounds bounds = new LevelBounds(3, 2);

        assertTrue(bounds.contains(new GridPoint2(0, 0)));
        assertTrue(bounds.contains(new GridPoint2(2, 1)));
        assertFalse(bounds.contains(new GridPoint2(-1, 0)));
        assertFalse(bounds.contains(new GridPoint2(3, 1)));
        assertFalse(bounds.contains(new GridPoint2(2, 2)));
    }
}
