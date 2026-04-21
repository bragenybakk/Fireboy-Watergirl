package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.Pool;

public class PoolTest {
    private Pool testPool;
    private Position pos;
    @BeforeEach
    void setUp() {
        pos = new Position(0, 0);
        testPool = new Pool(pos, 20, 10, ElementState.FIRE);
    }

    @Test
    void elementStateTest() {
        assertTrue(testPool.getElement() == ElementState.FIRE);
        testPool.setElement(ElementState.WATER);
        assertTrue(testPool.getElement() == ElementState.WATER);
    }

    @Test
    void getDepthTest() {
        double depthMiddlePool = testPool.getDepthAt(10);
        assertTrue(depthMiddlePool == 10);
        double depthInSlope = testPool.getDepthAt(2);
        assertTrue(depthInSlope < 10 && depthInSlope > 0);
    }

    @Test
    void getDepthRightSlopeTest() {
        double depthRightSlope = testPool.getDepthAt(18);
        assertTrue(depthRightSlope < 10 && depthRightSlope > 0);
    }

    @Test
    void getDepthOutsidePoolTest() {
        assertTrue(testPool.getDepthAt(-5) == 0);
        assertTrue(testPool.getDepthAt(25) == 0);
    }

    @Test
    void floorYAtTest() {
        assertTrue(testPool.floorYAt(10) == 10);
        assertTrue(testPool.floorYAt(-5) == 0);
    }

    @Test
    void footIsInWaterTest() {
        assertTrue(testPool.footIsInWater(10, 5));
        assertTrue(!testPool.footIsInWater(25, 5));
        assertTrue(!testPool.footIsInWater(10, -1));
        assertTrue(!testPool.footIsInWater(10, 11));
    }

    @Test
    void footOnFloorTest() {
        assertTrue(testPool.footOnFloor(10, 5, 10));
        assertTrue(!testPool.footOnFloor(25, 5, 10));
        assertTrue(!testPool.footOnFloor(10, 1, 5));
        assertTrue(!testPool.footOnFloor(10, 11, 15));
    }
}
