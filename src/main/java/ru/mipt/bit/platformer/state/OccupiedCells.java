package ru.mipt.bit.platformer.state;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Movable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public class OccupiedCells {

    private final Map<Movable, Reservation> reservations = new HashMap<>();
    private final Set<GridPoint2> staticObstacles;

    public OccupiedCells(Set<GridPoint2> staticObstacles) {
        this.staticObstacles = new HashSet<>(staticObstacles);
    }

    public void registerStanding(Movable movable) {
        reservations.put(movable, Reservation.singleCell(movable.getCoordinates()));
    }

    public boolean canMoveTo(Movable movable, GridPoint2 destination) {
        if (staticObstacles.contains(destination)) {
            return false;
        }

        for (Map.Entry<Movable, Reservation> entry : reservations.entrySet()) {
            if (entry.getKey() == movable) {
                continue;
            }
            if (entry.getValue().occupies(destination)) {
                return false;
            }
        }
        return true;
    }

    public void startMove(Movable movable, GridPoint2 destination) {
        reservations.put(movable, Reservation.moving(movable.getCoordinates(), destination));
    }

    public void syncWithMovement(Movable movable) {
        if (isEqual(movable.getPlayerMovementProgress(), 1f)) {
            reservations.put(movable, Reservation.singleCell(movable.getCoordinates()));
        }
    }

    public void remove(Movable movable) {
        reservations.remove(movable);
    }

    private static class Reservation {
        private final GridPoint2 from;
        private final GridPoint2 to;

        Reservation(GridPoint2 from, GridPoint2 to) {
            this.from = new GridPoint2(from);
            this.to = new GridPoint2(to);
        }

        static Reservation singleCell(GridPoint2 coordinates) {
            return new Reservation(coordinates, coordinates);
        }

        static Reservation moving(GridPoint2 from, GridPoint2 to) {
            return new Reservation(from, to);
        }

        boolean occupies(GridPoint2 point) {
            return from.equals(point) || to.equals(point);
        }
    }
}
