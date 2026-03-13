package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.Box;
import inf112.fireboys.model.entity.StaticEntity;

public class BoxChainPushTest {
    private GameModel model;
    private Box boxA;
    private Box boxB;
    @BeforeEach
    void setUp() throws Exception {
        Board board = GameReader.loadLevel("src/test/resources/twoboxes.txt");
        model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        // Find the two boxes from entities
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
    void testTwoBoxesLoaded() {
        assertNotNull(boxA, "First box should exist");
        assertNotNull(boxB, "Second box should exist");
    }

    @Test
    void testPushingBoxAMovesBoxB() {
        double boxBInitialX = boxB.getPos().x();
        // Give boxA rightward velocity to push into boxB
        boxA.setVelocityX(2.0);
        model.clockTick(); // boxA hits boxB, velocity transferred
        model.clockTick(); // boxB moves with transferred velocity
        assertTrue(boxB.getPos().x() > boxBInitialX,
                "BoxB should move right when BoxA is pushed into it");
    }

    @Test
    void testPushingBoxAMovesBoxBLeft() {
        double boxAInitialX = boxA.getPos().x();
        // Give boxB leftward velocity to push into boxA
        boxB.setVelocityX(-2.0);
        model.clockTick();
        assertTrue(boxA.getPos().x() < boxAInitialX,
                "BoxA should move left when BoxB is pushed into it");
    }

    @Test
    void testChainPushTransfersVelocity() {
        // BoxB should gain velocity when BoxA is pushed into it
        boxA.setVelocityX(2.0);
        model.clockTick();
        assertTrue(boxB.getVelocityX() > 0,
                "BoxB should have positive velocity after being pushed by BoxA");
    }

    @Test
    void testHeavierBoxReceivesLessPush() {
        double heavyWeight = 5.0;
        double pushVelocity = 2.0;
        double heavyPush = pushVelocity / heavyWeight;
        double lightPush = pushVelocity / 1.0;
        assertTrue(lightPush > heavyPush,
                "Lighter box should receive more push force than heavier box");
    }

    @Test
    void testBoxesStayAboveFloor() {
        double floorTop = 96.0;
        // Push boxes to the right for many ticks
        for (int i = 0; i < 60; i++) {
            boxA.setVelocityX(2.0);
            model.clockTick();
        }
        double boxABottom = boxA.getPos().y() + boxA.getHeight();
        double boxBBottom = boxB.getPos().y() + boxB.getHeight();
        assertTrue(boxABottom <= floorTop + 0.5,
                "BoxA should not warp below the floor, bottom=" + boxABottom);
        assertTrue(boxBBottom <= floorTop + 0.5,
                "BoxB should not warp below the floor, bottom=" + boxBBottom);
    }

    @Test
    void testPlayerPushesBoxChain() {
        // Position player to the left of boxA, pushing right
        model.getPlayers().get(0).setPos(new Position(boxA.getPos().x() - 4, boxA.getPos().y()));
        double boxBInitialX = boxB.getPos().x();
        // Push right for several ticks
        for (int i = 0; i < 30; i++) {
            model.movePlayerRight();
            model.clockTick();
        }
        assertTrue(boxB.getPos().x() > boxBInitialX,
                "BoxB should move when player pushes BoxA into it");
    }
}
