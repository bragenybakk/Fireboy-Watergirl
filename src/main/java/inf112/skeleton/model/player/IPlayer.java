package inf112.skeleton.model.player;

import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.ElementState;
import inf112.skeleton.model.entity.IMovable;

/**
 * Interface for player characters (Fireboy and Watergirl).
 * Extends IMovable for position, velocity, and physics properties.
 */
public interface IPlayer extends IMovable {
    /**
     * Returns the current element state of the player (FIRE or WATER).
     */
    ElementState getElementState();

    Position getStartPos();

    /**
     * 
     * @return true if the player is on the ground, false otherwise
     */
    boolean isOnGround();

    /**
     * Sets the player's onGround status to true.
     */
    void setOnGroundTRUE();

    /**
     * Sets the player's onGround status to false.
     */
    void setOnGroundFALSE();
}
