package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.player.IPlayer;

public class Gem extends StaticEntity {
    private boolean collected;
    private ElementState element;

    public Gem(Position position, double width, double height, ElementState element) {
        super(position, width, height);
        this.collected = false;
        this.element = element;
    }

    public ElementState getElement() {
        return element;
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        if (!collected && movableEntity instanceof IPlayer player && player.getElementState() == element) {
            player.addScore(1);
            this.collected = true;
        }
    }
}
