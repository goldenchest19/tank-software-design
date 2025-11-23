package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.state.ProjectileSpawner;

public class PlayerShootInputHandler implements GameInputHandler {
    private final Movable player;
    private final ProjectileSpawner projectileSpawner;

    public PlayerShootInputHandler(Movable player, ProjectileSpawner projectileSpawner) {
        this.player = player;
        this.projectileSpawner = projectileSpawner;
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Direction direction = Direction.fromRotation(player.getPlayerRotation());
            new ShootCommand(projectileSpawner, player, direction).execute();
        }
    }
}
