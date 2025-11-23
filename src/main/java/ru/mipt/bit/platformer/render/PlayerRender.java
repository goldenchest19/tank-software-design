package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Movable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class PlayerRender implements MovableRenderer {
    private static final String IMAGES_TANK_BLUE_PNG = "images/tank_blue.png";
    private static final String IMAGES_TANK_RED_PNG = "images/red_tank.png";
    /**
     * Текстура танка
     */
    private final Texture tankTexture;

    /**
     * Графика танка
     */
    private final TextureRegion playerGraphics;

    /**
     * Прямоугольник, описывающий положение и размеры игрока на экране
     */
    private final Rectangle playerRectangle;

    public PlayerRender() {
        this(IMAGES_TANK_BLUE_PNG);
    }

    public PlayerRender(boolean useRedTexture) {
        this(useRedTexture ? IMAGES_TANK_RED_PNG : IMAGES_TANK_BLUE_PNG);
    }

    public PlayerRender(String texturePath) {
        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        tankTexture = new Texture(texturePath);
        // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
        playerGraphics = new TextureRegion(tankTexture);
        playerRectangle = createBoundingRectangle(playerGraphics);
    }

    @Override
    public Rectangle getPlayerRectangle() {
        return playerRectangle;
    }

    @Override
    public void dispose() {
        tankTexture.dispose();
    }

    @Override
    public void render(Batch batch, Movable playerModel) {
        drawTextureRegionUnscaled(batch, playerGraphics, playerRectangle, playerModel.getPlayerRotation());
    }
}
