package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.TestMovable;
import ru.mipt.bit.platformer.level.LevelBounds;
import ru.mipt.bit.platformer.state.OccupiedCells;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomTankMovementInputHandlerTest {

    @Test
    void shouldNotMoveWhileMovementInProgress() {
        TestMovable tank = new TestMovable(0, 0);
        tank.setMovementProgress(0.2f);
        OccupiedCells occupiedCells = new OccupiedCells(Set.of());
        RandomTankMovementInputHandler handler = new RandomTankMovementInputHandler(tank, occupiedCells, new LevelBounds(2, 2));

        handler.handleInput();

        assertEquals(new GridPoint2(0, 0), tank.getPlayerDestinationCoordinates());
        assertEquals(0.2f, tank.getPlayerMovementProgress());
    }

    @Test
    void shouldChooseAnyAvailableDirectionUntilSuccess() {
        TestMovable tank = new TestMovable(0, 0);
        OccupiedCells occupiedCells = new OccupiedCells(Set.of());
        LevelBounds bounds = new LevelBounds(2, 1);
        RandomTankMovementInputHandler handler = new RandomTankMovementInputHandler(tank, occupiedCells, bounds);

        handler.handleInput();

        assertEquals(new GridPoint2(1, 0), tank.getPlayerDestinationCoordinates());
        assertEquals(0f, tank.getPlayerMovementProgress());
    }

    @Test
    void shouldNotMoveWhenAllDirectionsBlocked() {
        TestMovable tank = new TestMovable(0, 0);
        OccupiedCells occupiedCells = new OccupiedCells(Set.of(new GridPoint2(1, 0)));
        LevelBounds bounds = new LevelBounds(2, 1);
        RandomTankMovementInputHandler handler = new RandomTankMovementInputHandler(tank, occupiedCells, bounds);

        handler.handleInput();

        assertEquals(new GridPoint2(0, 0), tank.getPlayerDestinationCoordinates());
        assertEquals(1f, tank.getPlayerMovementProgress());
    }
}
