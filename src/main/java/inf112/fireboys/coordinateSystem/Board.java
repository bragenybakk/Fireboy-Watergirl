package inf112.fireboys.coordinateSystem;

import java.util.List;

import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.Door;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.entity.MovingPlatform;
import inf112.fireboys.model.entity.Pool;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.player.Player;

/**
 * Immutable snapshot of one loaded level — dimensions, entities, players, enemies, and decorations.
 */
public record Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities,
        List<IEnemy> enemies, List<Decoration> decorations, List<Pool> pools, List<Gem> gems,
        List<MovingPlatform> movingPlatforms, List<Door> doors, List<StaticEntity> movables) {

    /**
     * Creates a Board with no decorations, pools, gems, moving platforms, doors, or movables.
     *
     * @param boardWidth
     *            the logical width of the board
     * @param boardHeight
     *            the logical height of the board
     * @param players
     *            the list of players
     * @param entities
     *            the list of static entities (walls, etc.)
     * @param enemies
     *            the list of enemies
     */
    public Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities,
            List<IEnemy> enemies) {
        this(boardWidth, boardHeight, players, entities, enemies,
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
    }

    /**
     * Creates a Board with decorations but no pools, gems, moving platforms, doors, or movables.
     *
     * @param boardWidth
     *            the logical width of the board
     * @param boardHeight
     *            the logical height of the board
     * @param players
     *            the list of players
     * @param entities
     *            the list of static entities
     * @param enemies
     *            the list of enemies
     * @param decorations
     *            the list of decorations
     */
    public Board(double boardWidth, double boardHeight, List<Player> players, List<StaticEntity> entities,
            List<IEnemy> enemies, List<Decoration> decorations) {
        this(boardWidth, boardHeight, players, entities, enemies,
                decorations, List.of(), List.of(), List.of(), List.of(), List.of());
    }
}
