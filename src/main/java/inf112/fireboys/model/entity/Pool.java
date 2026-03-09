package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.player.IPlayer;

public class Pool extends StaticEntity {
    private ElementState element;
    public Pool(Position position, double width, double height, ElementState element) {
        super(position, width, height);
        this.element = element;
    }

    public ElementState getElement() {
        return element;
    }

    public void setElement(ElementState element) {
        this.element = element;
    }

    @Override
    protected void contactAction(IPlayer player, CollisionSide side) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contactAction'");
    }
}
