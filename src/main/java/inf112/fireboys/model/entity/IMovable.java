package inf112.fireboys.model.entity;

/**
 * Interface for movable game objects.
 * Extends IStaticEntity with velocity and weight properties.
 * Use this for players, boxes, or any object that can move.
 */
public interface IMovable extends IStaticEntity {
    /**
     * Gets the horizontal velocity.
     * 
     * @return the velocity in x-direction
     */
    double getVelocityX();

    /**
     * Sets the horizontal velocity.
     * 
     * @param vx
     *            the new velocity in x-direction
     */
    void setVelocityX(double vx);

    /**
     * Gets the vertical velocity.
     * 
     * @return the velocity in y-direction
     */
    double getVelocityY();

    /**
     * Sets the vertical velocity.
     * 
     * @param vy
     *            the new velocity in y-direction
     */
    void setVelocityY(double vy);

    /**
     * Gets the weight of the object (affects physics).
     * 
     * @return the weight as a double
     */
    double getWeight();

    /**
     * Called when this object lands on top of a solid surface.
     */
    default void setOnGroundTRUE() {
    }
}
