package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.IMovable;

/**
 * Interface for static (non-moving) game objects.
 * These objects have a position and dimensions but no velocity.
 */
public interface IStaticEntity {
    /**
     * Gets the current position of the object.
     * 
     * @return a Position object containing the x and y coordinates.
     */
    Position getPos();

    /**
     * Sets the position of the object.
     * 
     * @param pos
     *            the new position
     */
    void setPos(Position pos);

    /**
     * Gets the width of the object.
     * 
     * @return the width as a double.
     */
    double getWidth();

    /**
     * Gets the height of the object.
     * 
     * @return the height as a double.
     */
    double getHeight();

    /**
     * Decides what happens to the player and the entity when they come in contact.
     */
    void whenContact(IMovable movableEntity);
}
