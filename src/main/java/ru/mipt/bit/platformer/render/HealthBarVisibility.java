package ru.mipt.bit.platformer.render;

public class HealthBarVisibility {

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void toggle() {
        visible = !visible;
    }
}
