package ru.mipt.bit.platformer.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, объединяющий несколько обработчиков ввода.
 * Каждый кадр вызывает их по очереди.
 */
public class CompositeInputHandler implements GameInputHandler {

    private final List<GameInputHandler> handlers = new ArrayList<>();

    public void addHandler(GameInputHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void handleInput() {
        for (GameInputHandler handler : handlers) {
            handler.handleInput();
        }
    }
}