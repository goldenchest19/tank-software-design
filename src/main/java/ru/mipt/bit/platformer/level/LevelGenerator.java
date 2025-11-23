package ru.mipt.bit.platformer.level;

/**
 * Strategy: генерация уровня
 */
public interface LevelGenerator {
    /**
     * Генерирует уровень заданного размера (ширина, высота)
     * Координатная система: (0,0) — нижний левый угол.
     *
     * @param width  ширина в клетках
     * @param height высота в клетках
     * @return объект Level
     */
    Level generate(int width, int height);
}
