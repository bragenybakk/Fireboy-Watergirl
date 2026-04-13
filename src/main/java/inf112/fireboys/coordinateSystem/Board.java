package inf112.fireboys.coordinateSystem;

import java.util.List;

import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.player.Player;

public record Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities,
        List<IEnemy> enemies, List<Decoration> decorations) {
    public Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities,
            List<IEnemy> enemies) {
        this(boardWidth, boardHeight, players, entities, enemies, List.of());
    }
}
