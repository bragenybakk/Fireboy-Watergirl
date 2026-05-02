package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/** A pushable box that players can move around the level. */
public class Box extends StaticEntity implements IMovable {
    private double velocityX;
    private double velocityY;
    private double weight;

    /** Creates a box at the given position with the given size and weight. */
    public Box(Position position, double width, double height, double weight) {
        super(position, width, height);
        this.weight = weight;
    }

    /** Returns the weight of this box, which affects how hard it is to push. */
    public double getWeight() {
        return weight;
    }

    /** Returns the current horizontal velocity of this box. */
    @Override
    public double getVelocityX() {
        return velocityX;
    }

    /** Sets the horizontal velocity of this box. */
    @Override
    public void setVelocityX(double vx) {
        this.velocityX = vx;
    }

    /** Returns the current vertical velocity of this box. */
    @Override
    public double getVelocityY() {
        return velocityY;
    }

    /** Sets the vertical velocity of this box. */
    @Override
    public void setVelocityY(double vy) {
        this.velocityY = vy;
    }
}