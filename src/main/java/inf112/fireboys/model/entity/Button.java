package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

public class Button extends Wall {
    private boolean isPressed = false;
    private boolean trapSpawned = false;
    private final Position trapPos;
    private final double trapWidth;
    private final double trapHeight;

    public Button(Position position, double width, double height,
                  Position trapPos, double trapWidth, double trapHeight) {
        super(position, width, height);
        this.trapPos = trapPos;
        this.trapWidth = trapWidth;
        this.trapHeight = trapHeight;
    }

    public boolean isPressed() { return isPressed; }
    public boolean isTrapSpawned() { return trapSpawned; }
    public void markTrapSpawned() { trapSpawned = true; }
    public Position getTrapPos() { return trapPos; }
    public double getTrapWidth() { return trapWidth; }
    public double getTrapHeight() { return trapHeight; }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        super.contactAction(movableEntity, side);
        if (side == CollisionSide.TOP) {
            isPressed = true;
        }
    }
}
