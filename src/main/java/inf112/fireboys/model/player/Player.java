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
    private int score = 0;
    private boolean jumpBoostCharged = false;
    private static final double JUMP_IMPULSE_NORMAL = -1.8;
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

    /** Returns the element type of this player (FIRE or WATER). */
    public ElementState getElementState() {
        return elementState;
    }

    /** Returns the spawn position of this player. */
    @Override
    public Position getStartPos() {
        return startPos;
    }

    /** Returns the current horizontal velocity. */
    public double getVelocityX() {
        return velocityX;
    }

    /** Sets the horizontal velocity. */
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    /** Returns the current vertical velocity. */
    public double getVelocityY() {
        return velocityY;
    }

    /** Sets the vertical velocity. */
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    /** Returns the current position of this player. */
    public Position getPos() {
        return position;
    }

    /** Sets the position of this player. */
    public void setPos(Position pos) {
        this.position = pos;
    }

    /** Returns the height of this player. */
    public double getHeight() {
        return height;
    }

    /** Returns the width of this player. */
    public double getWidth() {
        return width;
    }

    /** Returns the weight of this player, used in physics calculations. */
    public double getWeight() {
        return weight;
    }

    /** Returns true if the player is currently standing on the ground. */
    public boolean isOnGround() {
        return isOnGround;
    }

    /** Marks the player as standing on the ground. */
    public void setOnGroundTRUE() {
        this.isOnGround = true;
    }

    /** Marks the player as airborne. */
    public void setOnGroundFALSE() {
        this.isOnGround = false;
    }

    /** Returns true if the player is still alive. */
    public boolean isAlive() {
        return alive;
    }

    /** Kills this player. */
    public void kill() {
        this.alive = false;
    }

    /** Adds the given number of points to this player's score. */
    public void addScore(int points) {
        this.score += points;
    }

    /** Returns the current score of this player. */
    public int getScore() {
        return score;
    }

    /** Charges a jump boost so the next jump will be stronger. */
    @Override
    public void grantJumpBoost() {
        jumpBoostCharged = true;
    }

    /** Returns true if this player has a jump boost ready. */
    @Override
    public boolean hasJumpBoost() {
        return jumpBoostCharged;
    }

    /** Returns the jump impulse — stronger if a boost is charged. */
    @Override
    public double getJumpImpulse() {
        return jumpBoostCharged ? JUMP_IMPULSE_BOOSTED : JUMP_IMPULSE_NORMAL;
    }

    /** Consumes the jump boost after it has been used. */
    @Override
    public void consumeJumpBoost() {
        jumpBoostCharged = false;
    }

    @Override
    public void whenContact(IMovable movableEntity) {
        return;
    }
}
