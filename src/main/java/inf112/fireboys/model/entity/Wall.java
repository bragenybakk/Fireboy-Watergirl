package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.player.IPlayer;

public class Wall extends StaticEntity {
    public Wall(Position position, double width, double height) {
        super(position, width, height);
    }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        Position pos = movableEntity.getPos();
        switch (side) {
            case LEFT:
                movableEntity.setPos(new Position(this.getPos().x() - movableEntity.getWidth(), pos.y()));
                movableEntity.setVelocityX(0);
                if (movableEntity instanceof IPlayer) {
                    IPlayer player = (IPlayer) movableEntity;
                    player.setOnGroundTRUE();
                }
                break;
            case RIGHT:
                movableEntity.setPos(new Position(this.getPos().x() + this.getWidth(), pos.y()));
                movableEntity.setVelocityX(0);
                if (movableEntity instanceof IPlayer) {
                    IPlayer player = (IPlayer) movableEntity;
                    player.setOnGroundTRUE();
                }
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
}
