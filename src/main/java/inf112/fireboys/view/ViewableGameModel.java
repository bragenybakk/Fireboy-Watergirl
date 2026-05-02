package inf112.fireboys.view;

import java.util.List;
import java.util.Map;

import inf112.fireboys.model.GameState;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.player.Player;
import inf112.fireboys.coordinateSystem.Board;

/**
 * Read-only interface for the view to access game model state.
 * Used by GameView to render the game without modifying it.
 */
public interface ViewableGameModel {
    /**
     * Returns the current game state.
     *
     * @return the current game state
     */
    GameState getGameState();

    /**
     * Returns the index of the currently selected menu option.
     *
     * @return the selected menu option index
     */
    int getSelectedMenuOption();

    /**
     * Returns the menu options for the current state.
     *
     * @return array of menu option strings
     */
    String[] getMenuOptions();

    /**
     * Returns the list of players.
     *
     * @return the list of players
     */
    List<Player> getPlayers();

    /**
     * Returns the list of enemies.
     *
     * @return the list of enemies
     */
    List<IEnemy> getEnemies();

    /**
     * Returns the game board.
     *
     * @return the current board
     */
    Board getBoard();

    /**
     * Returns the list of available level names.
     *
     * @return the list of level names
     */
    List<String> getLevelNames();

    /**
     * Returns how many levels are currently unlocked in order from level 1.
     *
     * @return the number of unlocked levels
     */
    int getUnlockedLevelCount();

    /**
     * Returns whether the ad blocker is currently enabled.
     *
     * @return true if the ad blocker is enabled
     */
    boolean isAdsBlocked();

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

    /**
     * Returns the total number of gems in the current level.
     *
     * @return the total gem count
     */
    int getTotalGems();

    /**
     * Returns the number of gems collected so far in the current level.
     *
     * @return the collected gem count
     */
    int getCollectedGems();

    /**
     * Returns ticks elapsed since the current level started.
     *
     * @return the elapsed tick count
     */
    int getElapsedTicks();

    /**
     * Returns a map from level name (without .txt) to best tick count.
     *
     * @return a map of level name to best completion time in ticks
     */
    Map<String, Integer> getLevelBestTicks();

    /**
     * True when the first player stands on a boost plate without a charged jump.
     *
     * @return true if a player is on a boost plate without a charged boost
     */
    boolean isPlayerOnBoostPlateWithoutCharge();

    /**
     * Returns 0.0 (no fade) to 1.0 (full fade) for the win-screen fade overlay.
     *
     * @return the win fade progress as a value between 0.0 and 1.0
     */
    double getWinFadeProgress();
}
