package inf112.fireboys.model;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Decoration;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.enemy.Enemy;
import inf112.fireboys.model.entity.Box;
import inf112.fireboys.model.entity.Button;
import inf112.fireboys.model.entity.Door;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.entity.BoostPlatform;
import inf112.fireboys.model.entity.MovingPlatform;
import inf112.fireboys.model.entity.Pool;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.entity.Wall;
import inf112.fireboys.model.player.Player;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Parses level files from the classpath and builds a Board with all game entities. */
public class GameReader {
    /**
     * Reads a level file from the classpath and returns a Board.
     * The fileName should be just the filename, e.g. "level1.txt".
     */
    public static Board loadLevel(String fileName) throws IOException {
        List<StaticEntity> entities = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        List<IEnemy> enemies = new ArrayList<>();
        List<Decoration> decorations = new ArrayList<>();
        List<Pool> pools = new ArrayList<>();
        List<Gem> gems = new ArrayList<>();
        double boardWidth = 0;
        double boardHeight = 0;
        InputStream is = GameReader.class.getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new IOException("Level file not found on classpath: " + fileName);
        }
        Scanner sc = new Scanner(is).useLocale(Locale.US);
        while (sc.hasNext()) {
            String entityId = sc.next().toUpperCase();
            switch (entityId) {
                case "BOARD":
                    boardWidth = sc.nextDouble();
                    boardHeight = sc.nextDouble();
                    break;
                case "BUTTON": {
                    double bx = sc.nextDouble(), by = sc.nextDouble();
                    double bw = sc.nextDouble(), bh = sc.nextDouble();
                    double tx = sc.nextDouble(), ty = sc.nextDouble();
                    double tw = sc.nextDouble(), th = sc.nextDouble();
                    entities.add(new Button(new Position(bx, by), bw, bh, new Position(tx, ty), tw, th));
                    break;
                }
                case "DOOR":
                    entities.add(new Door(new Position(sc.nextDouble(), sc.nextDouble()),
                            sc.nextDouble(), sc.nextDouble()));
                    break;
                case "PLAYER_BOY":
                    players.add(new Player(new Position(sc.nextDouble(), sc.nextDouble()), ElementState.FIRE));
                    break;
                case "PLAYER_GIRL":
                    players.add(new Player(new Position(sc.nextDouble(), sc.nextDouble()), ElementState.WATER));
                    break;
                case "WALL":
                    entities.add(
                            new Wall(new Position(sc.nextDouble(), sc.nextDouble()), sc.nextDouble(), sc.nextDouble()));
                    break;
                case "BOX":
                    entities.add(new Box(new Position(sc.nextDouble(), sc.nextDouble()), sc.nextDouble(),
                            sc.nextDouble(), sc.nextDouble()));
                    break;
                case "POOL": {
                    String type = sc.next();
                    ElementState elementType = ElementState.valueOf(type);
                    Pool pool = new Pool(new Position(sc.nextDouble(), sc.nextDouble()), sc.nextDouble(),
                            sc.nextDouble(), elementType);
                    entities.add(pool);
                    pools.add(pool);
                    break;
                }
                case "GEM": {
                    String gemType = sc.next();
                    ElementState gemElement = ElementState.valueOf(gemType);
                    Gem gem = new Gem(new Position(sc.nextDouble(), sc.nextDouble()), sc.nextDouble(),
                            sc.nextDouble(), gemElement);
                    entities.add(gem);
                    gems.add(gem);
                    break;
                }
                case "BOOST_PLATFORM":
                case "GRAVITY_POTION":
                case "JUMP_PAD":
                    entities.add(new BoostPlatform(
                            new Position(sc.nextDouble(), sc.nextDouble()),
                            sc.nextDouble(), sc.nextDouble()));
                    break;
                case "MOVING_PLATFORM": {
                    double mpX = sc.nextDouble(), mpY = sc.nextDouble();
                    double mpW = sc.nextDouble(), mpH = sc.nextDouble();
                    double mpDX = sc.nextDouble(), mpDY = sc.nextDouble();
                    double mpSpeed = sc.nextDouble(), mpDist = sc.nextDouble();
                    entities.add(new MovingPlatform(new Position(mpX, mpY), mpW, mpH, mpDX, mpDY, mpSpeed, mpDist));
                    break;
                }
                case "ENEMY":
                    enemies.add(new Enemy(new Position(sc.nextDouble(), sc.nextDouble()), sc.nextDouble(),
                            sc.nextDouble(), players, entities));
                    break;
                case "DECOR_TORCH":
                    decorations.add(new Decoration(new Position(sc.nextDouble(), sc.nextDouble()),
                            sc.nextDouble(), sc.nextDouble(), 96, 288, 32, 32));
                    break;
                case "DECOR_WINDOW":
                    decorations.add(new Decoration(new Position(sc.nextDouble(), sc.nextDouble()),
                            sc.nextDouble(), sc.nextDouble(), 96, 256, 32, 32));
                    break;
                default:
                    if (sc.hasNextLine())
                        sc.nextLine();
                    break;
            }
        }
        sc.close();
        return new Board(boardWidth, boardHeight, players, entities, enemies, decorations, pools, gems);
    }
}
