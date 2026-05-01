package inf112.fireboys.controller;

import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.GameState;

/**
 * Interface for the controller to interact with the game model.
 * Provides methods for player input, menu navigation, and game state changes.
 */
public interface ControllableGameModel {
    /**
     * Sets the current game state.
     *
     * @param state
     *            the new game state
     */
    void setGameState(GameState state);

    /**
     * Returns the current game state.
     *
     * @return the current game state
     */
    GameState getGameState();

    /**
     * Moves the menu selection up.
     */
    void menuUp();

    /**
     * Moves the menu selection down.
     */
    void menuDown();

    /**
     * Confirms the current menu selection.
     */
    void menuSelect();

    /**
     * Advances the game by one tick (physics, collisions, etc).
     */
    void clockTick();

    /** Moves the given player left. */
    void moveLeft(ElementState element);

    /** Moves the given player right. */
    void moveRight(ElementState element);

    /** Stops the given player's horizontal movement. */
    void stop(ElementState element);

    /** Makes the given player jump if on the ground. */
    void jump(ElementState element);

    /**
     * Toggles the ad blocker on or off.
     */
    void toggleAdsBlocked();

    /**
     * Toggles music on or off.
     */
    void toggleMusicEnabled();

    /**
     * Toggles sound effects on or off.
     */
    void toggleSoundEnabled();

    /**
     * Returns whether music is currently enabled.
     *
     * @return true if music is enabled
     */
    boolean isMusicEnabled();

    /**
     * Returns whether sound effects are currently enabled.
     *
     * @return true if sound effects are enabled
     */
    boolean isSoundEnabled();

}
