package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.GameMap;
import ru.mipt.bit.platformer.model.GreenTree;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.Tank;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;


public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private GameMap gameMap;
    private Tank tank;
    private GreenTree greenTree;
    private Player player;

    /**
     * Вызывается один раз при запуске игры
     * Загружает ресурсы (текстуры, карту), инициализирует объекты игрока и препятствий
     */
    @Override
    public void create() {
        batch = new SpriteBatch();

        gameMap = new GameMap(batch);
        tank = new Tank();
        greenTree = new GreenTree(new GridPoint2(1, 3));
        player = new Player();

        moveRectangleAtTileCenter(gameMap.getGroundLayer(),
                greenTree.getTreeObstacleRectangle(), greenTree.getTreeObstacleCoordinates());
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        player.movePlayer(greenTree.getTreeObstacleCoordinates());

        // calculate interpolated player screen coordinates
        gameMap.moveRectangleBetweenTileCenters(tank.getPlayerRectangle(), player.getPlayerCoordinates(),
                player.getPlayerDestinationCoordinates(), player.getPlayerMovementProgress());

        player.updateProgress(deltaTime);

        // render each tile of the level
        gameMap.render();

        // start recording all drawing commands
        batch.begin();

        // render player
        drawTextureRegionUnscaled(batch, tank.getPlayerGraphics(), tank.getPlayerRectangle(), player.getPlayerRotation());

        // render tree obstacle
        drawTextureRegionUnscaled(batch, greenTree.getTreeObstacleGraphics(), greenTree.getTreeObstacleRectangle(), 0f);

        // submit all drawing requests
        batch.end();
    }

    /**
     * Освобождает все ресурсы, загруженные в create()
     */
    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        greenTree.greenTreeTextureDispose();
        tank.blueTankTextureDispose();
        gameMap.levelDispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }
}
