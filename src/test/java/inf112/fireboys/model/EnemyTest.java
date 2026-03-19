package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.enemy.Enemy;
import inf112.fireboys.model.player.Player;

public class EnemyTest {
    private Enemy enemy;
    @BeforeEach
    void setUp() {
        enemy = new Enemy(new Position(0, 0), 1.0, 1.0);
    }

    /** Checks initial position, size, weight and alive state. */
    @Test
    void creation() {
        assertEquals(0, enemy.getPos().x());
        assertEquals(0, enemy.getPos().y());
        assertEquals(1.0, enemy.getWidth());
        assertEquals(1.0, enemy.getHeight());
        assertEquals(1.0, enemy.getWeight());
        assertTrue(enemy.isAlive());
    }

    /** Checks that kill() sets alive to false. */
    @Test
    void kill() {
        enemy.kill();
        assertFalse(enemy.isAlive());
    }

    /** Checks that isOnGround follows setOnGroundTRUE/FALSE. */
    @Test
    void onGround() {
        assertFalse(enemy.isOnGround());
        enemy.setOnGroundTRUE();
        assertTrue(enemy.isOnGround());
        enemy.setOnGroundFALSE();
        assertFalse(enemy.isOnGround());
    }

    /** Checks that update() resets isOnGround and moves the enemy right. */
    @Test
    void updateMovesRight() {
        enemy.setOnGroundTRUE();
        enemy.update();
        assertFalse(enemy.isOnGround());
        assertTrue(enemy.getVelocityX() > 0);
    }

    /**
     * Checks that the enemy jumps (negative velocityY) when blocked on the ground.
     */
    @Test
    void jumpsWhenBlocked() {
        enemy.setOnGroundTRUE();
        enemy.setVelocityX(0);
        enemy.update();
        assertTrue(enemy.getVelocityY() < 0);
    }

    /**
     * Checks that the enemy flips direction when blocked while jump cooldown is
     * active.
     */
    @Test
    void flipsDirectionOnCooldown() {
        enemy.setOnGroundTRUE();
        enemy.setVelocityX(0);
        enemy.update();
        boolean wasMovingRight = enemy.getVelocityX() > 0;
        enemy.setOnGroundTRUE();
        enemy.setVelocityX(0);
        enemy.update(); // flips direction
        boolean nowMovingRight = enemy.getVelocityX() > 0;
        assertNotEquals(wasMovingRight, nowMovingRight);
    }

    /** Checks that contact with a player kills the player. */
    @Test
    void contactKillsPlayer() {
        Player player = new Player(new Position(0, 0), ElementState.FIRE);
        enemy.whenContact(player);
        assertFalse(player.isAlive());
    }
}
