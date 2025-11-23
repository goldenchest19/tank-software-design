package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.render.HealthBarVisibility;

public class ToggleHealthBarCommand implements Command {

    private final HealthBarVisibility healthBarVisibility;

    public ToggleHealthBarCommand(HealthBarVisibility healthBarVisibility) {
        this.healthBarVisibility = healthBarVisibility;
    }

    @Override
    public boolean execute() {
        healthBarVisibility.toggle();
        return true;
    }
}
