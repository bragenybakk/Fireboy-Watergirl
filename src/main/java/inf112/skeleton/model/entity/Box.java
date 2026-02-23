package inf112.skeleton.model.entity;

import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.player.IPlayer;

public class Box extends StaticEntity {
    public Box(Position position, double width, double height, double weight) {
        super(position, width, height);
        this.weight = weight;
    }
    private double weight;
    public double getWeight() {
        return weight;
    }

    @Override
    public void whenContact(IPlayer player) {
        throw new UnsupportedOperationException("Unimplemented method 'whenContact'");
    }

    @Override
    protected void contactAction(IPlayer player, CollisionSide side) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contactAction'");
    }
}