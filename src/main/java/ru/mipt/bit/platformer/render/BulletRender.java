package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.BulletModel;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class BulletRender {
    private final Texture texture;
    private final TextureRegion region;
    private final Rectangle rectangle;

    public BulletRender() {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fill();
        this.texture = new Texture(pixmap);
        pixmap.dispose();
        this.region = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(region);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void render(Batch batch, BulletModel bulletModel) {
        drawTextureRegionUnscaled(batch, region, rectangle, bulletModel.getRotation());
    }

    public void dispose() {
        texture.dispose();
    }
}
