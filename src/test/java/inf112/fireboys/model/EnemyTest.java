package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.enemy.Enemy;
import inf112.fireboys.model.enemy.EnemyState;
import inf112.fireboys.model.entity.Wall;
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

    /** Checks that the enemy jumps (negative velocityY) when blocked on the ground. */
    @Test
    void jumpsWhenBlocked() {
        enemy.setOnGroundTRUE();
        enemy.setVelocityX(0);
        enemy.update();
        assertTrue(enemy.getVelocityY() < 0);
    }

    /** Checks that the enemy flips direction when blocked while jump cooldown is active. */
    @Test
    void flipsDirectionOnCooldown() {
        enemy.setOnGroundTRUE();
        enemy.setVelocityX(0);
        enemy.update();
        boolean wasMovingRight = enemy.getVelocityX() > 0;
        enemy.setOnGroundTRUE();
        enemy.setVelocityX(0);
        enemy.update();
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

    @Test
    void initialStateIsPatrol() {
        assertEquals(EnemyState.PATROL, enemy.getState());
    }

    @Test
    void startsInPatrolWithPlayers() {
        Player player = new Player(new Position(100, 100), ElementState.FIRE);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player));
        assertEquals(EnemyState.PATROL, e.getState());
    }

    @Test
    void entersAlertWhenPlayerInRange() {
        Player player = new Player(new Position(5, 0), ElementState.FIRE);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player));
        e.update();
        assertEquals(EnemyState.ALERT, e.getState());
    }

    @Test
    void entersChaseAfterAlertDuration() {
        Player player = new Player(new Position(5, 0), ElementState.FIRE);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player));
        for (int i = 0; i < 31; i++) e.update();
        assertEquals(EnemyState.CHASE, e.getState());
    }

    @Test
    void alertCancelsIfPlayerLeaves() {
        Player player = new Player(new Position(5, 0), ElementState.FIRE);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player));
        e.update();
        player.setPos(new Position(100, 0));
        e.update();
        assertEquals(EnemyState.PATROL, e.getState());
    }

    @Test
    void returnsToPatrolWhenPlayerLeaves() {
        Player player = new Player(new Position(5, 0), ElementState.FIRE);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player));
        for (int i = 0; i < 31; i++) e.update();
        player.setPos(new Position(100, 0));
        e.update();
        assertEquals(EnemyState.PATROL, e.getState());
    }

    @Test
    void alertPausesMovement() {
        Player player = new Player(new Position(5, 0), ElementState.FIRE);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player));
        e.update();
        assertEquals(0.0, e.getVelocityX());
    }

    @Test
    void chaseMovesTowardPlayerRight() {
        Player player = new Player(new Position(5, 0), ElementState.FIRE);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player));
        for (int i = 0; i < 31; i++) e.update();
        assertTrue(e.getVelocityX() > 0);
    }

    @Test
    void chaseMovesTowardPlayerLeft() {
        Player player = new Player(new Position(-10, 0), ElementState.FIRE);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player));
        for (int i = 0; i < 31; i++) e.update();
        assertTrue(e.getVelocityX() < 0);
    }

    @Test
    void chaseJumpsWhenBlocked() {
        Player player = new Player(new Position(5, 0), ElementState.FIRE);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player));
        for (int i = 0; i < 31; i++) e.update();
        e.setOnGroundTRUE();
        e.setVelocityX(0);
        e.update();
        assertTrue(e.getVelocityY() < 0);
    }

    @Test
    void patrolReversesAtEdge() {
        Wall farWall = new Wall(new Position(50, 1), 10.0, 1.0);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(), List.of(farWall));
        e.setOnGroundTRUE();
        e.update();
        assertTrue(e.getVelocityX() < 0);
    }

    @Test
    void patrolContinuesWithGroundAhead() {
        // enemy at (0,0) h=1 → feet at y=1, leadingX=1.3, wall covers x=[1,3) at y=1
        Wall groundAhead = new Wall(new Position(1.0, 1.0), 2.0, 1.0);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(), List.of(groundAhead));
        e.setOnGroundTRUE();
        e.update();
        assertTrue(e.getVelocityX() > 0);
    }

    @Test
    void chaseJumpsAtEdge() {
        Player player = new Player(new Position(5, 0), ElementState.FIRE);
        Wall farWall = new Wall(new Position(50, 1), 10.0, 1.0);
        Enemy e = new Enemy(new Position(0, 0), 1.0, 1.0, List.of(player), List.of(farWall));
        for (int i = 0; i < 31; i++) e.update();
        e.setOnGroundTRUE();
        e.setVelocityX(0.5); // not blocked — only edge check triggers
        e.update();
        assertTrue(e.getVelocityY() < 0);
    }
}
