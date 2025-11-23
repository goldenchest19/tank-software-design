package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mipt.bit.platformer.command.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.input.*;
import ru.mipt.bit.platformer.level.LevelBounds;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.render.HealthBarVisibility;
import ru.mipt.bit.platformer.state.OccupiedCells;
import ru.mipt.bit.platformer.state.ProjectileSpawner;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class InputHandlersConfiguration {

    @Bean
    public PlayerMovementInputHandler playerMovementInputHandler(
            Movable playerModel,
            OccupiedCells occupiedCells,
            LevelBounds levelBounds
    ) {
        return new PlayerMovementInputHandler(playerModel, occupiedCells, levelBounds);
    }

    @Bean
    public PlayerShootInputHandler playerShootInputHandler(
            Movable playerModel,
            ProjectileSpawner projectileSpawner
    ) {
        return new PlayerShootInputHandler(playerModel, projectileSpawner);
    }

    @Bean
    public HealthBarToggleInputHandler healthBarToggleInputHandler(HealthBarVisibility visibility) {
        return new HealthBarToggleInputHandler(new ToggleHealthBarCommand(visibility));
    }

    @Bean
    public List<RandomTankMovementInputHandler> aiMovementHandlers(
            @Qualifier("aiTanks") List<Movable> aiTanks,
            OccupiedCells occupiedCells,
            LevelBounds levelBounds,
            ProjectileSpawner projectileSpawner
    ) {
        return aiTanks.stream()
                .map(tank -> new RandomTankMovementInputHandler(tank, occupiedCells, levelBounds, projectileSpawner))
                .collect(Collectors.toList());
    }

    @Bean
    public InputHandlerSetup inputHandlerSetup(
            CompositeInputHandler compositeInputHandler,
            PlayerMovementInputHandler playerMovementInputHandler,
            PlayerShootInputHandler playerShootInputHandler,
            HealthBarToggleInputHandler healthBarToggleInputHandler,
            List<RandomTankMovementInputHandler> aiMovementHandlers
    ) {
        return new InputHandlerSetup(
                compositeInputHandler,
                playerMovementInputHandler,
                playerShootInputHandler,
                healthBarToggleInputHandler,
                aiMovementHandlers
        );
    }

    public static class InputHandlerSetup implements InitializingBean {

        private final CompositeInputHandler compositeInputHandler;
        private final PlayerMovementInputHandler playerMovementInputHandler;
        private final PlayerShootInputHandler playerShootInputHandler;
        private final HealthBarToggleInputHandler healthBarToggleInputHandler;
        private final List<RandomTankMovementInputHandler> aiMovementHandlers;

        public InputHandlerSetup(
                CompositeInputHandler compositeInputHandler,
                PlayerMovementInputHandler playerMovementInputHandler,
                PlayerShootInputHandler playerShootInputHandler,
                HealthBarToggleInputHandler healthBarToggleInputHandler,
                List<RandomTankMovementInputHandler> aiMovementHandlers
        ) {
            this.compositeInputHandler = compositeInputHandler;
            this.playerMovementInputHandler = playerMovementInputHandler;
            this.playerShootInputHandler = playerShootInputHandler;
            this.healthBarToggleInputHandler = healthBarToggleInputHandler;
            this.aiMovementHandlers = aiMovementHandlers;
        }

        @Override
        public void afterPropertiesSet() {
            compositeInputHandler.addHandler(playerMovementInputHandler);
            compositeInputHandler.addHandler(playerShootInputHandler);
            compositeInputHandler.addHandler(healthBarToggleInputHandler);
            aiMovementHandlers.forEach(compositeInputHandler::addHandler);
        }
    }
}
