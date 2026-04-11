package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.model.entity.Box;
import inf112.fireboys.model.entity.StaticEntity;

/**
 * Tests that collision resolution order does not cause boxes to warp
 * through walls or floors. The bug occurred when box-box collision
 * resolution undid a prior wall/floor resolution in the same frame.
 */
public class CollisionOrderTest {

    private static final double FLOOR_TOP = 96.0;
    private static final double LEFT_WALL_RIGHT = 2.0;

    private GameModel model;
    private Box boxA;
    private Box boxB;

    @BeforeEach
    void setUp() throws Exception {
        Board board = GameReader.loadLevel("twoboxes_walled.txt");
        model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        int boxCount = 0;
        for (StaticEntity e : model.getStaticEntities()) {
            if (e instanceof Box b) {
                if (boxCount == 0)
                    boxA = b;
                else
                    boxB = b;
                boxCount++;
            }
        }
    }

    @Test
    void testBoxDoesNotWarpBelowFloorWhenPushedLeft() {
        // Push both boxes left repeatedly into the wall
        for (int i = 0; i < 60; i++) {
            boxB.setVelocityX(-2.0);
            model.clockTick();
        }
        double boxABottom = boxA.getPos().y() + boxA.getHeight();
        double boxBBottom = boxB.getPos().y() + boxB.getHeight();
        assertTrue(boxABottom <= FLOOR_TOP + 0.5,
                "BoxA should not warp below floor, bottom=" + boxABottom);
        assertTrue(boxBBottom <= FLOOR_TOP + 0.5,
                "BoxB should not warp below floor, bottom=" + boxBBottom);
    }

    @Test
    void testBoxDoesNotWarpThroughWallWhenPushedLeft() {
        // Push boxes left into the wall for many ticks
        for (int i = 0; i < 60; i++) {
            boxB.setVelocityX(-2.0);
            model.clockTick();
        }
        assertTrue(boxA.getPos().x() >= LEFT_WALL_RIGHT - 0.5,
                "BoxA should not warp through the left wall, x=" + boxA.getPos().x());
        assertTrue(boxB.getPos().x() >= LEFT_WALL_RIGHT - 0.5,
                "BoxB should not warp through the left wall, x=" + boxB.getPos().x());
    }

    @Test
    void testBoxStaysOnFloorAfterBoxBoxCollision() {
        // Give boxA leftward velocity so it collides with wall, while boxB pushes into it
        boxA.setVelocityX(-2.0);
        boxB.setVelocityX(-2.0);
        for (int i = 0; i < 30; i++) {
            model.clockTick();
        }
        double boxABottom = boxA.getPos().y() + boxA.getHeight();
        double boxBBottom = boxB.getPos().y() + boxB.getHeight();
        assertTrue(boxABottom <= FLOOR_TOP + 0.5,
                "BoxA should stay on floor after collision, bottom=" + boxABottom);
        assertTrue(boxBBottom <= FLOOR_TOP + 0.5,
                "BoxB should stay on floor after collision, bottom=" + boxBBottom);
    }

    @Test
    void testBoxesDoNotOverlapAfterCollision() {
        // Push boxB into boxA against the wall for many ticks
        for (int i = 0; i < 60; i++) {
            boxB.setVelocityX(-2.0);
            model.clockTick();
        }
        double boxARight = boxA.getPos().x() + boxA.getWidth();
        double boxBLeft = boxB.getPos().x();
        assertTrue(boxBLeft >= boxARight - 0.5,
                "Boxes should not overlap, boxA right=" + boxARight + " boxB left=" + boxBLeft);
    }

    @Test
    void testRepeatedPushingDoesNotCauseFloorWarp() {
        // Simulate a player continuously pushing boxes left into wall
        for (int i = 0; i < 120; i++) {
            boxA.setVelocityX(-1.0);
            boxB.setVelocityX(-1.0);
            model.clockTick();

            double boxABottom = boxA.getPos().y() + boxA.getHeight();
            double boxBBottom = boxB.getPos().y() + boxB.getHeight();
            assertTrue(boxABottom <= FLOOR_TOP + 0.5,
                    "BoxA warped below floor at tick " + i + ", bottom=" + boxABottom);
            assertTrue(boxBBottom <= FLOOR_TOP + 0.5,
                    "BoxB warped below floor at tick " + i + ", bottom=" + boxBBottom);
        }
    }
}
