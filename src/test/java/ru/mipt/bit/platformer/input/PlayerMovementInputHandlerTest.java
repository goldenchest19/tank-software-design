package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.TestMovable;
import ru.mipt.bit.platformer.level.LevelBounds;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.state.OccupiedCells;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerMovementInputHandlerTest {

    private Input previousInput;

    @BeforeEach
    void setUp() {
        previousInput = Gdx.input;
    }

    @AfterEach
    void tearDown() {
        Gdx.input = previousInput;
    }

    @Test
    void shouldExecuteMoveForPressedDirection() {
        Input input = mock(Input.class);
        when(input.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);
        Gdx.input = input;

        TestMovable player = new TestMovable(1, 1);
        OccupiedCells occupiedCells = new OccupiedCells(Set.of());
        LevelBounds levelBounds = new LevelBounds(5, 5);
        PlayerMovementInputHandler handler = new PlayerMovementInputHandler(player, occupiedCells, levelBounds);

        handler.handleInput();

        assertEquals(new GridPoint2(2, 1), player.getPlayerDestinationCoordinates());
        assertEquals(Direction.RIGHT.getRotation(), player.getPlayerRotation());
        assertEquals(0f, player.getPlayerMovementProgress());
    }

    @Test
    void shouldIgnoreWhenNoKeysPressed() {
        Input input = mock(Input.class);
        Gdx.input = input;

        TestMovable player = new TestMovable(1, 1);
        OccupiedCells occupiedCells = new OccupiedCells(Set.of());
        LevelBounds levelBounds = new LevelBounds(5, 5);
        PlayerMovementInputHandler handler = new PlayerMovementInputHandler(player, occupiedCells, levelBounds);

        handler.handleInput();

        assertEquals(new GridPoint2(1, 1), player.getPlayerDestinationCoordinates());
        assertEquals(1f, player.getPlayerMovementProgress());
    }

    @Test
    void shouldUseFirstPressedDirectionInOrder() {
        Input input = mock(Input.class);
        when(input.isKeyPressed(Input.Keys.UP)).thenReturn(true);
        when(input.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);
        Gdx.input = input;

        TestMovable player = new TestMovable(1, 1);
        OccupiedCells occupiedCells = new OccupiedCells(Set.of());
        LevelBounds levelBounds = new LevelBounds(5, 5);
        PlayerMovementInputHandler handler = new PlayerMovementInputHandler(player, occupiedCells, levelBounds);

        handler.handleInput();

        assertEquals(new GridPoint2(1, 2), player.getPlayerDestinationCoordinates());
        assertEquals(Direction.UP.getRotation(), player.getPlayerRotation());
    }
}
