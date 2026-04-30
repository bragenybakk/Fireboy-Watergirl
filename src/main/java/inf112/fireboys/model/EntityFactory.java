package inf112.fireboys.model;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Decoration;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.enemy.Enemy;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.*;
import inf112.fireboys.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Builds level entities from text tokens. To add a new entity type, add one entry in register().
 *
 * AI (Claude) was used to draft the factory structure and the lambda-based
 * registration pattern. The code has been reviewed and adjusted by the team.
 */
public class EntityFactory {
    private final List<StaticEntity> entities = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private final List<IEnemy> enemies = new ArrayList<>();
    private final List<Decoration> decorations = new ArrayList<>();
    private final List<Pool> pools = new ArrayList<>();
    private final List<Gem> gems = new ArrayList<>();
    private final List<MovingPlatform> movingPlatforms = new ArrayList<>();
    private final List<Door> doors = new ArrayList<>();
    private final List<StaticEntity> movables = new ArrayList<>();
    private final Map<String, Consumer<Scanner>> parsers = new HashMap<>();

    public EntityFactory() {
        register();
    }

    /** Maps each token to the code that builds that entity. */
    private void register() {
        parsers.put("WALL", sc -> entities.add(
                new Wall(readPos(sc), sc.nextDouble(), sc.nextDouble())));
        parsers.put("BUTTON", sc -> entities.add(
                new Button(readPos(sc), sc.nextDouble(), sc.nextDouble(),
                        readPos(sc), sc.nextDouble(), sc.nextDouble())));
        parsers.put("DOOR", sc -> {
            Door d = new Door(readPos(sc), sc.nextDouble(), sc.nextDouble());
            entities.add(d);
            doors.add(d);
        });
        parsers.put("BOX", sc -> {
            Box b = new Box(readPos(sc), sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
            entities.add(b);
            movables.add(b);
        });
        parsers.put("POOL", sc -> {
            ElementState el = ElementState.valueOf(sc.next());
            Pool p = new Pool(readPos(sc), sc.nextDouble(), sc.nextDouble(), el);
            entities.add(p);
            pools.add(p);
        });
        parsers.put("GEM", sc -> {
            ElementState el = ElementState.valueOf(sc.next());
            Gem g = new Gem(readPos(sc), sc.nextDouble(), sc.nextDouble(), el);
            entities.add(g);
            gems.add(g);
        });
        Consumer<Scanner> boost = sc -> entities.add(
                new BoostPlatform(readPos(sc), sc.nextDouble(), sc.nextDouble()));
        parsers.put("BOOST_PLATFORM", boost);
        parsers.put("GRAVITY_POTION", boost);
        parsers.put("JUMP_PAD", boost);
        parsers.put("MOVING_PLATFORM", sc -> {
            Position pos = readPos(sc);
            double w = sc.nextDouble(), h = sc.nextDouble();
            double dx = sc.nextDouble(), dy = sc.nextDouble();
            double speed = sc.nextDouble(), dist = sc.nextDouble();
            MovingPlatform mp = new MovingPlatform(pos, w, h, dx, dy, speed, dist);
            entities.add(mp);
            movingPlatforms.add(mp);
        });
        parsers.put("PLAYER_BOY", sc -> players.add(
                new Player(readPos(sc), ElementState.FIRE)));
        parsers.put("PLAYER_GIRL", sc -> players.add(
                new Player(readPos(sc), ElementState.WATER)));
        parsers.put("ENEMY", sc -> enemies.add(
                new Enemy(readPos(sc), sc.nextDouble(), sc.nextDouble(), players, entities)));
        parsers.put("DECOR_TORCH", sc -> decorations.add(
                new Decoration(readPos(sc), sc.nextDouble(), sc.nextDouble(), 96, 288, 32, 32)));
        parsers.put("DECOR_WINDOW", sc -> decorations.add(
                new Decoration(readPos(sc), sc.nextDouble(), sc.nextDouble(), 96, 256, 32, 32)));
    }

    /** Builds one entity for the given token. Returns false if the token is unknown. */
    public boolean parse(String token, Scanner sc) {
        Consumer<Scanner> p = parsers.get(token);
        if (p == null) return false;
        p.accept(sc);
        return true;
    }

    /** Returns a Board with everything built so far. */
    public Board build(double width, double height) {
        return new Board(width, height, players, entities, enemies, decorations,
                pools, gems, movingPlatforms, doors, movables);
    }

    /** Reads x and y as a Position. */
    private static Position readPos(Scanner sc) {
        return new Position(sc.nextDouble(), sc.nextDouble());
    }
}
