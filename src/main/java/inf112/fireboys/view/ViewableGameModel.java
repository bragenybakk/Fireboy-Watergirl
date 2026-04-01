package inf112.fireboys.view;

import java.util.List;

import inf112.fireboys.model.GameState;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.player.Player;
import inf112.fireboys.coordinateSystem.Board;

public interface ViewableGameModel {
    GameState getGameState();

    int getSelectedMenuOption();

    String[] getMenuOptions();

    List<Player> getPlayers();

    List<IEnemy> getEnemies();

    Board getBoard();

    java.util.List<String> getLevelNames();

    int getScore();
}
