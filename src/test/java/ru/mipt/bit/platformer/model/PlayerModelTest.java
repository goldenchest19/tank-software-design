package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerModelTest {

    @Test
    void testInitialState() {
        PlayerModel player = new PlayerModel();
        assertEquals(new GridPoint2(1, 1), player.getPlayerCoordinates());
        assertEquals(1f, player.getPlayerMovementProgress());
        assertEquals(0f, player.getPlayerRotation());
    }

    @Test
    void testResetMovementProgress() {
        PlayerModel player = new PlayerModel();
        player.resetMovementProgress();
        assertEquals(0f, player.getPlayerMovementProgress());
    }

    @Test
    void testSetRotation() {
        PlayerModel player = new PlayerModel();
        player.setPlayerRotation(45f);
        assertEquals(45f, player.getPlayerRotation());
    }

    @Test
    void testDestinationCoordinatesUpdate() {
        PlayerModel player = new PlayerModel();
        player.getPlayerDestinationCoordinates().add(1, 0);
        assertEquals(new GridPoint2(2, 1), player.getPlayerDestinationCoordinates());
    }
}
