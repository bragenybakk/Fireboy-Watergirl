package inf112.skeleton.model.player;

import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.ElementState;

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
        this.height = 4.0;
        this.width = 4.0;
        this.weight = 1.0;
        this.isOnGround = true;
    }

    public ElementState getElementState() {
        return elementState;
    }

    @Override
    public Position getStartPos() {
        return startPos;
    }

    public void setElementState(ElementState elementState) {
        this.elementState = elementState;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public Position getPos() {
        return position;
    }

    public void setPos(Position pos) {
        this.position = pos;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public void setOnGroundTRUE() {
        this.isOnGround = true;
    }

    public void setOnGroundFALSE() {
        this.isOnGround = false;
    }

    /**
     * Sets the weight of the player, affecting physics calculations.
     * 
     * @param weight
     *            the new weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void whenContact(IPlayer player) {
    }
}
