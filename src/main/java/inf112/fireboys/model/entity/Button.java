package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

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
    public void whenContact(IMovable movableEntity) {
        throw new UnsupportedOperationException("Unimplemented method 'whenContact'");
    }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contactAction'");
    }
}
