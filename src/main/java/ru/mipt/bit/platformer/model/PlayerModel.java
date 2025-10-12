package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * Класс, представляющий игрока в игре
 */
public class PlayerModel extends BaseModel implements Movable {
    /**
     * Скорость перемещения игрока между клетками (в секундах)
     */
    private static final float MOVEMENT_SPEED = 0.4f;

    /**
     * Координаты клетки, к которой движется игрок
     */
    private final GridPoint2 playerDestinationCoordinates;

    /**
     * Угол поворота игрока (в градусах)
     */
    private float playerRotation;

    /**
     * Прогресс перемещения игрока между клетками (от 0 до 1)
     */
    private float playerMovementProgress = 1f;

    public PlayerModel(GridPoint2 initialCoordinates) {
        super(initialCoordinates);
        playerDestinationCoordinates = new GridPoint2(initialCoordinates);
        playerRotation = 0f;
    }

    /**
     * Обновляет прогресс движения игрока между клетками
     */
    public void updateProgress() {
        // время, прошедшее с прошлого кадра
        float deltaTime = Gdx.graphics.getDeltaTime();

        playerMovementProgress = continueProgress(playerMovementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(playerMovementProgress, 1f)) {
            // record that the player has reached his/her destination
            getCoordinates().set(playerDestinationCoordinates);
        }
    }

    public void resetMovementProgress() {
        this.playerMovementProgress = 0f;
    }

    public void setPlayerRotation(float rotation) {
        this.playerRotation = rotation;
    }

    public GridPoint2 getPlayerDestinationCoordinates() {
        return playerDestinationCoordinates;
    }

    public float getPlayerRotation() {
        return playerRotation;
    }

    public float getPlayerMovementProgress() {
        return playerMovementProgress;
    }
}
