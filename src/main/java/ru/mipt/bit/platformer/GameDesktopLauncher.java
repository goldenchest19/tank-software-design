package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.input.CompositeInputHandler;
import ru.mipt.bit.platformer.input.PlayerMovementInputHandler;
import ru.mipt.bit.platformer.model.GreenTreeModel;
import ru.mipt.bit.platformer.model.PlayerModel;
import ru.mipt.bit.platformer.render.GameMap;
import ru.mipt.bit.platformer.render.GreenTreeRender;
import ru.mipt.bit.platformer.render.PlayerRender;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private GameMap gameMap;
    private PlayerRender playerRender;
    private PlayerModel playerModel;
    private GreenTreeModel greenTreeModel;
    private GreenTreeRender greenTreeRender;
    private CompositeInputHandler inputHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();

        gameMap = new GameMap(batch);
        playerRender = new PlayerRender();
        playerModel = new PlayerModel();

        greenTreeModel = new GreenTreeModel(new GridPoint2(1, 3));
        greenTreeRender = new GreenTreeRender(gameMap, greenTreeModel.getTreeObstacleCoordinates());

        // Инициализация системы ввода
        inputHandler = new CompositeInputHandler();
        inputHandler.addHandler(new PlayerMovementInputHandler(playerModel, greenTreeModel.getTreeObstacleCoordinates()));
    }

    @Override
    public void render() {
        updateGameProgress();

        drawGraphicChanges();
    }

    /**
     * Освобождает все ресурсы, загруженные в create()
     */
    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        greenTreeRender.dispose();
        playerRender.blueTankTextureDispose();
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

    private void updateGameProgress() {
        // Теперь обработка ввода вынесена в отдельный класс
        inputHandler.handleInput();

        // calculate interpolated player screen coordinates
        gameMap.moveRectangleBetweenTileCenters(playerRender.getPlayerRectangle(), playerModel.getPlayerCoordinates(),
                playerModel.getPlayerDestinationCoordinates(), playerModel.getPlayerMovementProgress());

        playerModel.updateProgress();
    }

    private void drawGraphicChanges() {
        // clear the screen
        clearScreen();

        // render each tile of the level
        gameMap.render();

        // start recording all drawing commands
        batch.begin();

        // render player
        playerRender.render(batch, playerModel);

        // render tree obstacle
        greenTreeRender.render(batch);

        // submit all drawing requests
        batch.end();
    }

    private static void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }
}
