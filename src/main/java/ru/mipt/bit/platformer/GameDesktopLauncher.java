package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.command.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.config.GameConfig.LevelMode;
import ru.mipt.bit.platformer.input.*;
import ru.mipt.bit.platformer.level.*;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.render.*;
import ru.mipt.bit.platformer.state.GameWorld;
import ru.mipt.bit.platformer.state.OccupiedCells;
import ru.mipt.bit.platformer.state.WorldObserver;

import java.util.*;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener, WorldObserver {

    private Batch batch;
    private GameMap gameMap;
    private Movable playerModel;
    private OccupiedCells occupiedCells;
    private LevelBounds levelBounds;
    private GameWorld gameWorld;
    private HealthBarVisibility healthBarVisibility;

    private final List<BaseModel> treeModels = new ArrayList<>();
    private final Map<BaseModel, GreenTreeRender> treeRenders = new HashMap<>();
    private final List<Movable> aiTanks = new ArrayList<>();
    private final Map<Movable, MovableRenderer> tankRenderers = new HashMap<>();
    private final Map<BulletModel, BulletRender> bulletRenderers = new HashMap<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameMap = new GameMap(batch);
        healthBarVisibility = new HealthBarVisibility();
        levelBounds = new LevelBounds(gameMap.getGroundLayer().getWidth(), gameMap.getGroundLayer().getHeight());

        LevelGenerator generator;
        Level level;

        LevelMode mode = GameConfig.getLevelMode();
        if (mode == LevelMode.FILE) {
            try {
                generator = new FileLevelGenerator(GameConfig.getLevelFilePath());
                level = generator.generate(levelBounds.getWidth(), levelBounds.getHeight());
            } catch (Exception e) {
                Gdx.app.log("LevelGen", "Failed to load level from file, fallback to random. " + e.getMessage());
                generator = new RandomLevelGenerator(GameConfig.getRandomObstacleDensity());
                level = generator.generate(levelBounds.getWidth(), levelBounds.getHeight());
            }
        } else {
            generator = new RandomLevelGenerator(GameConfig.getRandomObstacleDensity());
            level = generator.generate(levelBounds.getWidth(), levelBounds.getHeight());
        }

        playerModel = new PlayerModel(level.getPlayerStart());
        Set<GridPoint2> obstacleSet = new HashSet<>(level.getObstacles());

        occupiedCells = new OccupiedCells(obstacleSet);
        occupiedCells.registerStanding(playerModel);

        for (GridPoint2 pos : obstacleSet) {
            BaseModel treeModel = new GreenTreeModel(new GridPoint2(pos));
            treeModels.add(treeModel);
        }

        generateAiTanks(obstacleSet);

        CompositeInputHandler inputHandler = new CompositeInputHandler();
        gameWorld = new GameWorld(playerModel, aiTanks, treeModels, occupiedCells, inputHandler, levelBounds);
        gameWorld.addObserver(this);

        inputHandler.addHandler(
                new PlayerMovementInputHandler(playerModel, occupiedCells, levelBounds)
        );
        inputHandler.addHandler(new PlayerShootInputHandler(playerModel, gameWorld));
        inputHandler.addHandler(new HealthBarToggleInputHandler(new ToggleHealthBarCommand(healthBarVisibility)));

        for (Movable aiTank : aiTanks) {
            inputHandler.addHandler(new RandomTankMovementInputHandler(aiTank, occupiedCells, levelBounds, gameWorld));
        }
    }

    @Override
    public void render() {
        gameWorld.update(Gdx.graphics.getDeltaTime());

        drawGraphicChanges();
    }

    /**
     * Освобождает все ресурсы, загруженные в create()
     */
    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        treeRenders.values().forEach(GreenTreeRender::dispose);
        tankRenderers.values().forEach(MovableRenderer::dispose);
        bulletRenderers.values().forEach(BulletRender::dispose);
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

    private void drawGraphicChanges() {
        // clear the screen
        clearScreen();

        // render each tile of the level
        gameMap.render();

        updateRenderState();

        // start recording all drawing commands
        batch.begin();

        // render player and ai tanks
        renderMovables();

        bulletRenderers.forEach((bullet, renderer) -> renderer.render(batch, bullet));

        // render all tree obstacles
        treeRenders.values().forEach(render -> render.render(batch));

        // submit all drawing requests
        batch.end();
    }

    private static void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void updateRenderState() {
        tankRenderers.forEach((tank, render) -> syncMovement(render.getPlayerRectangle(), tank));
        bulletRenderers.forEach((bullet, render) -> syncMovement(render.getRectangle(), bullet));
    }

    private void syncMovement(com.badlogic.gdx.math.Rectangle rectangle, MovingEntity entity) {
        gameMap.moveRectangleBetweenTileCenters(rectangle, entity.getCoordinates(),
                entity.getDestinationCoordinates(), entity.getMovementProgress());
    }

    private void renderMovables() {
        MovableRenderer playerRenderer = tankRenderers.get(playerModel);
        if (playerRenderer != null) {
            playerRenderer.render(batch, playerModel);
        }

        for (Movable aiTank : aiTanks) {
            MovableRenderer aiRenderer = tankRenderers.get(aiTank);
            if (aiRenderer != null) {
                aiRenderer.render(batch, aiTank);
            }
        }
    }

    private void generateAiTanks(Set<GridPoint2> obstacleSet) {
        aiTanks.clear();
        Random random = new Random();
        Set<GridPoint2> occupied = new HashSet<>(obstacleSet);
        occupied.add(playerModel.getCoordinates());

        int maxAttempts = levelBounds.getWidth() * levelBounds.getHeight() * 3;
        int attempts = 0;
        while (aiTanks.size() < GameConfig.getAiTankCount() && attempts < maxAttempts) {
            int x = random.nextInt(levelBounds.getWidth());
            int y = random.nextInt(levelBounds.getHeight());
            GridPoint2 candidate = new GridPoint2(x, y);
            if (occupied.contains(candidate)) {
                attempts++;
                continue;
            }
            Movable aiTank = new AiTankModel(candidate);
            aiTanks.add(aiTank);
            occupied.add(candidate);
            occupiedCells.registerStanding(aiTank);
            attempts++;
        }
    }

    private MovableRenderer decorateWithHealthBar(MovableRenderer renderer) {
        return new HealthBarRenderDecorator(renderer, healthBarVisibility);
    }

    @Override
    public void onObjectAdded(BaseModel model) {
        if (model instanceof Movable) {
            boolean useRedTexture = model instanceof AiTankModel;
            tankRenderers.put((Movable) model, decorateWithHealthBar(new PlayerRender(useRedTexture)));
            if (model != playerModel && !aiTanks.contains(model)) {
                aiTanks.add((Movable) model);
            }
        } else if (model instanceof BulletModel) {
            bulletRenderers.put((BulletModel) model, new BulletRender());
        } else if (model instanceof GreenTreeModel) {
            treeRenders.put(model, new GreenTreeRender(gameMap, model.getCoordinates()));
        }
    }

    @Override
    public void onObjectRemoved(BaseModel model) {
        if (model instanceof Movable) {
            MovableRenderer renderer = tankRenderers.remove(model);
            if (renderer != null) {
                renderer.dispose();
            }
            if (model != playerModel) {
                aiTanks.remove(model);
            }
        } else if (model instanceof BulletModel) {
            BulletRender render = bulletRenderers.remove(model);
            if (render != null) {
                render.dispose();
            }
        } else if (model instanceof GreenTreeModel) {
            GreenTreeRender render = treeRenders.remove(model);
            if (render != null) {
                render.dispose();
            }
        }
    }
}
