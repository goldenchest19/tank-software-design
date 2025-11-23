package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

/**
 * Класс для управления плавным перемещением прямоугольников между клетками tiled-карты с использованием интерполяции
 */
public class TileMovement {

    /**
     * Слой tiled-карты, по которому осуществляется движение
     */
    private final TiledMapTileLayer tileLayer;

    /**
     * Тип интерполяции, используемый при перемещении
     */
    private final Interpolation interpolation;

    public TileMovement(TiledMapTileLayer tileLayer, Interpolation interpolation) {
        this.tileLayer = tileLayer;
        this.interpolation = interpolation;
    }

    /**
     * Перемещает прямоугольник между двумя клетками по заданному прогрессу
     *
     * @param rectangle           прямоугольник для перемещения
     * @param fromTileCoordinates координаты начальной клетки
     * @param toTileCoordinates   координаты конечной клетки
     * @param progress            значение от 0 до 1, определяющее прогресс перемещения
     * @return перемещённый прямоугольник
     */
    public Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle, GridPoint2 fromTileCoordinates, GridPoint2 toTileCoordinates, float progress) {
        moveRectangleAtTileCenter(tileLayer, rectangle, fromTileCoordinates);
        float fromTileBottomLeftX = rectangle.x;
        float fromTileBottomLeftY = rectangle.y;

        moveRectangleAtTileCenter(tileLayer, rectangle, toTileCoordinates);
        float toTileBottomLeftX = rectangle.x;
        float toTileBottomLeftY = rectangle.y;

        float intermediateBottomLeftX = interpolation.apply(fromTileBottomLeftX, toTileBottomLeftX, progress);
        float intermediateBottomLeftY = interpolation.apply(fromTileBottomLeftY, toTileBottomLeftY, progress);

        return rectangle
                .setX(intermediateBottomLeftX)
                .setY(intermediateBottomLeftY);
    }
}
