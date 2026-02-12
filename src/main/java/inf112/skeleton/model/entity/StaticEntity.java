package inf112.skeleton.model.entity;

import inf112.skeleton.coordinateSystem.Position;

/**
 * Abstract base class for static entities (buttons, doors, levers, pools,
 * etc.).
 * Provides common functionality so subclasses only need to implement specific
 * behavior.
 */
public abstract class StaticEntity implements IStaticEntity {
    protected Position position;
    protected double width;
    protected double height;
    /**
     * Creates a new static entity.
     * 
     * @param type
     *            the position of the entity
     * @param width2
     *            the width of the entity
     * @param height
     *            the height of the entity
     */
    public StaticEntity(Position position, double height, double width) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    @Override
    public Position getPos() {
        return position;
    }

    @Override
    public void setPos(Position pos) {
        this.position = pos;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }
}
