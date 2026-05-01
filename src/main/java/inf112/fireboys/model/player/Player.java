package inf112.fireboys.model.player;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.entity.Movable;
import inf112.fireboys.model.entity.IMovable;

/**
 * Represents a player in the game.
 * Implements IPlayer to support movement, velocity, and physics interactions.
 */
public class Player extends Movable implements IPlayer {
    private ElementState elementState;
    private Position startPos;
    private boolean jumpBoostCharged = false;
    private static final double JUMP_IMPULSE_NORMAL = -1.26;
    private static final double JUMP_IMPULSE_BOOSTED = JUMP_IMPULSE_NORMAL * 1.5;

    /**
     * Constructs a Player with the given position and element state.
     *
     * @param position
     *            the initial position of the player
     * @param elementState
     *            the element state (e.g., FIRE, WATER)
     */
    public Player(Position position, ElementState elementState) {
        super(position, 8.0, 8.0);
        this.elementState = elementState;
        this.startPos = position;
        this.isOnGround = true;
    }

    @Override
    public ElementState getElementState() {
        return elementState;
    }

    @Override
    public Position getStartPos() {
        return startPos;
    }

    /** Charges a jump boost so the next jump will be stronger. */
    @Override
    public void grantJumpBoost() {
        jumpBoostCharged = true;
    }

    @Override
    public boolean hasJumpBoost() {
        return jumpBoostCharged;
    }

    @Override
    public double getJumpImpulse() {
        return jumpBoostCharged ? JUMP_IMPULSE_BOOSTED : JUMP_IMPULSE_NORMAL;
    }

    @Override
    public void consumeJumpBoost() {
        jumpBoostCharged = false;
    }

    @Override
    public void whenContact(IMovable movableEntity) {
    }
}
