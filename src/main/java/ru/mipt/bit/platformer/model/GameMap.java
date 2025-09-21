package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.NoSuchElementException;

/**
 * Класс, инкапсулирующий tiled-карту уровня и операции с ней
 * Отвечает за:
 * - загрузку карты и слоев,
 * - создание рендерера карты,
 * - управление перемещением объектов между клетками
 */
public class GameMap {

    public static final String LEVEL_TMX = "level.tmx";
    /**
     * Загруженная tiled-карта уровня
     */
    private final TiledMap level;

    /**
     * Рендерер для отрисовки карты
     */
    private final MapRenderer levelRenderer;

    /**
     * Компонент для управления движением объектов по клеткам с интерполяцией
     */
    private final TileMovement tileMovement;

    /**
     * Слой карты, который используется для перемещений (например, земля)
     */
    private final TiledMapTileLayer groundLayer;

    public GameMap(Batch batch) {
        this.level = new TmxMapLoader().load(LEVEL_TMX);
        this.levelRenderer = createSingleLayerMapRenderer(level, batch);
        this.groundLayer = getSingleLayer(level);
        this.tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    /**
     * Отрисовка карты на экране
     * Вызывается каждый кадр в игровом цикле
     */
    public void render() {
        levelRenderer.render();
    }

    /**
     * Перемещает прямоугольник между центрами двух клеток с учётом интерполяции
     *
     * @param playerRectangle              прямоугольник объекта
     * @param playerCoordinates            текущие координаты объекта
     * @param playerDestinationCoordinates координаты клетки назначения
     * @param playerMovementProgress       прогресс перемещения от 0 до 1
     */
    public void moveRectangleBetweenTileCenters(Rectangle playerRectangle, GridPoint2 playerCoordinates,
                                                GridPoint2 playerDestinationCoordinates, float playerMovementProgress) {
        tileMovement.moveRectangleBetweenTileCenters(playerRectangle, playerCoordinates,
                playerDestinationCoordinates, playerMovementProgress);
    }

    /**
     * Освобождение ресурсов карты
     */
    public void levelDispose() {
        level.dispose();
    }

    /**
     * Создаёт рендерер для карты с одним слоем
     *
     * @param tiledMap карта уровня
     * @param batch    Batch для отрисовки
     * @return настроенный рендерер карты
     */
    private MapRenderer createSingleLayerMapRenderer(TiledMap tiledMap, Batch batch) {
        TiledMapTileLayer tileLayer = getSingleLayer(tiledMap);
        float viewWidth = tileLayer.getWidth() * tileLayer.getTileWidth();
        float viewHeight = tileLayer.getHeight() * tileLayer.getTileHeight();

        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        mapRenderer.getViewBounds().set(0f, 0f, viewWidth, viewHeight);

        return mapRenderer;
    }

    /**
     * Возвращает единственный слой карты
     *
     * @param map карта
     * @param <L> тип слоя
     * @return единственный слой карты
     * @throws NoSuchElementException   если слоёв нет
     * @throws IllegalArgumentException если слоёв больше одного
     */
    private <L extends MapLayer> L getSingleLayer(Map map) {
        MapLayers layers = map.getLayers();
        switch (layers.size()) {
            case 0:
                throw new NoSuchElementException("Map has no layers");
            case 1:
                @SuppressWarnings("unchecked")
                L layer = (L) layers.iterator().next();
                return layer;
            default:
                throw new IllegalArgumentException("Map has more than one layer");
        }
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }
}
