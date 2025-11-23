package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.level.LevelBounds;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.state.OccupiedCells;

/**
 * Обработчик перемещения игрока. Проверяет столкновения со множеством препятствий.
 */
public class PlayerMovementInputHandler implements GameInputHandler {

    private final Movable player;
    private final OccupiedCells occupiedCells;
    private final LevelBounds levelBounds;

    public PlayerMovementInputHandler(Movable player, OccupiedCells occupiedCells, LevelBounds levelBounds) {
        this.player = player;
        this.occupiedCells = occupiedCells;
        this.levelBounds = levelBounds;
    }

    @Override
    public void handleInput() {
        for (Direction direction : Direction.values()) {
            if (direction.isPressed()) {
                MoveCommand command = new MoveCommand(player, direction, occupiedCells, levelBounds);
                command.execute();
                break;
            }
        }
    }
}