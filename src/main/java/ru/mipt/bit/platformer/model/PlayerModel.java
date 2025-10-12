package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * Класс, представляющий игрока в игре
 */
public class PlayerModel {
    /**
     * Скорость перемещения игрока между клетками (в секундах)
     */
    private static final float MOVEMENT_SPEED = 0.4f;

    /**
     * Координаты клетки, к которой движется игрок
     */
    private final GridPoint2 playerDestinationCoordinates;

    /**
     * Координаты клетки, в которой находится игрок
     */
    private final GridPoint2 playerCoordinates;

    /**
     * Угол поворота игрока (в градусах)
     */
    private float playerRotation;

    /**
     * Прогресс перемещения игрока между клетками (от 0 до 1)
     */
    private float playerMovementProgress = 1f;

    public PlayerModel() {
        playerDestinationCoordinates = new GridPoint2(1, 1);
        playerCoordinates = new GridPoint2(playerDestinationCoordinates);
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
            playerCoordinates.set(playerDestinationCoordinates);
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

    public GridPoint2 getPlayerCoordinates() {
        return playerCoordinates;
    }

    public float getPlayerRotation() {
        return playerRotation;
    }

    public float getPlayerMovementProgress() {
        return playerMovementProgress;
    }
}
