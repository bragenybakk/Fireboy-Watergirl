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
     *
     * @return the element state of this player
     */
    ElementState getElementState();

    /**
     * Returns the spawn position of this player.
     *
     * @return the starting position
     */
    Position getStartPos();

    /**
     * Returns true if the player is on the ground.
     *
     * @return true if on the ground, false if airborne
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
     * Returns true if the player is alive.
     *
     * @return true if alive, false if dead
     */
    boolean isAlive();

    /**
     * Kills the player, setting their alive status to false.
     */
    void kill();

    /**
     * Charges a jump boost so the next jump will be stronger.
     */
    void grantJumpBoost();

    /**
     * Returns true if this player has a jump boost ready.
     *
     * @return true if a jump boost is charged
     */
    boolean hasJumpBoost();

    /**
     * Returns the jump impulse — stronger if a boost is charged.
     *
     * @return the jump impulse value to apply
     */
    double getJumpImpulse();

    /**
     * Consumes the jump boost after it has been used.
     */
    void consumeJumpBoost();
}
