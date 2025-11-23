package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Movable;

public interface MovableRenderer {
    Rectangle getPlayerRectangle();

    void render(Batch batch, Movable playerModel);

    void dispose();
}
