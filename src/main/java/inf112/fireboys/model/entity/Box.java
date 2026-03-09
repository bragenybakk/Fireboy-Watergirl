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
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        Position pos = movableEntity.getPos();
        switch (side) {
            case LEFT:
                movableEntity.setPos(new Position(this.getPos().x() - movableEntity.getWidth(), pos.y()));
                break;
            case RIGHT:
                movableEntity.setPos(new Position(this.getPos().x() + this.getWidth(), pos.y()));
                break;
            case TOP:
                movableEntity.setPos(new Position(pos.x(), this.getPos().y() - movableEntity.getHeight()));
                movableEntity.setVelocityY(0);
                if (movableEntity instanceof IPlayer) {
                    IPlayer player = (IPlayer) movableEntity;
                    player.setOnGroundTRUE();
                }
                break;
            case BOTTOM:
                movableEntity.setPos(new Position(pos.x(), this.getPos().y() + this.getHeight()));
                movableEntity.setVelocityY(0);
                break;
        }
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