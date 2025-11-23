package ru.mipt.bit.platformer.state;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

public interface ProjectileSpawner {
    boolean spawnBullet(Movable shooter, Direction direction);
}
