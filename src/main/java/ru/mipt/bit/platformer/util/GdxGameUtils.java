package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.math.MathUtils.clamp;

public final class GdxGameUtils {

    private GdxGameUtils() {
    }

    /**
     * Перемещает прямоугольник в центр указанной клетки
     *
     * @param tileLayer       слой карты
     * @param rectangle       прямоугольник
     * @param tileCoordinates координаты клетки
     * @return прямоугольник, центрированный по клетке
     */
    public static Rectangle moveRectangleAtTileCenter(TiledMapTileLayer tileLayer, Rectangle rectangle, GridPoint2 tileCoordinates) {
        Vector2 tileCenter = calculateTileCenter(tileLayer, tileCoordinates);
        return rectangle.setCenter(tileCenter);
    }

    public static GridPoint2 incrementedY(GridPoint2 point) {
        return new GridPoint2(point).add(0, 1);
    }

    public static GridPoint2 decrementedX(GridPoint2 point) {
        return new GridPoint2(point).sub(1, 0);
    }

    public static GridPoint2 decrementedY(GridPoint2 point) {
        return new GridPoint2(point).sub(0, 1);
    }

    public static GridPoint2 incrementedX(GridPoint2 point) {
        return new GridPoint2(point).add(1, 0);
    }

    public static void drawTextureRegionUnscaled(Batch batch, TextureRegion region, Rectangle rectangle, float rotation) {
        int regionWidth = region.getRegionWidth();
        int regionHeight = region.getRegionHeight();
        float regionOriginX = regionWidth / 2f;
        float regionOriginY = regionHeight / 2f;
        batch.draw(region, rectangle.x, rectangle.y, regionOriginX, regionOriginY, regionWidth, regionHeight, 1f, 1f, rotation);
    }

    public static Rectangle createBoundingRectangle(TextureRegion region) {
        return new Rectangle()
                .setWidth(region.getRegionWidth())
                .setHeight(region.getRegionHeight());
    }

    public static float continueProgress(float previousProgress, float deltaTime, float speed) {
        return clamp(previousProgress + deltaTime / speed, 0f, 1f);
    }

    private static Vector2 calculateTileCenter(TiledMapTileLayer tileLayer, GridPoint2 tileCoordinates) {
        int tileWidth = tileLayer.getTileWidth();
        int tileHeight = tileLayer.getTileHeight();
        int tileBottomLeftCornerX = tileCoordinates.x * tileWidth;
        int tileBottomLeftCornerY = tileCoordinates.y * tileHeight;

        return new Rectangle()
                .setX(tileBottomLeftCornerX)
                .setY(tileBottomLeftCornerY)
                .setWidth(tileWidth)
                .setHeight(tileHeight)
                .getCenter(new Vector2());
    }
}
