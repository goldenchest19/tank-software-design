package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.PlayerModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PlayerMovementInputHandlerTest {

    private PlayerModel player;
    private GridPoint2 obstacle;
    private PlayerMovementInputHandler handler;

    @BeforeEach
    void setUp() {
        // Подготавливаем модель игрока
        player = new PlayerModel(new GridPoint2(1 ,1));
        obstacle = new GridPoint2(99, 99); // далеко, не мешает
        handler = new PlayerMovementInputHandler(player, obstacle);

        // Мокаем Gdx.input
        Gdx.input = mock(Input.class);
    }

    @Test
    void movesPlayerRightWhenRightKeyPressed() {
        when(Gdx.input.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

        handler.handleInput();

        // Проверяем, что движение началось в нужную клетку
        assertEquals(new GridPoint2(2, 1), player.getPlayerDestinationCoordinates());
        assertEquals(0f, player.getPlayerMovementProgress());
        assertEquals(Direction.RIGHT.getRotation(), player.getPlayerRotation());
    }

    @Test
    void movesPlayerUpWhenUpKeyPressed() {
        when(Gdx.input.isKeyPressed(Input.Keys.UP)).thenReturn(true);

        handler.handleInput();

        assertEquals(new GridPoint2(1, 2), player.getPlayerDestinationCoordinates());
        assertEquals(Direction.UP.getRotation(), player.getPlayerRotation());
    }

    @Test
    void doesNotMoveIfObstacleInFront() {
        // Ставим препятствие перед игроком
        obstacle = new GridPoint2(2, 1);
        handler = new PlayerMovementInputHandler(player, obstacle);

        when(Gdx.input.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

        handler.handleInput();

        // Координаты не меняются, потому что там дерево
        assertEquals(new GridPoint2(1, 1), player.getPlayerDestinationCoordinates());
        assertEquals(Direction.RIGHT.getRotation(), player.getPlayerRotation());
    }

    @Test
    void ignoresInputIfStillMoving() {
        // Симулируем незавершённое движение
        player.resetMovementProgress(); // 0f — в процессе

        when(Gdx.input.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

        handler.handleInput();

        // Координаты не меняются
        assertEquals(new GridPoint2(1, 1), player.getPlayerDestinationCoordinates());
    }

    @Test
    void onlyOneDirectionHandledAtATime() {
        // Нажаты сразу две клавиши
        when(Gdx.input.isKeyPressed(Input.Keys.UP)).thenReturn(true);
        when(Gdx.input.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);

        handler.handleInput();

        // Проверяем, что обработана только первая по порядку (UP)
        assertEquals(new GridPoint2(1, 2), player.getPlayerDestinationCoordinates());
    }

    @Test
    void doesNotMoveIfNoKeyPressed() {
        // Все клавиши отпущены
        when(Gdx.input.isKeyPressed(anyInt())).thenReturn(false);

        handler.handleInput();

        assertEquals(new GridPoint2(1, 1), player.getPlayerDestinationCoordinates());
        assertEquals(1f, player.getPlayerMovementProgress());
    }
}
