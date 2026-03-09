package inf112.skeleton.model;

import inf112.skeleton.coordinateSystem.Board;
import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.enemy.Enemy;
import inf112.skeleton.model.entity.Box;
import inf112.skeleton.model.entity.Pool;
import inf112.skeleton.model.entity.StaticEntity;
import inf112.skeleton.model.entity.Wall;
import inf112.skeleton.model.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameReader {
    /**
     * Reads a level file and returns a Board.
     * 
     * @throws FileNotFoundException
     */
    public static Board loadLevel(String filePath) throws FileNotFoundException {
        List<StaticEntity> entities = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        double boardWidth = 0;
        double boardHeight = 0;
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String entityId = sc.next().toUpperCase();
            switch (entityId) {
                case "BOARD":
                    boardWidth = sc.nextDouble();
                    boardHeight = sc.nextDouble();
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
                case "ENEMY":
                    enemies.add(new Enemy(new Position(sc.nextDouble(), sc.nextDouble())));
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