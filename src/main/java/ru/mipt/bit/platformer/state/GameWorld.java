package ru.mipt.bit.platformer.state;

import ru.mipt.bit.platformer.input.CompositeInputHandler;
import ru.mipt.bit.platformer.model.BaseModel;
import ru.mipt.bit.platformer.model.Movable;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates all game-logic state and updates it independently from rendering.
 */
public class GameWorld {

    private final Movable player;
    private final List<Movable> aiTanks;
    private final List<BaseModel> obstacles;
    private final OccupiedCells occupiedCells;
    private final CompositeInputHandler inputHandler;

    public GameWorld(
            Movable player,
            List<Movable> aiTanks,
            List<BaseModel> obstacles,
            OccupiedCells occupiedCells,
            CompositeInputHandler inputHandler
    ) {
        this.player = player;
        this.aiTanks = new ArrayList<>(aiTanks);
        this.obstacles = new ArrayList<>(obstacles);
        this.occupiedCells = occupiedCells;
        this.inputHandler = inputHandler;
    }

    public void update(float deltaTime) {
        inputHandler.handleInput();

        updateMovable(player, deltaTime);
        aiTanks.forEach(tank -> updateMovable(tank, deltaTime));
    }

    private void updateMovable(Movable movable, float deltaTime) {
        movable.updateProgress(deltaTime);
        occupiedCells.syncWithMovement(movable);
    }
}
