package inf112.fireboys.coordinateSystem;

import java.util.List;

import inf112.fireboys.model.enemy.Enemy;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.player.Player;

public record Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities, List<Enemy> enemies) {
    public Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities, List<Enemy> enemies) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.players = players;
        this.entities = entities;
        this.enemies = enemies;
    }
}