package inf112.skeleton.coordinateSystem;

import java.util.List;

import inf112.skeleton.model.enemy.Enemy;
import inf112.skeleton.model.entity.StaticEntity;
import inf112.skeleton.model.player.Player;

public record Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities, List<Enemy> enemies) {
    public Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities, List<Enemy> enemies) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.players = players;
        this.entities = entities;
        this.enemies = enemies;
    }
}