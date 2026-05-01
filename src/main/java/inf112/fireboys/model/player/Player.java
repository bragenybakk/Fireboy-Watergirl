package inf112.fireboys.model.player;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.entity.IMovable;

/**
 * Represents a player in the game.
 * Implements IPlayer to support movement, velocity, and physics interactions.
 */
public class Player implements IPlayer {
    private ElementState elementState;
    private double velocityX;
    private double velocityY;
    private Position position;
    private Position startPos;
    private double height;
    private double width;
    private double weight;
    private boolean isOnGround;
    private boolean alive;
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
        this.elementState = elementState;
        this.velocityX = 0;
        this.velocityY = 0;
        this.position = position;
        this.startPos = position;
        this.height = 8.0;
        this.width = 8.0;
        this.weight = 1.0;
        this.isOnGround = true;
        this.alive = true;
    }

    @Override
    public ElementState getElementState() {
        return elementState;
    }

    @Override
    public Position getStartPos() {
        return startPos;
    }

    @Override
    public double getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    @Override
    public Position getPos() {
        return position;
    }

    @Override
    public void setPos(Position pos) {
        this.position = pos;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public boolean isOnGround() {
        return isOnGround;
    }

    @Override
    public void setOnGroundTRUE() {
        this.isOnGround = true;
    }

    @Override
    public void setOnGroundFALSE() {
        this.isOnGround = false;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void kill() {
        this.alive = false;
    }

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
        return;
    }
}
