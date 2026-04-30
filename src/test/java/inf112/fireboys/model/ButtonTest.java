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
    void resetPressedClearsFlag() {
        Button button = makeButton();
        Player player = new Player(new Position(2, 2.5), ElementState.WATER);
        button.whenContact(player);
        button.resetPressed();
        assertFalse(button.isPressed(),
                "resetPressed should clear the pressed flag");
    }

    @Test
    void laserWallNullInitially() {
        assertNull(makeButton().getLaserWall(),
                "Button should not have a laser wall reference before one is set");
    }

    @Test
    void laserWallAbsentWhenNothingOnButton() {
        Board board = readButtonLevel();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        model.clockTick();
        boolean hasLaser = false;
        for (StaticEntity e : model.getStaticEntities()) {
            if (e instanceof LaserWall) {
                hasLaser = true;
                break;
            }
        }
        assertFalse(hasLaser,
                "Laser wall should be absent when nothing is on the button");
    }

    @Test
    void laserWallSpawnedWhilePlayerOnButton() {
        Board board = readButtonLevel();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        // First tick — button not pressed yet, no laser
        model.clockTick();
        // Place the player on top of the button and tick — laser should appear
        for (int i = 0; i < 3; i++) {
            model.getPlayers().get(0).setPos(new Position(22, 82));
            model.getPlayers().get(0).setVelocityY(0);
            model.clockTick();
        }
        int laserCount = 0;
        for (StaticEntity e : model.getStaticEntities()) {
            if (e instanceof LaserWall)
                laserCount++;
        }
        assertEquals(1, laserCount,
                "Laser wall should be spawned while a player is on the button");
    }
}
