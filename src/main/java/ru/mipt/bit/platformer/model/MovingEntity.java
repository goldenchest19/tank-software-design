package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface MovingEntity {
    GridPoint2 getCoordinates();

    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    float getRotation();
}
