package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.Wall;
import inf112.fireboys.model.player.Player;

// Tests wall collision for all 4 sides.
// Players and walls are set up directly so we control which side gets hit.
public class WallCollisionTest {

    private static final double P_W = 8.0;
    private static final double P_H = 8.0;

    // --- landing on top of wall ---

    @Test
    void testPlayerLandsOnWallStopsVertically() {
        // player bottom at 19, wall top at 18 -> small overlap from top
        Wall wall = new Wall(new Position(0, 18), 10, 10);
        Player player = new Player(new Position(0, 15), ElementState.FIRE);
        player.setVelocityY(2.0);

        wall.whenContact(player);

        assertEquals(0, player.getVelocityY(), 0.001);
    }

    @Test
    void testPlayerLandsOnWallIsPlacedOnTopSurface() {
        Wall wall = new Wall(new Position(0, 18), 10, 10);
        Player player = new Player(new Position(0, 15), ElementState.FIRE);

        wall.whenContact(player);

        assertEquals(18 - P_H, player.getPos().y(), 0.001);
    }

    @Test
    void testPlayerLandsOnWallIsOnGround() {
        Wall wall = new Wall(new Position(0, 18), 10, 10);
        Player player = new Player(new Position(0, 15), ElementState.FIRE);
        player.setOnGroundFALSE();

        wall.whenContact(player);

        assertTrue(player.isOnGround());
    }

    // --- hitting the left face of a wall ---

    @Test
    void testPlayerHitsWallFromLeftStopsHorizontally() {
        // player right edge at 21, wall left at 20 -> small overlap from left
        Wall wall = new Wall(new Position(20, 0), 10, 20);
        Player player = new Player(new Position(17, 2), ElementState.FIRE);
        player.setVelocityX(2.0);

        wall.whenContact(player);

        assertEquals(0, player.getVelocityX(), 0.001);
    }

    @Test
    void testPlayerHitsWallFromLeftIsRepositioned() {
        Wall wall = new Wall(new Position(20, 0), 10, 20);
        Player player = new Player(new Position(17, 2), ElementState.FIRE);

        wall.whenContact(player);

        assertEquals(20 - P_W, player.getPos().x(), 0.001);
    }

    // --- hitting the right face of a wall ---

    @Test
    void testPlayerHitsWallFromRightStopsHorizontally() {
        // player left at 24, wall right at 26 -> small overlap from right
        Wall wall = new Wall(new Position(20, 0), 6, 20);
        Player player = new Player(new Position(24, 2), ElementState.FIRE);
        player.setVelocityX(-2.0);

        wall.whenContact(player);

        assertEquals(0, player.getVelocityX(), 0.001);
    }

    @Test
    void testPlayerHitsWallFromRightIsRepositioned() {
        Wall wall = new Wall(new Position(20, 0), 6, 20);
        Player player = new Player(new Position(24, 2), ElementState.FIRE);

        wall.whenContact(player);

        assertEquals(20 + 6, player.getPos().x(), 0.001);
    }

    // --- jumping into a ceiling ---

    @Test
    void testPlayerHitsCeilingStopsVertically() {
        // player top at 10, wall bottom at 12 -> small overlap from bottom
        Wall wall = new Wall(new Position(0, 0), 10, 12);
        Player player = new Player(new Position(2, 10), ElementState.FIRE);
        player.setVelocityY(-2.0);

        wall.whenContact(player);

        assertEquals(0, player.getVelocityY(), 0.001);
    }

    @Test
    void testPlayerHitsCeilingIsRepositionedBelow() {
        Wall wall = new Wall(new Position(0, 0), 10, 12);
        Player player = new Player(new Position(2, 10), ElementState.FIRE);

        wall.whenContact(player);

        assertEquals(12, player.getPos().y(), 0.001);
    }

    @Test
    void testPlayerHitsCeilingDoesNotSetOnGround() {
        Wall wall = new Wall(new Position(0, 0), 10, 12);
        Player player = new Player(new Position(2, 10), ElementState.FIRE);
        player.setOnGroundFALSE();

        wall.whenContact(player);

        assertFalse(player.isOnGround());
    }
}