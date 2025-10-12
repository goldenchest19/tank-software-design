package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class GreenTreeRender {

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
     * Прямоугольник, описывающий позицию и размеры дерева, используется для проверки коллизий
     */
    private final Rectangle treeObstacleRectangle;

    public GreenTreeRender(GameMap gameMap, GridPoint2 treePosition) {
        this.greenTreeTexture = new Texture(Gdx.files.internal(IMAGES_GREEN_TREE_PNG));
        ;
        this.treeObstacleGraphics = new TextureRegion(greenTreeTexture);
        this.treeObstacleRectangle = new Rectangle()
                .setWidth(treeObstacleGraphics.getRegionWidth())
                .setHeight(treeObstacleGraphics.getRegionHeight());
        moveRectangleAtTileCenter(gameMap.getGroundLayer(), treeObstacleRectangle, treePosition);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, treeObstacleGraphics, treeObstacleRectangle, 0f);
    }

    public void dispose() {
        greenTreeTexture.dispose();
    }
}
