package inf112.skeleton.coordinateSystem;

import java.util.List;

import inf112.skeleton.model.entity.StaticEntity;
import inf112.skeleton.model.player.Player;

public record Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities) {
    public Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.players = players;
        this.entities = entities;
    }
}