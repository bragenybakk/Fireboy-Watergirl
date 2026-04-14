package inf112.fireboys.view;

import java.util.List;

import inf112.fireboys.model.GameState;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.player.Player;
import inf112.fireboys.coordinateSystem.Board;

/**
 * Read-only interface for the view to access game model state.
 * Used by GameView to render the game without modifying it.
 */
public interface ViewableGameModel {
    /** Returns the current game state. */
    GameState getGameState();

    /** Returns the index of the currently selected menu option. */
    int getSelectedMenuOption();

    /** Returns the menu options for the current state. */
    String[] getMenuOptions();

    /** Returns the list of players. */
    List<Player> getPlayers();

    /** Returns the list of enemies. */
    List<IEnemy> getEnemies();

    /** Returns the game board. */
    Board getBoard();

    /** Returns the list of available level names. */
    List<String> getLevelNames();

    /** Returns how many levels are currently unlocked in order from level 1. */
    int getUnlockedLevelCount();

    /** Returns whether the ad blocker is currently enabled. */
    boolean isAdsBlocked();

    /** Returns whether music is currently enabled. */
    boolean isMusicEnabled();

    /** Returns whether sound effects are currently enabled. */
    boolean isSoundEnabled();

    int getScore();

    int getTotalGems();

    int getCollectedGems();

    /**
     * True when the first player stands on a boost plate without a charged jump.
     */
    boolean isPlayerOnBoostPlateWithoutCharge();
}
