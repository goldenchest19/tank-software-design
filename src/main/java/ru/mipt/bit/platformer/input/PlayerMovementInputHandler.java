package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.render.GameMap;

import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.isEqual;

/**
 * Обработчик перемещения игрока. Проверяет столкновения со множеством препятствий.
 */
public class PlayerMovementInputHandler implements GameInputHandler {

    private final Movable player;
    private final Set<GridPoint2> obstacleCoordinates;
    private final GameMap gameMap;

    public PlayerMovementInputHandler(Movable player, Set<GridPoint2> obstacleCoordinates, GameMap gameMap) {
        this.player = player;
        this.obstacleCoordinates = obstacleCoordinates;
        this.gameMap = gameMap;
    }

    @Override
    public void handleInput() {
        for (Direction direction : Direction.values()) {
            if (direction.isPressed() && isEqual(player.getPlayerMovementProgress(), 1f)) {
                GridPoint2 next = new GridPoint2(player.getCoordinates()).add(direction.getDelta());

                if (next.x < 0 || next.x >= gameMap.getGroundLayer().getWidth() || next.y < 0 || next.y >= gameMap.getGroundLayer().getHeight()) {
                    player.setPlayerRotation(direction.getRotation());
                    break;
                }

                // Проверка на коллизию с любым препятствием
                if (!obstacleCoordinates.contains(next)) {
                    player.getPlayerDestinationCoordinates().set(next);
                    player.resetMovementProgress();
                }

                player.setPlayerRotation(direction.getRotation());
                break;
            }
        }
    }
}