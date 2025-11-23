package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.state.ProjectileSpawner;

public class ShootCommand implements Command {
    private final ProjectileSpawner projectileSpawner;
    private final Movable shooter;
    private final Direction direction;

    public ShootCommand(ProjectileSpawner projectileSpawner, Movable shooter, Direction direction) {
        this.projectileSpawner = projectileSpawner;
        this.shooter = shooter;
        this.direction = direction;
    }

    @Override
    public boolean execute() {
        return projectileSpawner.spawnBullet(shooter, direction);
    }
}
