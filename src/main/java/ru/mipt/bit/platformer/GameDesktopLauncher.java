package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.GameMap;
import ru.mipt.bit.platformer.model.GreenTreeModel;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.render.GreenTreeRender;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;


public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private GameMap gameMap;
    private Tank tank;
    private Player player;
    private GreenTreeModel greenTreeModel;
    private GreenTreeRender greenTreeRender;

    /**
     * Вызывается один раз при запуске игры
     * Загружает ресурсы (текстуры, карту), инициализирует объекты игрока и препятствий
     */
    @Override
    public void create() {
        batch = new SpriteBatch();

        gameMap = new GameMap(batch);
        tank = new Tank();
        player = new Player();

        greenTreeModel = new GreenTreeModel(new GridPoint2(1, 3));
        greenTreeRender = new GreenTreeRender(gameMap, greenTreeModel.getTreeObstacleCoordinates());
    }

    @Override
    public void render() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Передаем координаты препятствия для проверки коллизий
        player.movePlayer(greenTreeModel.getTreeObstacleCoordinates());

        // calculate interpolated player screen coordinates
        gameMap.moveRectangleBetweenTileCenters(tank.getPlayerRectangle(), player.getPlayerCoordinates(),
                player.getPlayerDestinationCoordinates(), player.getPlayerMovementProgress());

        player.updateProgress(deltaTime);

        // clear the screen
        clearScreen();

        // render each tile of the level
        gameMap.render();

        // start recording all drawing commands
        batch.begin();

        // render player
        drawTextureRegionUnscaled(batch, tank.getPlayerGraphics(), tank.getPlayerRectangle(), player.getPlayerRotation());

        // render tree obstacle
        greenTreeRender.render(batch);

        // submit all drawing requests
        batch.end();
    }

    /**
     * Освобождает все ресурсы, загруженные в create()
     */
    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        greenTreeRender.dispose();
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

    private static void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }
}
