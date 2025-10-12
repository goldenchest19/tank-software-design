package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.PlayerModel;

import static com.badlogic.gdx.math.MathUtils.isEqual;

/**
 * Обработчик нажатия клавиш, отвечающих за перемещение игрока.
 */
public class PlayerMovementInputHandler implements GameInputHandler {

    private final PlayerModel player;
    private final GridPoint2 obstacleCoordinates;

    public PlayerMovementInputHandler(PlayerModel player, GridPoint2 obstacleCoordinates) {
        this.player = player;
        this.obstacleCoordinates = obstacleCoordinates;
    }

    @Override
    public void handleInput() {
        for (Direction direction : Direction.values()) {
            if (direction.isPressed() && isEqual(player.getPlayerMovementProgress(), 1f)) {
                GridPoint2 next = new GridPoint2(player.getPlayerCoordinates()).add(direction.getDelta());

                // Проверка на коллизию с препятствием
                if (!obstacleCoordinates.equals(next)) {
                    player.getPlayerDestinationCoordinates().set(next);
                    player.resetMovementProgress();
                }

                player.setPlayerRotation(direction.getRotation());
                break;
            }
        }
    }
}