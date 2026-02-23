package inf112.skeleton.model.entity;

import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.player.IPlayer;

public class Button extends StaticEntity {
    private boolean isPressed;
    public Button(Position position, double width, double height) {
        super(position, width, height);
        this.isPressed = false;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        this.isPressed = pressed;
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
