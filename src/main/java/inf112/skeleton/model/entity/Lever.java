package inf112.skeleton.model.entity;

import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.player.IPlayer;

public class Lever extends StaticEntity {
    private boolean isActivated;
    public Lever(Position position, double width, double height) {
        super(position, width, height);
        this.isActivated = false;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        this.isActivated = activated;
    }

    @Override
    public void whenContact(IPlayer player) {
        throw new UnsupportedOperationException("Unimplemented method 'whenContact'");
    }
}
