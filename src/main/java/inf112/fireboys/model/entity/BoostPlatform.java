package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.player.IPlayer;

/**
 * Pressure plate: wall-like collision. First jump on the plate only charges
 * (purple glow); the next jump elsewhere uses boost (see GameModel.playerJump).
 */
public class BoostPlatform extends StaticEntity {

    public BoostPlatform(Position position, double width, double height) {
        super(position, width, height);
    }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        Position pos = movableEntity.getPos();
        if (side == CollisionSide.LEFT) {
            movableEntity.setPos(new Position(this.getPos().x() - movableEntity.getWidth(), pos.y()));
            movableEntity.setVelocityX(0);
        } else if (side == CollisionSide.RIGHT) {
            movableEntity.setPos(new Position(this.getPos().x() + this.getWidth(), pos.y()));
            movableEntity.setVelocityX(0);
        } else if (side == CollisionSide.TOP) {
            movableEntity.setPos(new Position(pos.x(), this.getPos().y() - movableEntity.getHeight()));
            movableEntity.setVelocityY(0);
            if (movableEntity instanceof IPlayer player) {
                player.setOnGroundTRUE();
            } else if (movableEntity instanceof IEnemy enemy) {
                enemy.setOnGroundTRUE();
            }
        } else if (side == CollisionSide.BOTTOM) {
            movableEntity.setPos(new Position(pos.x(), this.getPos().y() + this.getHeight()));
            movableEntity.setVelocityY(0);
        }
    }
}
