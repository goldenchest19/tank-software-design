package ru.mipt.bit.platformer.state;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.input.CompositeInputHandler;
import ru.mipt.bit.platformer.level.LevelBounds;
import ru.mipt.bit.platformer.model.BaseModel;
import ru.mipt.bit.platformer.model.BulletModel;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import java.util.*;

import static com.badlogic.gdx.math.MathUtils.isEqual;

/**
 * Encapsulates all game-logic state and updates it independently from rendering.
 */
public class GameWorld implements ProjectileSpawner {

    private static final int BULLET_DAMAGE = 34;

    private final Movable player;
    private final List<Movable> aiTanks;
    private final List<BaseModel> obstacles;
    private final List<BulletModel> bullets = new ArrayList<>();
    private final OccupiedCells occupiedCells;
    private final CompositeInputHandler inputHandler;
    private final List<WorldObserver> observers = new ArrayList<>();
    private final LevelBounds levelBounds;
    private boolean playerRemoved;

    public GameWorld(
            Movable player,
            List<Movable> aiTanks,
            List<BaseModel> obstacles,
            OccupiedCells occupiedCells,
            CompositeInputHandler inputHandler,
            LevelBounds levelBounds
    ) {
        this.player = player;
        this.aiTanks = new ArrayList<>(aiTanks);
        this.obstacles = new ArrayList<>(obstacles);
        this.occupiedCells = occupiedCells;
        this.inputHandler = inputHandler;
        this.levelBounds = levelBounds;
    }

    public void addObserver(WorldObserver observer) {
        observers.add(observer);
        publishExistingObjects(observer);
    }

    public void update(float deltaTime) {
        inputHandler.handleInput();

        updateMovable(player, deltaTime);
        aiTanks.forEach(tank -> updateMovable(tank, deltaTime));
        bullets.forEach(bullet -> bullet.updateProgress(deltaTime));

        processBulletArrivals();
        cleanupRemovedObjects();
    }

    @Override
    public boolean spawnBullet(Movable shooter, Direction direction) {
        if (direction == null || shooter.isDestroyed()) {
            return false;
        }
        GridPoint2 start = new GridPoint2(shooter.getCoordinates()).add(direction.getDelta());
        if (!levelBounds.contains(start)) {
            return false;
        }

        BulletModel bullet = new BulletModel(start, direction);
        if (resolveCollision(bullet)) {
            return true;
        }

        bullets.add(bullet);
        notifyAdded(bullet);
        return true;
    }

    private void updateMovable(Movable movable, float deltaTime) {
        if (movable.isDestroyed()) {
            return;
        }
        movable.updateProgress(deltaTime);
        occupiedCells.syncWithMovement(movable);
    }

    private void processBulletArrivals() {
        for (BulletModel bullet : bullets) {
            if (bullet.isRemoved()) {
                continue;
            }
            if (!isEqual(bullet.getMovementProgress(), 1f)) {
                continue;
            }

            if (resolveCollision(bullet)) {
                continue;
            }

            GridPoint2 nextStep = new GridPoint2(bullet.getCoordinates()).add(bullet.getDirection().getDelta());
            if (!levelBounds.contains(nextStep)) {
                bullet.markRemoved();
                continue;
            }
            bullet.resetMovementProgress();
            bullet.prepareNextDestination();
        }
    }

    private void cleanupRemovedObjects() {
        removeDestroyedTanks();
        List<BulletModel> removed = new ArrayList<>();
        for (BulletModel bullet : bullets) {
            if (bullet.isRemoved()) {
                removed.add(bullet);
            }
        }
        bullets.removeAll(removed);
        removed.forEach(this::notifyRemoved);
    }

    private void removeDestroyedTanks() {
        if (player.isDestroyed() && !playerRemoved) {
            occupiedCells.remove(player);
            notifyRemoved((BaseModel) player);
            playerRemoved = true;
        }

        List<Movable> destroyed = new ArrayList<>();
        for (Movable tank : aiTanks) {
            if (tank.isDestroyed()) {
                destroyed.add(tank);
            }
        }

        destroyed.forEach(tank -> {
            occupiedCells.remove(tank);
            notifyRemoved((BaseModel) tank);
        });
        aiTanks.removeAll(destroyed);
    }

    private boolean resolveCollision(BulletModel bullet) {
        if (isObstacleAt(bullet.getCoordinates())) {
            bullet.markRemoved();
            return true;
        }

        Map<GridPoint2, List<Movable>> tanksByCell = collectLivingTanks();
        List<Movable> victims = tanksByCell.getOrDefault(bullet.getCoordinates(), Collections.emptyList());
        if (!victims.isEmpty()) {
            victims.forEach(tank -> tank.applyDamage(BULLET_DAMAGE));
            bullet.markRemoved();
            return true;
        }

        boolean collidedWithBullet = bullets.stream()
                .anyMatch(other -> other != bullet
                        && !other.isRemoved()
                        && other.getCoordinates().equals(bullet.getCoordinates()));
        if (collidedWithBullet) {
            bullet.markRemoved();
            bullets.stream()
                    .filter(other -> other != bullet && other.getCoordinates().equals(bullet.getCoordinates()))
                    .forEach(BulletModel::markRemoved);
            return true;
        }

        return false;
    }

    private Map<GridPoint2, List<Movable>> collectLivingTanks() {
        Map<GridPoint2, List<Movable>> tanks = new HashMap<>();
        addTank(tanks, player);
        for (Movable tank : aiTanks) {
            addTank(tanks, tank);
        }
        return tanks;
    }

    private void addTank(Map<GridPoint2, List<Movable>> tanks, Movable movable) {
        if (movable.isDestroyed()) {
            return;
        }
        tanks.computeIfAbsent(movable.getCoordinates(), cell -> new ArrayList<>()).add(movable);
    }

    private boolean isObstacleAt(GridPoint2 point) {
        for (BaseModel obstacle : obstacles) {
            if (obstacle.getCoordinates().equals(point)) {
                return true;
            }
        }
        return false;
    }

    private void publishExistingObjects(WorldObserver observer) {
        observer.onObjectAdded((BaseModel) player);
        aiTanks.forEach(tank -> observer.onObjectAdded((BaseModel) tank));
        obstacles.forEach(observer::onObjectAdded);
        bullets.forEach(observer::onObjectAdded);
    }

    private void notifyAdded(BaseModel model) {
        observers.forEach(observer -> observer.onObjectAdded(model));
    }

    private void notifyRemoved(BaseModel model) {
        observers.forEach(observer -> observer.onObjectRemoved(model));
    }
}
