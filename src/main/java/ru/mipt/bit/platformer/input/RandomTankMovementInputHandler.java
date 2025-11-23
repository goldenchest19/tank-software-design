package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.level.LevelBounds;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.state.OccupiedCells;
import ru.mipt.bit.platformer.state.ProjectileSpawner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public class RandomTankMovementInputHandler implements GameInputHandler {

    private final Movable tank;
    private final OccupiedCells occupiedCells;
    private final LevelBounds levelBounds;
    private final ProjectileSpawner projectileSpawner;
    private final Random random = new Random();

    public RandomTankMovementInputHandler(
            Movable tank,
            OccupiedCells occupiedCells,
            LevelBounds levelBounds,
            ProjectileSpawner projectileSpawner
    ) {
        this.tank = tank;
        this.occupiedCells = occupiedCells;
        this.levelBounds = levelBounds;
        this.projectileSpawner = projectileSpawner;
    }

    @Override
    public void handleInput() {
        if (!isEqual(tank.getPlayerMovementProgress(), 1f)) {
            return;
        }

        List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(directions, random);

        if (random.nextFloat() < 0.25f) {
            Direction direction = directions.get(0);
            tank.setPlayerRotation(direction.getRotation());
            new ShootCommand(projectileSpawner, tank, direction).execute();
            return;
        }

        for (Direction direction : directions) {
            MoveCommand command = new MoveCommand(tank, direction, occupiedCells, levelBounds);
            if (command.execute()) {
                break;
            }
        }
    }
}
