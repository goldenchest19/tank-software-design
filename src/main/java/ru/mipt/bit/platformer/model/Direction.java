package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.isEqual;

/**
 * Enum, представляющий направления движения игрока
 * Каждое направление хранит:
 * - смещение по координатам сетки (delta),
 * - угол поворота для отображения игрока (rotation),
 * - список клавиш, которые соответствуют этому направлению (keys)
 */
public enum Direction {
    UP(new GridPoint2(0, 1), 90f, List.of(Input.Keys.UP, Input.Keys.W)),
    DOWN(new GridPoint2(0, -1), -90f, List.of(Input.Keys.DOWN, Input.Keys.S)),
    LEFT(new GridPoint2(-1, 0), 180f, List.of(Input.Keys.LEFT, Input.Keys.A)),
    RIGHT(new GridPoint2(1, 0), 0f, List.of(Input.Keys.RIGHT, Input.Keys.D));

    private final GridPoint2 delta;
    private final float rotation;
    private final List<Integer> keys;

    Direction(GridPoint2 delta, float rotation, List<Integer> keys) {
        this.delta = delta;
        this.rotation = rotation;
        this.keys = keys;
    }

    public GridPoint2 getDelta() {
        return delta;
    }

    public float getRotation() {
        return rotation;
    }

    /**
     * Проверяет, нажата ли какая-либо из клавиш, соответствующих этому направлению.
     * Используется в игровом цикле для управления движением.
     *
     * @return true, если нажата хотя бы одна клавиша направления, иначе false
     */
    public boolean isPressed() {
        return keys.stream().anyMatch(key -> Gdx.input.isKeyPressed(key));
    }

    public static Direction fromRotation(float rotation) {
        for (Direction direction : values()) {
            if (isEqual(direction.rotation, rotation)) {
                return direction;
            }
        }
        return RIGHT;
    }
}