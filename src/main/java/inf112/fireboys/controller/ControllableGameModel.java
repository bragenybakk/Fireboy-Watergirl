package inf112.fireboys.controller;

import inf112.fireboys.model.GameState;

/**
 * Interface for the controller to interact with the game model.
 * Provides methods for player input, menu navigation, and game state changes.
 */
public interface ControllableGameModel {
    /** Sets the current game state. */
    void setGameState(GameState state);

    /** Returns the current game state. */
    GameState getGameState();

    /** Moves the menu selection up. */
    void menuUp();

    /** Moves the menu selection down. */
    void menuDown();

    /** Confirms the current menu selection. */
    void menuSelect();

    /** Advances the game by one tick (physics, collisions, etc). */
    void clockTick();

    /** Moves the player to the right. */
    void movePlayerRight();

    /** Moves the player to the left. */
    void movePlayerLeft();

    /** Stops the player's horizontal movement. */
    void stopPlayer();

    /** Makes the player jump if on the ground. */
    void playerJump();

    /** Toggles the ad blocker on or off. */
    void toggleAdsBlocked();

    /** Moves player 2 (Fireboy) to the right. */
    void movePlayer2Right();

    /** Moves player 2 (Fireboy) to the left. */
    void movePlayer2Left();

    /** Stops player 2's (Fireboy) horizontal movement. */
    void stopPlayer2();

    /** Makes player 2 (Fireboy) jump if on the ground. */
    void player2Jump();
}
