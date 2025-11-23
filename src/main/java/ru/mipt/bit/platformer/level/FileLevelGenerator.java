package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;
import java.util.Set;

/**
 * Генератор уровня из текстового файла.
 * <p>
 * Формат (пример): <p>
 * ___T__T___ <p>
 * __TT__TTTT <p>
 * _________T <p>
 * TTTT__T__T <p>
 * _____X____ <p>
 * __________ <p>
 * <p>
 * Допущения:
 * - первая строка файла считается верхним рядом (y = height-1)
 * - символы: 'T' - дерево, 'X' - старт игрока, '_' (или любой другой) - пусто
 * - если встречается несколько 'X', берётся первый (сверху слева)
 */
public class FileLevelGenerator implements LevelGenerator {

    private final String internalFilePath;

    public FileLevelGenerator(String internalFilePath) {
        this.internalFilePath = internalFilePath;
    }

    @Override
    public Level generate(int width, int height) {
        FileHandle fh = Gdx.files.internal(internalFilePath);
        if (!fh.exists()) {
            throw new IllegalArgumentException("Level file not found: " + internalFilePath);
        }

        String content = fh.readString();
        String[] lines = content.split("\\r?\\n");

        // высота файла — количество строк, ширина — максимальная длина строки
        int fileHeight = lines.length;
        int fileWidth = 0;
        for (String l : lines) {
            fileWidth = Math.max(fileWidth, l.length());
        }

        // Совместим размеры: если размеры в файле отличаются от width/height —
        // мы центрируем/обрезаем/дополняем снизу/слева. Для простоты:
        // требуем совпадения: fileWidth == width && fileHeight == height. Если нет — попытаемся адаптировать.
        if (fileWidth != width || fileHeight != height) {
            // Попытка адаптации: если размеры не совпадают, используем fileWidth/fileHeight как реальные.
            width = fileWidth;
            height = fileHeight;
        }

        Set<GridPoint2> obstacles = new HashSet<>();
        GridPoint2 playerStart = null;

        // Первая строка — верхняя (y = height-1)
        for (int row = 0; row < height; row++) {
            String line = lines[row];
            int y = height - 1 - row;
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c == 'T' || c == 't') {
                    obstacles.add(new GridPoint2(x, y));
                } else if (c == 'X' || c == 'x') {
                    if (playerStart == null) {
                        playerStart = new GridPoint2(x, y);
                    }
                } // '_' и другие символы — пусто
            }
        }

        // Если X не найден — ставим игрока в (0,0)
        if (playerStart == null) {
            playerStart = new GridPoint2(0, 0);
        }

        return new Level(obstacles, playerStart);
    }
}
