package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.render.GameMap;
import ru.mipt.bit.platformer.state.OccupiedCells;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public class MoveCommand implements Command {

    private final Movable movable;
    private final Direction direction;
    private final OccupiedCells occupiedCells;
    private final GameMap gameMap;

    public MoveCommand(Movable movable, Direction direction, OccupiedCells occupiedCells, GameMap gameMap) {
        this.movable = movable;
        this.direction = direction;
        this.occupiedCells = occupiedCells;
        this.gameMap = gameMap;
    }

    @Override
    public boolean execute() {
        movable.setPlayerRotation(direction.getRotation());

        if (!isEqual(movable.getPlayerMovementProgress(), 1f)) {
            return false;
        }

        GridPoint2 next = new GridPoint2(movable.getCoordinates()).add(direction.getDelta());

        if (next.x < 0 || next.x >= gameMap.getGroundLayer().getWidth()
                || next.y < 0 || next.y >= gameMap.getGroundLayer().getHeight()) {
            return false;
        }

        if (!occupiedCells.canMoveTo(movable, next)) {
            return false;
        }

        occupiedCells.startMove(movable, next);
        movable.getPlayerDestinationCoordinates().set(next);
        movable.resetMovementProgress();
        return true;
    }
}
