package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/** A pushable box that players can move around the level. */
public class Box extends StaticEntity implements IMovable {
    private double velocityX;
    private double velocityY;
    private double weight;

    /**
     * Creates a box at the given position with the given size and weight.
     *
     * @param position
     *            the initial position of the box
     * @param width
     *            the width of the box
     * @param height
     *            the height of the box
     * @param weight
     *            the weight, which affects how hard it is to push
     */
    public Box(Position position, double width, double height, double weight) {
        super(position, width, height);
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public double getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(double vx) {
        this.velocityX = vx;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(double vy) {
        this.velocityY = vy;
    }
}
