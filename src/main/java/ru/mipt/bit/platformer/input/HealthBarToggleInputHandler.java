package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.command.Command;

public class HealthBarToggleInputHandler implements GameInputHandler {

    private final Command toggleCommand;

    public HealthBarToggleInputHandler(Command toggleCommand) {
        this.toggleCommand = toggleCommand;
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            toggleCommand.execute();
        }
    }
}
