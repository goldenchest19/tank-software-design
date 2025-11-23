package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.render.GameMap;
import ru.mipt.bit.platformer.state.OccupiedCells;

/**
 * Обработчик перемещения игрока. Проверяет столкновения со множеством препятствий.
 */
public class PlayerMovementInputHandler implements GameInputHandler {

    private final Movable player;
    private final OccupiedCells occupiedCells;
    private final GameMap gameMap;

    public PlayerMovementInputHandler(Movable player, OccupiedCells occupiedCells, GameMap gameMap) {
        this.player = player;
        this.occupiedCells = occupiedCells;
        this.gameMap = gameMap;
    }

    @Override
    public void handleInput() {
        for (Direction direction : Direction.values()) {
            if (direction.isPressed()) {
                MoveCommand command = new MoveCommand(player, direction, occupiedCells, gameMap);
                command.execute();
                break;
            }
        }
    }
}