package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class GreenTreeModel {
    /**
     * Координаты клетки, в которой расположено дерево на карте
     */
    private final GridPoint2 treeObstacleCoordinates;


    public GreenTreeModel(GridPoint2 treeObstacleCoordinates) {
        this.treeObstacleCoordinates = treeObstacleCoordinates;
    }

    public GridPoint2 getTreeObstacleCoordinates() {
        return treeObstacleCoordinates;
    }
}
