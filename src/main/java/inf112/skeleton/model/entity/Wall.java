package inf112.skeleton.model.entity;

import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.player.IPlayer;

public class Wall extends StaticEntity {
    public Wall(Position position, double width, double height) {
        super(position, width, height);
    }

    @Override
    public void whenContact(IPlayer player) {
        throw new UnsupportedOperationException("Unimplemented method 'whenContact'");
    }
}
