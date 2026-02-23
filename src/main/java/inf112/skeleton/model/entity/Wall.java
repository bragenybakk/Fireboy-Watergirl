package inf112.skeleton.model.entity;

import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.player.IPlayer;

public class Wall extends StaticEntity {
    public Wall(Position position, double width, double height) {
        super(position, width, height);
    }

    @Override
    protected void contactAction(IPlayer player, CollisionSide side) {
        Position pos = player.getPos();
        switch (side) {
            case LEFT:
                player.setPos(new Position(this.getPos().x() - player.getWidth(), pos.y()));
                player.setVelocityX(0);
                break;
            case RIGHT:
                player.setPos(new Position(this.getPos().x() + this.getWidth(), pos.y()));
                player.setVelocityX(0);
                break;
            case TOP:
                player.setPos(new Position(pos.x(), this.getPos().y() - player.getHeight()));
                player.setVelocityY(0);
                break;
            case BOTTOM:
                player.setPos(new Position(pos.x(), this.getPos().y() + this.getHeight()));
                player.setVelocityY(0);
                break;
        }
    }
}
