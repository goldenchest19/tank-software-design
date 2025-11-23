package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class BulletModel extends BaseModel implements MovingEntity {
    private static final float MOVEMENT_SPEED = 0.15f;
    private static final float ROTATION_UP = 90f;
    private static final float ROTATION_DOWN = -90f;
    private static final float ROTATION_LEFT = 180f;

    private final Direction direction;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress;
    private boolean removed;

    public BulletModel(GridPoint2 start, Direction direction) {
        super(start);
        this.direction = direction;
        this.destinationCoordinates = new GridPoint2(start).add(direction.getDelta());
        this.movementProgress = 0f;
    }

    public void updateProgress(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(movementProgress, 1f)) {
            getCoordinates().set(destinationCoordinates);
        }
    }

    public void resetMovementProgress() {
        movementProgress = 0f;
    }

    public void prepareNextDestination() {
        destinationCoordinates.set(getCoordinates()).add(direction.getDelta());
    }

    public Direction getDirection() {
        return direction;
    }

    public void markRemoved() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        switch (direction) {
            case UP:
                return ROTATION_UP;
            case DOWN:
                return ROTATION_DOWN;
            case LEFT:
                return ROTATION_LEFT;
            default:
                return 0f;
        }
    }
}
