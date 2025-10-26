package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.input.CompositeInputHandler;
import ru.mipt.bit.platformer.input.PlayerMovementInputHandler;
import ru.mipt.bit.platformer.level.FileLevelGenerator;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.model.BaseModel;
import ru.mipt.bit.platformer.model.GreenTreeModel;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.PlayerModel;
import ru.mipt.bit.platformer.render.GameMap;
import ru.mipt.bit.platformer.render.GreenTreeRender;
import ru.mipt.bit.platformer.render.PlayerRender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private GameMap gameMap;
    private PlayerRender playerRender;
    private Movable playerModel;
    private CompositeInputHandler inputHandler;

    // Деревья: храним модели и рендеры в списках
    private final List<BaseModel> treeModels = new ArrayList<>();
    private final List<GreenTreeRender> treeRenders = new ArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();

        gameMap = new GameMap(batch);
        playerRender = new PlayerRender();

        // Выбор генератора: сначала пытаемся загрузить уровень из файла "level.txt" в assets,
        // при ошибке — используем рандомный генератор.
        LevelGenerator generator;
        Level level;
        try {
            generator = new FileLevelGenerator("level.txt");
            level = generator.generate(gameMap.getGroundLayer().getWidth(), gameMap.getGroundLayer().getHeight());
        } catch (Exception e) {
            // fallback to random generator with 12% obstacle density
            Gdx.app.log("LevelGen", "Failed to load level from file, falling back to random. Reason: " + e.getMessage());
            generator = new RandomLevelGenerator(0.12f);
            level = generator.generate(gameMap.getGroundLayer().getWidth(), gameMap.getGroundLayer().getHeight());
        }

        // Создаём игрока на стартовой позиции
        playerModel = new PlayerModel(level.getPlayerStart());

        // Создаём деревья (модели + рендеры)
        Set<GridPoint2> obstacleSet = new HashSet<>(level.getObstacles());
        for (GridPoint2 pos : obstacleSet) {
            BaseModel treeModel = new GreenTreeModel(new GridPoint2(pos));
            treeModels.add(treeModel);
            treeRenders.add(new GreenTreeRender(gameMap, treeModel.getCoordinates()));
        }

        // Инициализация системы ввода
        inputHandler = new CompositeInputHandler();
        inputHandler.addHandler(new PlayerMovementInputHandler(playerModel, obstacleSet));
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
        for (GreenTreeRender r : treeRenders) {
            r.dispose();
        }
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
        gameMap.moveRectangleBetweenTileCenters(playerRender.getPlayerRectangle(), playerModel.getCoordinates(),
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

        // render all tree obstacles
        for (GreenTreeRender r : treeRenders) {
            r.render(batch);
        }

        // submit all drawing requests
        batch.end();
    }

    private static void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }
}
