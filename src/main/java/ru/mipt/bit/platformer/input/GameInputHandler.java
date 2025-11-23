package ru.mipt.bit.platformer.input;

/**
 * Базовый интерфейс для всех обработчиков ввода.
 * Позволяет расширять систему, добавляя новые типы действий.
 */
public interface GameInputHandler {
    void handleInput();
}
