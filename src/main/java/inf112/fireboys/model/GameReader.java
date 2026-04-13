package inf112.fireboys.model;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.enemy.Enemy;
import inf112.fireboys.model.entity.Box;
import inf112.fireboys.model.entity.Door;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.entity.BoostPlatform;
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

public class GameReader {
    /**
     * Reads a level file from the classpath and returns a Board.
     * The fileName should be just the filename, e.g. "level1.txt".
     */
    public static Board loadLevel(String fileName) throws IOException {
        List<StaticEntity> entities = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        List<IEnemy> enemies = new ArrayList<>();
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
                case "POOL":
                    String type = sc.next();
                    ElementState elementType = ElementState.valueOf(type);
                    entities.add(new Pool(new Position(sc.nextDouble(), sc.nextDouble()), sc.nextDouble(),
                            sc.nextDouble(), elementType));
                    break;
                case "GEM":
                    String gemType = sc.next();
                    ElementState gemElement = ElementState.valueOf(gemType);
                    entities.add(new Gem(new Position(sc.nextDouble(), sc.nextDouble()), sc.nextDouble(), sc.nextDouble(), gemElement));
                    break;
                case "BOOST_PLATFORM":
                case "GRAVITY_POTION":
                case "JUMP_PAD":
                    entities.add(new BoostPlatform(
                            new Position(sc.nextDouble(), sc.nextDouble()),
                            sc.nextDouble(), sc.nextDouble()));
                    break;
                case "ENEMY":
                    enemies.add(new Enemy(new Position(sc.nextDouble(), sc.nextDouble()), sc.nextDouble(), sc.nextDouble()));
                    break;
                default:
                    if (sc.hasNextLine())
                        sc.nextLine();
                    break;
            }
        }
        sc.close();
        return new Board(boardWidth, boardHeight, players, entities, enemies);
    }
}
