package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.config.GameConfig.LevelMode;
import ru.mipt.bit.platformer.input.CompositeInputHandler;
import ru.mipt.bit.platformer.input.PlayerMovementInputHandler;
import ru.mipt.bit.platformer.input.RandomTankMovementInputHandler;
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
import ru.mipt.bit.platformer.state.OccupiedCells;

import java.util.*;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private GameMap gameMap;
    private PlayerRender playerRender;
    private Movable playerModel;
    private CompositeInputHandler inputHandler;
    private OccupiedCells occupiedCells;

    private final List<BaseModel> treeModels = new ArrayList<>();
    private final List<GreenTreeRender> treeRenders = new ArrayList<>();
    private final List<Movable> aiTanks = new ArrayList<>();
    private final List<PlayerRender> aiRenders = new ArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameMap = new GameMap(batch);
        playerRender = new PlayerRender();

        LevelGenerator generator;
        Level level;

        LevelMode mode = GameConfig.getLevelMode();
        if (mode == LevelMode.FILE) {
            try {
                generator = new FileLevelGenerator(GameConfig.getLevelFilePath());
                level = generator.generate(gameMap.getGroundLayer().getWidth(), gameMap.getGroundLayer().getHeight());
            } catch (Exception e) {
                Gdx.app.log("LevelGen", "Failed to load level from file, fallback to random. " + e.getMessage());
                generator = new RandomLevelGenerator(GameConfig.getRandomObstacleDensity());
                level = generator.generate(gameMap.getGroundLayer().getWidth(), gameMap.getGroundLayer().getHeight());
            }
        } else {
            generator = new RandomLevelGenerator(GameConfig.getRandomObstacleDensity());
            level = generator.generate(gameMap.getGroundLayer().getWidth(), gameMap.getGroundLayer().getHeight());
        }

        playerModel = new PlayerModel(level.getPlayerStart());
        Set<GridPoint2> obstacleSet = new HashSet<>(level.getObstacles());

        occupiedCells = new OccupiedCells(obstacleSet);
        occupiedCells.registerStanding(playerModel);

        for (GridPoint2 pos : obstacleSet) {
            BaseModel treeModel = new GreenTreeModel(new GridPoint2(pos));
            treeModels.add(treeModel);
            treeRenders.add(new GreenTreeRender(gameMap, treeModel.getCoordinates()));
        }

        generateAiTanks(obstacleSet);

        inputHandler = new CompositeInputHandler();

        inputHandler.addHandler(
                new PlayerMovementInputHandler(playerModel, occupiedCells, gameMap)
        );

        for (Movable aiTank : aiTanks) {
            inputHandler.addHandler(new RandomTankMovementInputHandler(aiTank, occupiedCells, gameMap));
        }
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
        playerRender.dispose();
        for (PlayerRender aiRender : aiRenders) {
            aiRender.dispose();
        }
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
        syncTankState(playerModel, playerRender);
        for (int i = 0; i < aiTanks.size(); i++) {
            syncTankState(aiTanks.get(i), aiRenders.get(i));
        }
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

        for (int i = 0; i < aiTanks.size(); i++) {
            aiRenders.get(i).render(batch, aiTanks.get(i));
        }

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

    private void syncTankState(Movable tank, PlayerRender render) {
        gameMap.moveRectangleBetweenTileCenters(render.getPlayerRectangle(), tank.getCoordinates(),
                tank.getPlayerDestinationCoordinates(), tank.getPlayerMovementProgress());

        tank.updateProgress();
        occupiedCells.syncWithMovement(tank);
    }

    private void generateAiTanks(Set<GridPoint2> obstacleSet) {
        aiTanks.clear();
        aiRenders.clear();
        Random random = new Random();
        Set<GridPoint2> occupied = new HashSet<>(obstacleSet);
        occupied.add(playerModel.getCoordinates());

        int maxAttempts = gameMap.getGroundLayer().getWidth() * gameMap.getGroundLayer().getHeight() * 3;
        int attempts = 0;
        while (aiTanks.size() < GameConfig.getAiTankCount() && attempts < maxAttempts) {
            int x = random.nextInt(gameMap.getGroundLayer().getWidth());
            int y = random.nextInt(gameMap.getGroundLayer().getHeight());
            GridPoint2 candidate = new GridPoint2(x, y);
            if (occupied.contains(candidate)) {
                attempts++;
                continue;
            }
            Movable aiTank = new PlayerModel(candidate);
            aiTanks.add(aiTank);
            aiRenders.add(new PlayerRender(true));
            occupied.add(candidate);
            occupiedCells.registerStanding(aiTank);
            attempts++;
        }
    }
}
