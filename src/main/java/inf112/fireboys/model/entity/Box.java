package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.player.IPlayer;

public class Box extends StaticEntity implements IMovable {
    private double velocityX;
    private double velocityY;
    private double weight;
    public Box(Position position, double width, double height, double weight) {
        super(position, width, height);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public void whenContact(IPlayer player) {
        return;
    }

    @Override
    protected void contactAction(IPlayer player, CollisionSide side) {
        return;
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