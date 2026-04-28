package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.Button;
import inf112.fireboys.model.entity.LaserWall;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.player.Player;

public class ButtonTest {
    private Button makeButton() {
        return new Button(new Position(0, 10), 10, 2, new Position(50, 0), 2, 40);
    }

    private Board readButtonLevel() {
        try {
            return GameReader.loadLevel("button.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    void buttonNotPressedInitially() {
        assertFalse(makeButton().isPressed(),
                "Button should not be pressed from start");
    }

    @Test
    void buttonPressedWhenPlayerLandsOnTop() {
        Button button = makeButton();
        Player player = new Player(new Position(2, 2.5), ElementState.WATER);
        button.whenContact(player);
        assertTrue(button.isPressed(),
                "Button should be pressed when player lands on top");
    }

    @Test
    void buttonNotPressedFromSide() {
        Button button = makeButton();
        Player player = new Player(new Position(-7, 10), ElementState.WATER);
        button.whenContact(player);
        assertFalse(button.isPressed(),
                "Button should not be pressed by a side collision");
    }

    @Test
    void trapNotSpawnedInitially() {
        assertFalse(makeButton().isTrapSpawned(),
                "Trap should not be spawned from start");
    }

    @Test
    void markTrapSpawnedWorks() {
        Button button = makeButton();
        button.markTrapSpawned();
        assertTrue(button.isTrapSpawned(),
                "Trap should be marked as spawned after markTrapSpawned()");
    }

    @Test
    void laserWallNotPresentBeforeButtonPressed() {
        Board board = readButtonLevel();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        for (StaticEntity e : model.getStaticEntities()) {
            assertFalse(e instanceof LaserWall,
                    "LaserWall should not exist before button is pressed");
        }
    }

    @Test
    void laserWallSpawnedAfterButtonPressed() {
        Board board = readButtonLevel();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        model.getPlayers().get(0).setPos(new Position(22, 82));
        model.clockTick();
        boolean hasLaser = false;
        for (StaticEntity e : model.getStaticEntities()) {
            if (e instanceof LaserWall) {
                hasLaser = true;
                break;
            }
        }
        assertTrue(hasLaser,
                "LaserWall should be spawned after button is pressed");
    }

    @Test
    void laserWallOnlySpawnedOnce() {
        Board board = readButtonLevel();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        model.getPlayers().get(0).setPos(new Position(22, 82));
        for (int i = 0; i < 5; i++)
            model.clockTick();
        int laserCount = 0;
        for (StaticEntity e : model.getStaticEntities()) {
            if (e instanceof LaserWall)
                laserCount++;
        }
        assertEquals(1, laserCount,
                "Only one LaserWall should be spawned even after multiple clockticks");
    }
}
