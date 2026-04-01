package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.player.IPlayer;

public class Gem extends StaticEntity {
    private boolean collected;

    public Gem(Position position, double width, double height) {
        super(position, width, height);
        this.collected = false;
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        if (movableEntity instanceof IPlayer player) {
            player.addScore(1);
            this.collected = true;
        }
    }
}
