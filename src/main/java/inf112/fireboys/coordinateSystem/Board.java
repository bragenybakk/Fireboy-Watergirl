package inf112.fireboys.coordinateSystem;

import java.util.List;

import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.Door;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.entity.MovingPlatform;
import inf112.fireboys.model.entity.Pool;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.player.Player;

public record Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities,
        List<IEnemy> enemies, List<Decoration> decorations, List<Pool> pools, List<Gem> gems,
        List<MovingPlatform> movingPlatforms, List<Door> doors, List<StaticEntity> movables) {

    public Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities,
            List<IEnemy> enemies) {
        this(boardWidth, boardHeight, players, entities, enemies,
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
    }

    public Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities,
            List<IEnemy> enemies, List<Decoration> decorations) {
        this(boardWidth, boardHeight, players, entities, enemies,
                decorations, List.of(), List.of(), List.of(), List.of(), List.of());
    }
}
