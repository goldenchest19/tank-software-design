package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.PlayerModel;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class PlayerRender {
    private static final String IMAGES_TANK_BLUE_PNG = "images/tank_blue.png";
    /**
     * Текстура танка
     */
    private final Texture blueTankTexture;

    /**
     * Графика танка
     */
    private final TextureRegion playerGraphics;

    /**
     * Прямоугольник, описывающий положение и размеры игрока на экране
     */
    private final Rectangle playerRectangle;

    public PlayerRender() {
        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        blueTankTexture = new Texture(IMAGES_TANK_BLUE_PNG);
        // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
        playerGraphics = new TextureRegion(blueTankTexture);
        playerRectangle = createBoundingRectangle(playerGraphics);
    }

    public Rectangle getPlayerRectangle() {
        return playerRectangle;
    }

    public void blueTankTextureDispose() {
        blueTankTexture.dispose();
    }

    public TextureRegion getPlayerGraphics() {
        return playerGraphics;
    }

    public void render(Batch batch, PlayerModel playerModel) {
        drawTextureRegionUnscaled(batch, playerGraphics, playerRectangle, playerModel.getPlayerRotation());
    }
}
