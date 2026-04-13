package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.BoostPlatform;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.entity.Wall;
import inf112.fireboys.model.player.Player;

public class BoostPlatformTest {
    /** Player (4×4) with feet on platform top at y=20. */
    private static final Position PLAYER_ON_TOP = new Position(12, 16);
    private static final Position PLATFORM_POS = new Position(10, 20);

    private BoostPlatform platform;
    private Player fireboy;
    private Player watergirl;

    @BeforeEach
    void setUp() {
        platform = new BoostPlatform(PLATFORM_POS, 8, 2);
        fireboy = new Player(PLAYER_ON_TOP, ElementState.FIRE);
        watergirl = new Player(PLAYER_ON_TOP, ElementState.WATER);
    }

    @Test
    void contactDoesNotGrantBoostWithoutJump() {
        platform.whenContact(fireboy);
        assertFalse(fireboy.hasJumpBoost());
    }

    @Test
    void anyPlayerContactDoesNotAutoGrant() {
        platform.whenContact(watergirl);
        assertFalse(watergirl.hasJumpBoost());
    }

    @Test
    void noBoostFromSideHit() {
        Player side = new Player(new Position(20, 21), ElementState.FIRE);
        platform.whenContact(side);
        assertFalse(side.hasJumpBoost());
    }

    @Test
    void thinPlateOnFloor_detectsStandingWhenFeetAtFloorLevel() {
        // Plate h=1 at y=109 (AABB 109–110); player 4×4 at y=106 → feet at 110, same as floor under plate.
        List<Player> pl = new ArrayList<>();
        Player p = new Player(new Position(12, 106), ElementState.FIRE);
        p.setOnGroundTRUE();
        pl.add(p);
        List<StaticEntity> ents = new ArrayList<>();
        ents.add(new BoostPlatform(new Position(10, 109), 10, 1));
        Board board = new Board(40, 40, pl, ents, new ArrayList<>());
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        assertTrue(model.isPlayerOnBoostPlateWithoutCharge());
    }

    @Test
    void jumpOnPlateChargesWithoutLeavingGround() {
        Player p = new Player(PLAYER_ON_TOP, ElementState.FIRE);
        p.setOnGroundTRUE();
        List<Player> pl = new ArrayList<>();
        pl.add(p);
        List<StaticEntity> ents = new ArrayList<>();
        ents.add(new BoostPlatform(PLATFORM_POS, 8, 2));
        Board board = new Board(40, 40, pl, ents, new ArrayList<>());
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        assertTrue(model.isPlayerOnBoostPlateWithoutCharge());
        model.playerJump();
        assertTrue(p.hasJumpBoost());
        assertEquals(0.0, p.getVelocityY(), 0.0001);
    }

    @Test
    void chargeOnPlateThenJumpOffUsesBoost() {
        List<StaticEntity> ents = new ArrayList<>();
        ents.add(new Wall(new Position(0, 30), 50, 8));
        ents.add(new BoostPlatform(new Position(10, 28), 10, 2));
        Player p = new Player(new Position(12, 24), ElementState.FIRE);
        p.setOnGroundTRUE();
        List<Player> pl = new ArrayList<>();
        pl.add(p);
        Board board = new Board(50, 50, pl, ents, new ArrayList<>());
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);

        model.playerJump();
        assertTrue(p.hasJumpBoost());

        p.setPos(new Position(5, 26));
        p.setOnGroundTRUE();
        double normalImpulse = new Player(new Position(0, 0), ElementState.FIRE).getJumpImpulse();
        model.playerJump();
        assertTrue(p.getVelocityY() < normalImpulse - 0.01);
        assertFalse(p.hasJumpBoost());
    }

    @Test
    void canChargeAgainOnPlateAfterBoostWasUsed() {
        List<StaticEntity> ents = new ArrayList<>();
        ents.add(new Wall(new Position(0, 30), 50, 8));
        ents.add(new BoostPlatform(new Position(10, 28), 10, 2));
        Player p = new Player(new Position(12, 24), ElementState.FIRE);
        p.setOnGroundTRUE();
        List<Player> pl = new ArrayList<>();
        pl.add(p);
        Board board = new Board(50, 50, pl, ents, new ArrayList<>());
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);

        model.playerJump();
        assertTrue(p.hasJumpBoost());
        p.setPos(new Position(5, 26));
        p.setOnGroundTRUE();
        model.playerJump();
        assertFalse(p.hasJumpBoost());

        p.setVelocityY(0);
        p.setPos(new Position(12, 24));
        p.setOnGroundTRUE();
        assertTrue(model.isPlayerOnBoostPlateWithoutCharge());
        model.playerJump();
        assertTrue(p.hasJumpBoost());
        assertEquals(0.0, p.getVelocityY(), 0.0001);
    }

    @Test
    void jumpImpulseStrongerWhenCharged() {
        double normalImpulse = fireboy.getJumpImpulse();
        fireboy.grantJumpBoost();
        double boostedImpulse = fireboy.getJumpImpulse();
        assertTrue(boostedImpulse < normalImpulse, "Boosted impulse should be more negative (stronger)");
    }

    @Test
    void boostUsesConfiguredStrength() {
        double normalImpulse = fireboy.getJumpImpulse();
        fireboy.grantJumpBoost();
        double boostedImpulse = fireboy.getJumpImpulse();
        assertEquals(normalImpulse * 1.5, boostedImpulse, 0.001);
    }

    @Test
    void boostConsumedAfterJump() {
        fireboy.grantJumpBoost();
        assertTrue(fireboy.hasJumpBoost());
        fireboy.consumeJumpBoost();
        assertFalse(fireboy.hasJumpBoost());
        assertEquals(fireboy.getJumpImpulse(), new Player(PLAYER_ON_TOP, ElementState.FIRE).getJumpImpulse());
    }
}
