package ru.mipt.bit.platformer.state;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.TestMovable;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OccupiedCellsTest {

    @Test
    void shouldBlockStaticObstacles() {
        OccupiedCells occupiedCells = new OccupiedCells(Set.of(new GridPoint2(1, 1)));
        TestMovable movable = new TestMovable(0, 0);

        assertFalse(occupiedCells.canMoveTo(movable, new GridPoint2(1, 1)));
        assertTrue(occupiedCells.canMoveTo(movable, new GridPoint2(0, 1)));
    }

    @Test
    void shouldRespectExistingReservations() {
        OccupiedCells occupiedCells = new OccupiedCells(Set.of());
        TestMovable first = new TestMovable(1, 1);
        occupiedCells.registerStanding(first);

        TestMovable second = new TestMovable(0, 0);
        assertFalse(occupiedCells.canMoveTo(second, new GridPoint2(1, 1)));
        assertTrue(occupiedCells.canMoveTo(first, new GridPoint2(1, 1)));
    }

    @Test
    void shouldReserveSourceAndDestinationDuringMove() {
        OccupiedCells occupiedCells = new OccupiedCells(Set.of());
        TestMovable moving = new TestMovable(0, 0);
        occupiedCells.startMove(moving, new GridPoint2(1, 0));

        TestMovable other = new TestMovable(2, 0);
        assertFalse(occupiedCells.canMoveTo(other, new GridPoint2(0, 0)));
        assertFalse(occupiedCells.canMoveTo(other, new GridPoint2(1, 0)));
    }

    @Test
    void shouldUpdateReservationWhenMovementCompletes() {
        OccupiedCells occupiedCells = new OccupiedCells(Set.of());
        TestMovable moving = new TestMovable(0, 0);
        occupiedCells.startMove(moving, new GridPoint2(1, 0));

        moving.setCoordinates(1, 0);
        moving.setMovementProgress(1f);
        occupiedCells.syncWithMovement(moving);

        TestMovable other = new TestMovable(2, 0);
        assertTrue(occupiedCells.canMoveTo(other, new GridPoint2(0, 0)));
        assertFalse(occupiedCells.canMoveTo(other, new GridPoint2(1, 0)));
    }
}
