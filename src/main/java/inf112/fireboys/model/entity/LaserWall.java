package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/** A laser wall obstacle that behaves like a wall but is rendered differently. */
public class LaserWall extends Wall {
    /** Creates a laser wall at the given position with the given size. */
    public LaserWall(Position position, double width, double height) {
        super(position, width, height);
    }
}
