package inf112.fireboys.model.enemy;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.IMovable;
import inf112.fireboys.model.player.IPlayer;

public class Enemy implements IEnemy {
    private Position position;
    private double velocityX, velocityY;
    private double weight;
    private double height;
    private double width;
    private boolean alive;
    public Enemy(Position position) {
        this.position = position;
        this.velocityX = 0;
        this.velocityY = 0;
        this.weight = 1.0;
        this.height = 4.0;
        this.width = 4.0;
        this.alive = true;
    }

    @Override
    public void update() {
        // Implement AI behavior here
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public Position getPos() {
        return position;
    }

    @Override
    public void setPos(Position pos) {
        this.position = pos;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(double vx) {
        this.velocityX = vx;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(double vy) {
        this.velocityY = vy;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void whenContact(IMovable movableEntity) {
        if (movableEntity instanceof IPlayer) {
            IPlayer player = (IPlayer) movableEntity;
            player.kill();
        }
    }
}
