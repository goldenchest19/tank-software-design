package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface Movable extends MovingEntity {
    GridPoint2 getPlayerDestinationCoordinates();
    float getPlayerMovementProgress();
    void resetMovementProgress();
    void setPlayerRotation(float rotation);
    float getPlayerRotation();
    void updateProgress(float deltaTime);
    int getHealth();
    int getMaxHealth();
    void applyDamage(int damage);
    boolean isDestroyed();

    @Override
    default GridPoint2 getDestinationCoordinates() {
        return getPlayerDestinationCoordinates();
    }

    @Override
    default float getRotation() {
        return getPlayerRotation();
    }

    @Override
    default float getMovementProgress() {
        return getPlayerMovementProgress();
    }
}