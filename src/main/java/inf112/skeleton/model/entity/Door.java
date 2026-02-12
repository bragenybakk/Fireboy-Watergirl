package inf112.skeleton.model.entity;

import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.player.IPlayer;

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
    public void whenContact(IPlayer player) {
        throw new UnsupportedOperationException("Unimplemented method 'whenContact'");
    }
}