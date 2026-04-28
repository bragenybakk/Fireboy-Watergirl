package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.player.IPlayer;

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

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        Position pos = movableEntity.getPos();
        switch (side) {
            case LEFT:
                movableEntity.setPos(new Position(this.getPos().x() - movableEntity.getWidth(), pos.y()));
                movableEntity.setVelocityX(0);
                break;
            case RIGHT:
                movableEntity.setPos(new Position(this.getPos().x() + this.getWidth(), pos.y()));
                movableEntity.setVelocityX(0);
                break;
            case TOP:
                movableEntity.setPos(new Position(pos.x(), this.getPos().y() - movableEntity.getHeight()));
                movableEntity.setVelocityY(0);
                if (movableEntity instanceof IPlayer) {
                    IPlayer player = (IPlayer) movableEntity;
                    player.setOnGroundTRUE();
                } else if (movableEntity instanceof IEnemy) {
                    IEnemy enemy = (IEnemy) movableEntity;
                    enemy.setOnGroundTRUE();
                }
                break;
            case BOTTOM:
                movableEntity.setPos(new Position(pos.x(), this.getPos().y() + this.getHeight()));
                movableEntity.setVelocityY(0);
                break;
            default:
                break;
        }
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