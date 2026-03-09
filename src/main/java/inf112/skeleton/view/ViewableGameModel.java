package inf112.skeleton.view;

import java.util.List;

import inf112.skeleton.model.GameState;
import inf112.skeleton.model.enemy.IEnemy;
import inf112.skeleton.model.player.Player;
import inf112.skeleton.coordinateSystem.Board;

public interface ViewableGameModel {
    GameState getGameState();

    int getSelectedMenuOption();

    String[] getMenuOptions();

    List<Player> getPlayers();

    List<IEnemy> getEnemies();

    Board getBoard();

    java.util.List<String> getLevelNames();
}
