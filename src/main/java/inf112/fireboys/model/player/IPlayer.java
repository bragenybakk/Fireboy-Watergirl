package inf112.fireboys.model.player;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.entity.IMovable;

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

    /**
     * @return true if the player is alive, false otherwise
     */
    boolean isAlive();

    /**
     * Kills the player, setting their alive status to false.
     */
    void kill();

    /**
     * @return the player's current score
     */
    int getScore();

    /**
     * Adds the specified number of points to the player's score.
     * 
     * @param i
     */
    void addScore(int i);
}
