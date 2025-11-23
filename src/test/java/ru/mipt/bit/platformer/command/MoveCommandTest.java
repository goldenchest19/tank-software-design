package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.mipt.bit.platformer.TestMovable;
import ru.mipt.bit.platformer.level.LevelBounds;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.state.OccupiedCells;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoveCommandTest {

    private TestMovable movable;
    private OccupiedCells occupiedCells;
    private LevelBounds levelBounds;

    @BeforeEach
    void setUp() {
        movable = new TestMovable(1, 1);
        occupiedCells = Mockito.mock(OccupiedCells.class);
        levelBounds = new LevelBounds(3, 3);
    }

    @Test
    void shouldNotMoveWhenMovementNotFinished() {
        movable.setMovementProgress(0.5f);
        MoveCommand command = new MoveCommand(movable, Direction.UP, occupiedCells, levelBounds);

        boolean result = command.execute();

        assertFalse(result);
        assertEquals(Direction.UP.getRotation(), movable.getPlayerRotation());
        assertEquals(0.5f, movable.getPlayerMovementProgress());
        assertEquals(new GridPoint2(1, 1), movable.getPlayerDestinationCoordinates());
        verify(occupiedCells, never()).canMoveTo(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldNotMoveOutsideBounds() {
        MoveCommand command = new MoveCommand(movable, Direction.LEFT, occupiedCells, new LevelBounds(1, 1));

        boolean result = command.execute();

        assertFalse(result);
        assertEquals(new GridPoint2(1, 1), movable.getPlayerDestinationCoordinates());
        verify(occupiedCells, never()).canMoveTo(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldNotMoveIntoOccupiedCell() {
        GridPoint2 next = new GridPoint2(2, 1);
        when(occupiedCells.canMoveTo(movable, next)).thenReturn(false);
        MoveCommand command = new MoveCommand(movable, Direction.RIGHT, occupiedCells, levelBounds);

        boolean result = command.execute();

        assertFalse(result);
        verify(occupiedCells).canMoveTo(movable, next);
        verify(occupiedCells, never()).startMove(Mockito.any(), Mockito.any());
        assertEquals(1f, movable.getPlayerMovementProgress());
    }

    @Test
    void shouldMoveWhenPathIsClear() {
        GridPoint2 next = new GridPoint2(1, 2);
        when(occupiedCells.canMoveTo(movable, next)).thenReturn(true);
        MoveCommand command = new MoveCommand(movable, Direction.UP, occupiedCells, levelBounds);

        boolean result = command.execute();

        assertTrue(result);
        verify(occupiedCells).startMove(movable, next);
        assertEquals(next, movable.getPlayerDestinationCoordinates());
        assertEquals(0f, movable.getPlayerMovementProgress());
    }
}
