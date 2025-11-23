package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Movable;

public class TestMovable implements Movable {

    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    private float movementProgress = 1f;
    private float rotation;

    public TestMovable(int x, int y) {
        this.coordinates = new GridPoint2(x, y);
        this.destination = new GridPoint2(x, y);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public GridPoint2 getPlayerDestinationCoordinates() {
        return destination;
    }

    @Override
    public float getPlayerMovementProgress() {
        return movementProgress;
    }

    @Override
    public void resetMovementProgress() {
        movementProgress = 0f;
    }

    @Override
    public void setPlayerRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public float getPlayerRotation() {
        return rotation;
    }

    @Override
    public void updateProgress(float deltaTime) {
        movementProgress = Math.min(1f, movementProgress + deltaTime);
    }

    public void setMovementProgress(float movementProgress) {
        this.movementProgress = movementProgress;
    }

    public void setCoordinates(int x, int y) {
        coordinates.set(x, y);
        destination.set(x, y);
    }
}
