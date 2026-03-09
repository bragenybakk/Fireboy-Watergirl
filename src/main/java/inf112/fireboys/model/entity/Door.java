package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.GameModel;
import inf112.fireboys.model.player.IPlayer;

public class Door extends StaticEntity {
    private boolean isOpen;
    public Door(Position position, double width, double height) {
        super(position, width, height);
        this.isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    @Override
    protected void contactAction(IPlayer player, CollisionSide side) {
        setOpen(true);
    }
}