package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/**
 * Moving walls
 */
public class MovingPlatform extends Wall {
    private final Position startPos;
    private final double dirX;
    private final double dirY;
    private final double speed;
    private final double distance;
    private double distanceTraveled = 0;
    private int travelDir = 1;
    private double deltaX = 0;
    private double deltaY = 0;
    public MovingPlatform(Position position, double width, double height,
            double dirX, double dirY, double speed, double distance) {
        super(position, width, height);
        this.startPos = position;
        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        this.dirX = len > 0 ? dirX / len : 0;
        this.dirY = len > 0 ? dirY / len : 0;
        this.speed = speed;
        this.distance = distance;
    }

    public void tick() {
        deltaX = dirX * speed * travelDir;
        deltaY = dirY * speed * travelDir;
        distanceTraveled += speed * travelDir;
        if (distanceTraveled >= distance) {
            distanceTraveled = distance;
            travelDir = -1;
        } else if (distanceTraveled <= 0) {
            distanceTraveled = 0;
            travelDir = 1;
        }
        setPos(new Position(startPos.x() + dirX * distanceTraveled, startPos.y() + dirY * distanceTraveled));
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }
}
