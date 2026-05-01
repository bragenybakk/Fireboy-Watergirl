package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/** Abstract class for movable game entities (like players and enemies). */
public abstract class Movable implements IMovable {
    protected Position position;
    protected double velocityX;
    protected double velocityY;
    protected double weight;
    protected double height;
    protected double width;
    protected boolean alive;
    protected boolean isOnGround;
    protected Movable(Position position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.velocityX = 0;
        this.velocityY = 0;
        this.weight = 1.0;
        this.alive = true;
        this.isOnGround = false;
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
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
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

    @Override
    public double getWeight() {
        return weight;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    @Override
    public void setOnGroundTRUE() {
        this.isOnGround = true;
    }

    public void setOnGroundFALSE() {
        this.isOnGround = false;
    }

    @Override
    public abstract void whenContact(IMovable movableEntity);
}
