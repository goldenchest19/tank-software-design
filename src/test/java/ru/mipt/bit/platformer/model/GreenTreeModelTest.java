package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GreenTreeModelTest {

    @Test
    void testTreeCoordinatesStoredCorrectly() {
        GridPoint2 coords = new GridPoint2(2, 3);
        GreenTreeModel tree = new GreenTreeModel(coords);
        assertEquals(coords, tree.getTreeObstacleCoordinates());
    }
}
