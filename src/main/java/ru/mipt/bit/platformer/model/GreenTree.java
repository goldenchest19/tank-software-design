package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

/**
 * Класс, представляющий дерево-препятствие на уровне
 * Инкапсулирует текстуру, графику, координаты и прямоугольник для коллизий
 */
public class GreenTree {

    private static final String IMAGES_GREEN_TREE_PNG = "images/greenTree.png";
    /**
     * Текстура дерева. Загружается из файла
     */
    private final Texture greenTreeTexture;

    /**
     * Графическое представление дерева (TextureRegion). Используется для рендеринга
     */
    private final TextureRegion treeObstacleGraphics;

    /**
     * Координаты клетки, в которой расположено дерево на карте
     */
    private final GridPoint2 treeObstacleCoordinates;

    /**
     * Прямоугольник, описывающий позицию и размеры дерева, используется для проверки коллизий
     */
    private final Rectangle treeObstacleRectangle;

    public GreenTree(GridPoint2 gridPoint2) {
        greenTreeTexture = new Texture(IMAGES_GREEN_TREE_PNG);
        treeObstacleGraphics = new TextureRegion(greenTreeTexture);
        treeObstacleCoordinates = gridPoint2;
        treeObstacleRectangle = createBoundingRectangle(treeObstacleGraphics);
    }

    public TextureRegion getTreeObstacleGraphics() {
        return treeObstacleGraphics;
    }

    public GridPoint2 getTreeObstacleCoordinates() {
        return treeObstacleCoordinates;
    }

    public Rectangle getTreeObstacleRectangle() {
        return treeObstacleRectangle;
    }

    public void greenTreeTextureDispose() {
        greenTreeTexture.dispose();
    }
}
