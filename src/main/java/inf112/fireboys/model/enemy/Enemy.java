package inf112.fireboys.model.enemy;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.IMovable;
import inf112.fireboys.model.entity.IStaticEntity;
import inf112.fireboys.model.entity.Pool;
import inf112.fireboys.model.player.IPlayer;

import java.util.Collections;
import java.util.List;

/** An enemy that patrols the level and chases players when they get close. */
public class Enemy implements IEnemy {
    private Position position;
    private double velocityX, velocityY;
    private double weight;
    private double height;
    private double width;
    private boolean alive;
    private boolean isOnGround = false;
    private boolean movingRight = true;
    private int jumpCooldown = 0;
    private static final double PATROL_SPEED = 0.3;
    private static final double DETECTION_RANGE_X = 25.0;
    private static final double DETECTION_RANGE_Y = 18.0;
    private static final double CHASE_SPEED = 0.5;
    private static final int ALERT_DURATION = 30;
    private final List<? extends IPlayer> players;
    private final List<? extends IStaticEntity> entities;
    private EnemyState state = EnemyState.PATROL;
    private int alertTimer = 0;
    /**
     * Creates an enemy with awareness of the given players and entities.
     * The entity list is used to detect edges so the enemy doesn't walk off platforms.
     */
    public Enemy(Position position, double width, double height,
            List<? extends IPlayer> players, List<? extends IStaticEntity> entities) {
        this.position = position;
        this.velocityX = PATROL_SPEED;
        this.velocityY = 0;
        this.weight = 1.0;
        this.height = height;
        this.width = width;
        this.alive = true;
        this.players = players;
        this.entities = entities;
    }

    /** Creates an enemy aware of players but with no entity list for edge detection. */
    public Enemy(Position position, double width, double height, List<? extends IPlayer> players) {
        this(position, width, height, players, Collections.emptyList());
    }

    /** Creates a standalone enemy with no players or entities to interact with. */
    public Enemy(Position position, double width, double height) {
        this(position, width, height, Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public void update() {
        boolean wasOnGround = isOnGround;
        isOnGround = false;
        if (jumpCooldown > 0)
            jumpCooldown--;
        IPlayer nearest = findNearestPlayerInRange();
        updateFSM(nearest);
        switch (state) {
            case PATROL -> updatePatrol(wasOnGround);
            case ALERT -> updateAlert();
            case CHASE -> updateChase(wasOnGround, nearest);
        }
    }

    private void updateFSM(IPlayer nearest) {
        switch (state) {
            case PATROL -> {
                if (nearest != null) {
                    state = EnemyState.ALERT;
                    alertTimer = ALERT_DURATION;
                }
            }
            case ALERT -> {
                if (nearest == null) {
                    state = EnemyState.PATROL;
                    alertTimer = 0;
                } else if (--alertTimer <= 0) {
                    state = EnemyState.CHASE;
                }
            }
            case CHASE -> {
                if (nearest == null)
                    state = EnemyState.PATROL;
            }
        }
    }

    private IPlayer findNearestPlayerInRange() {
        IPlayer nearest = null;
        double bestDist = Double.MAX_VALUE;
        for (IPlayer p : players) {
            if (!p.isAlive())
                continue;
            double dx = Math.abs(p.getPos().x() - position.x());
            double dy = Math.abs(p.getPos().y() - position.y());
            if (dx <= DETECTION_RANGE_X && dy <= DETECTION_RANGE_Y) {
                double dist = dx + dy;
                if (dist < bestDist) {
                    bestDist = dist;
                    nearest = p;
                }
            }
        }
        return nearest;
    }

    private void updatePatrol(boolean wasOnGround) {
        boolean blocked = Math.abs(velocityX) < PATROL_SPEED * 0.5;
        if (blocked && wasOnGround) {
            if (jumpCooldown == 0) {
                velocityY = -1.4;
                jumpCooldown = 45;
            } else {
                movingRight = !movingRight;
            }
        } else if (wasOnGround && !entities.isEmpty() && !hasGroundAhead(PATROL_SPEED)) {
            movingRight = !movingRight;
        }
        velocityX = movingRight ? PATROL_SPEED : -PATROL_SPEED;
    }

    private void updateAlert() {
        velocityX = 0;
    }

    private void updateChase(boolean wasOnGround, IPlayer target) {
        if (target == null)
            return;
        movingRight = target.getPos().x() > position.x();
        boolean blocked = Math.abs(velocityX) < CHASE_SPEED * 0.5;
        if (blocked && wasOnGround) {
            if (jumpCooldown == 0) {
                velocityY = -1.4;
                jumpCooldown = 45;
            } else {
                movingRight = !movingRight;
            }
        } else if (wasOnGround && !entities.isEmpty() && !hasGroundAhead(CHASE_SPEED)) {
            if (jumpCooldown == 0) {
                velocityY = -1.4;
                jumpCooldown = 45;
            }
        }
        velocityX = movingRight ? CHASE_SPEED : -CHASE_SPEED;
    }

    private boolean hasGroundAhead(double speed) {
        double footY = position.y() + height;
        double leadingX = movingRight ? position.x() + width + speed : position.x() - speed;
        for (IStaticEntity e : entities) {
            if (e instanceof Pool)
                continue;
            double ex = e.getPos().x();
            double ey = e.getPos().y();
            boolean xOverlap = leadingX < ex + e.getWidth() && leadingX + 0.5 > ex;
            boolean justBelow = ey >= footY && ey < footY + 2.0;
            if (xOverlap && justBelow)
                return true;
        }
        return false;
    }

    @Override
    public EnemyState getState() {
        return state;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void kill() {
        this.alive = false;
    }

    @Override
    public boolean isOnGround() {
        return isOnGround;
    }

    @Override
    public void setOnGroundTRUE() {
        this.isOnGround = true;
    }

    @Override
    public void setOnGroundFALSE() {
        this.isOnGround = false;
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
        if (movableEntity instanceof IPlayer player)
            player.kill();
    }
}
