package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/** A solid wall that blocks movement from all sides. */
public class Wall extends StaticEntity {
    /** Creates a wall at the given position with the given size. */
    public Wall(Position position, double width, double height) {
        super(position, width, height);
    }
}
