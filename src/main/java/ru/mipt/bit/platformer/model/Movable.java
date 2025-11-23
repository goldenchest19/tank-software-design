package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface Movable {
    GridPoint2 getCoordinates();
    GridPoint2 getPlayerDestinationCoordinates();
    float getPlayerMovementProgress();
    void resetMovementProgress();
    void setPlayerRotation(float rotation);
    float getPlayerRotation();
    void updateProgress(float deltaTime);
}