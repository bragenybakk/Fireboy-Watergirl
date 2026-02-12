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
    private double height;
    private double width;
    private double weight;
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
        this.height = 1.0;
        this.width = 1.0;
        this.weight = 1.0;
    }

    public ElementState getElementState() {
        return elementState;
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
        // Implement contact behavior with other players when needed
    }
}
