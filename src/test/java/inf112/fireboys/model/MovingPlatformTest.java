package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.MovingPlatform;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.entity.Wall;
import inf112.fireboys.model.player.Player;

// Tests movement, direction, bouncing, and player-carrying for MovingPlatform.
public class MovingPlatformTest {

    private static final double SPEED = 1.0;
    private static final double DISTANCE = 10.0;
    private static final double P_H = 8.0;

    private MovingPlatform verticalMp;

    @BeforeEach
    void setUp() {
        // vertical escalator moving upward (dirY=-1), starts at y=50
        verticalMp = new MovingPlatform(new Position(0, 50), 10, 3, 0, -1, SPEED, DISTANCE);
    }

    // --- movement direction ---

    @Test
    void movesUpwardAfterOneTick() {
        verticalMp.tick();
        assertEquals(50 - SPEED, verticalMp.getPos().y(), 0.001);
    }

    @Test
    void xStaysFixedForVerticalPlatform() {
        verticalMp.tick();
        assertEquals(0, verticalMp.getPos().x(), 0.001);
    }

    @Test
    void horizontalPlatformMovesAlongX() {
        MovingPlatform mp = new MovingPlatform(new Position(10, 30), 10, 3, 1, 0, SPEED, DISTANCE);
        mp.tick();
        assertEquals(10 + SPEED, mp.getPos().x(), 0.001);
        assertEquals(30, mp.getPos().y(), 0.001);
    }

    // --- delta values ---

    @Test
    void deltaYReflectsMovementAfterTick() {
        verticalMp.tick();
        assertEquals(-SPEED, verticalMp.getDeltaY(), 0.001);
        assertEquals(0, verticalMp.getDeltaX(), 0.001);
    }

    @Test
    void deltaIsZeroBeforeFirstTick() {
        assertEquals(0, verticalMp.getDeltaX(), 0.001);
        assertEquals(0, verticalMp.getDeltaY(), 0.001);
    }

    // --- bounce / reverse ---

    @Test
    void reachesEndPositionAfterFullDistance() {
        for (int i = 0; i < (int) DISTANCE; i++) verticalMp.tick();
        assertEquals(50 - DISTANCE, verticalMp.getPos().y(), 0.001);
    }

    @Test
    void reversesDirectionAfterReachingEnd() {
        for (int i = 0; i <= (int) DISTANCE; i++) verticalMp.tick();
        // one extra tick after reversal: platform should move downward again
        assertEquals(50 - DISTANCE + SPEED, verticalMp.getPos().y(), 0.001);
    }

    @Test
    void returnsToStartAfterFullRoundTrip() {
        for (int i = 0; i < (int) (DISTANCE * 2); i++) verticalMp.tick();
        assertEquals(50, verticalMp.getPos().y(), 0.001);
    }

    // --- direction normalization ---

    @Test
    void normalizesDirectionVectorForDiagonal() {
        // dir (1,1) must be normalized to (1/√2, 1/√2) before applying speed
        MovingPlatform mp = new MovingPlatform(new Position(0, 0), 10, 3, 1, 1, SPEED, DISTANCE);
        mp.tick();
        double expected = SPEED / Math.sqrt(2);
        assertEquals(expected, mp.getDeltaX(), 0.001);
        assertEquals(expected, mp.getDeltaY(), 0.001);
    }

    // --- Wall inheritance ---

    @Test
    void isInstanceOfWall() {
        assertTrue(verticalMp instanceof Wall);
    }

    @Test
    void whenContactSnapsPlayerOnTop() {
        // Platform top at y=20; player (8×8) slightly overlapping from above
        MovingPlatform mp = new MovingPlatform(new Position(0, 20), 10, 3, 0, -1, SPEED, DISTANCE);
        Player player = new Player(new Position(1, 17), ElementState.FIRE);
        player.setVelocityY(2.0);

        mp.whenContact(player);

        assertEquals(0, player.getVelocityY(), 0.001);
        assertEquals(20 - P_H, player.getPos().y(), 0.001);
    }

    // --- player carrying (integration via GameModel) ---

    @Test
    void playerOnTopIsCarriedWithPlatform() {
        // Platform at y=30; player (8×8) with feet exactly at platform top
        MovingPlatform mp = new MovingPlatform(new Position(0, 30), 10, 3, 0, -1, SPEED, DISTANCE);
        Player player = new Player(new Position(1, 30 - P_H), ElementState.FIRE);
        player.setOnGroundTRUE();

        List<Player> pl = new ArrayList<>();
        pl.add(player);
        List<StaticEntity> ents = new ArrayList<>();
        ents.add(mp);
        List<MovingPlatform> mps = List.of(mp);
        Board board = new Board(100, 100, pl, ents, new ArrayList<>(),
                List.of(), List.of(), List.of(), mps, List.of(), List.of());
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);

        double yBefore = player.getPos().y();
        model.clockTick();
        assertTrue(player.getPos().y() < yBefore, "Player should move upward with the platform");
    }

    @Test
    void playerNotOnPlatformIsNotCarriedUpward() {
        // Platform at y=60; player high above, not touching
        MovingPlatform mp = new MovingPlatform(new Position(0, 60), 10, 3, 0, -1, SPEED, DISTANCE);
        Player player = new Player(new Position(1, 5), ElementState.FIRE);
        player.setOnGroundFALSE();

        List<Player> pl = new ArrayList<>();
        pl.add(player);
        List<StaticEntity> ents = new ArrayList<>();
        ents.add(mp);
        ents.add(new Wall(new Position(0, 90), 100, 2)); // floor
        Board board = new Board(100, 100, pl, ents, new ArrayList<>());
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);

        double yBefore = player.getPos().y();
        model.clockTick();
        // gravity pulls player down — must not be carried upward by the platform
        assertTrue(player.getPos().y() >= yBefore, "Player far from platform should not be carried upward");
    }

    @Test
    void platformMovesEachClockTick() {
        List<Player> pl = new ArrayList<>();
        List<StaticEntity> ents = new ArrayList<>();
        ents.add(verticalMp);
        List<MovingPlatform> mps = List.of(verticalMp);
        Board board = new Board(100, 100, pl, ents, new ArrayList<>(),
                List.of(), List.of(), List.of(), mps, List.of(), List.of());
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);

        double yBefore = verticalMp.getPos().y();
        model.clockTick();
        assertTrue(verticalMp.getPos().y() < yBefore, "Platform should have moved upward after one tick");
    }
}
