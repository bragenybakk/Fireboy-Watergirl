package inf112.fireboys.model.enemy;

import inf112.fireboys.model.entity.IMovable;

/**
 * Interface for enemy characters.
 * Extends IMovable for position, velocity, and physics properties.
 */
public interface IEnemy extends IMovable {
    /**
     * Updates the enemy's state, including movement and behavior logic.
     */
    void update();

    /**
     * Returns the current FSM state of the enemy.
     *
     * @return the current enemy state
     */
    EnemyState getState();

    /**
     * Returns true if the enemy is alive.
     *
     * @return true if alive, false if dead
     */
    boolean isAlive();

    /**
     * Kills the enemy, setting their alive status to false.
     */
    void kill();

    /**
     * Returns true if the enemy is on the ground.
     *
     * @return true if on the ground, false if airborne
     */
    boolean isOnGround();

    /**
     * Sets the enemy's ground status to true.
     */
    void setOnGroundTRUE();

    /**
     * Sets the enemy's ground status to false.
     */
    void setOnGroundFALSE();
}
