package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.IMovable;
import inf112.fireboys.model.entity.Movable;

/**
 * Class that tests Movable class
 */
public class MovableTest {
    private static class TestMovable extends Movable {
        TestMovable(Position position, double width, double height) {
            super(position, width, height);
        }

        @Override
        public void whenContact(IMovable movableEntity) {
        }
    }
    private TestMovable movable;
    @BeforeEach
    void setUp() {
        movable = new TestMovable(new Position(10, 20), 8, 6);
    }

    @Test
    void initialPositionIsSet() {
        assertEquals(10, movable.getPos().x(), 0.001);
        assertEquals(20, movable.getPos().y(), 0.001);
    }

    @Test
    void initialVelocityIsZero() {
        assertEquals(0, movable.getVelocityX(), 0.001);
        assertEquals(0, movable.getVelocityY(), 0.001);
    }

    @Test
    void initialWeightIsOne() {
        assertEquals(1.0, movable.getWeight(), 0.001);
    }

    @Test
    void initialAliveIsTrue() {
        assertTrue(movable.isAlive());
    }

    @Test
    void initialIsOnGroundIsFalse() {
        assertFalse(movable.isOnGround());
    }

    @Test
    void widthAndHeightAreSet() {
        assertEquals(8, movable.getWidth(), 0.001);
        assertEquals(6, movable.getHeight(), 0.001);
    }

    @Test
    void setPosUpdatesPosition() {
        movable.setPos(new Position(50, 60));
        assertEquals(50, movable.getPos().x(), 0.001);
        assertEquals(60, movable.getPos().y(), 0.001);
    }

    @Test
    void setVelocityXUpdatesValue() {
        movable.setVelocityX(3.5);
        assertEquals(3.5, movable.getVelocityX(), 0.001);
    }

    @Test
    void setVelocityYUpdatesValue() {
        movable.setVelocityY(-1.8);
        assertEquals(-1.8, movable.getVelocityY(), 0.001);
    }

    @Test
    void killSetsAliveFalse() {
        movable.kill();
        assertFalse(movable.isAlive());
    }

    @Test
    void setOnGroundTRUESetsOnGround() {
        movable.setOnGroundTRUE();
        assertTrue(movable.isOnGround());
    }

    @Test
    void setOnGroundFALSEClearsOnGround() {
        movable.setOnGroundTRUE();
        movable.setOnGroundFALSE();
        assertFalse(movable.isOnGround());
    }
}
