package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Movable;

public class HealthBarRenderDecorator implements MovableRenderer {

    private static final float HEALTH_BAR_HEIGHT = 6f;
    private static final float HEALTH_BAR_VERTICAL_MARGIN = 4f;

    private final MovableRenderer delegate;
    private final HealthBarVisibility healthBarVisibility;
    private final Texture pixelTexture;

    public HealthBarRenderDecorator(MovableRenderer delegate, HealthBarVisibility healthBarVisibility) {
        this.delegate = delegate;
        this.healthBarVisibility = healthBarVisibility;
        this.pixelTexture = createPixelTexture();
    }

    @Override
    public Rectangle getPlayerRectangle() {
        return delegate.getPlayerRectangle();
    }

    @Override
    public void render(Batch batch, Movable playerModel) {
        delegate.render(batch, playerModel);
        if (healthBarVisibility.isVisible()) {
            drawHealthBar(batch, playerModel);
        }
    }

    private void drawHealthBar(Batch batch, Movable playerModel) {
        Rectangle playerRectangle = getPlayerRectangle();
        float x = playerRectangle.getX();
        float y = playerRectangle.getY() + playerRectangle.getHeight() + HEALTH_BAR_VERTICAL_MARGIN;
        float width = playerRectangle.getWidth();

        float healthPercentage = (float) playerModel.getHealth() / playerModel.getMaxHealth();
        float filledWidth = width * healthPercentage;

        batch.setColor(Color.DARK_GRAY);
        batch.draw(pixelTexture, x, y, width, HEALTH_BAR_HEIGHT);

        batch.setColor(Color.GREEN);
        batch.draw(pixelTexture, x, y, filledWidth, HEALTH_BAR_HEIGHT);

        batch.setColor(Color.WHITE);
    }

    private Texture createPixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void dispose() {
        delegate.dispose();
        pixelTexture.dispose();
    }
}
