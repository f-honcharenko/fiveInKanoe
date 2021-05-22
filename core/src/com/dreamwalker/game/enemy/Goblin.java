package com.dreamwalker.game.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.player.Player;

public class Goblin extends Enemy {

    private Player player;

    private int attackSpeedMax;
    private int attackSpeedCounter;

    private float tempX;
    private float tempY;
    private int idleTimer;

    private int idleTimerMax;
    private int waitingTimerMax;
    private int agroTimerMax;

    private float spawnX;
    private float spawnY;
    private float idleRadius;
    private float agroRadius;
    private float attackRadius;

    private Boolean attackFlag = true;
    // private Boolean idleFlag = true;
    private String status = "waiting";

    /**
     * Конструктор
     * 
     * @param player - обьект игрока
     * @param x      - стартовая позиция врага по х
     * @param y      - стартовая позиция врага по у
     */
    public Goblin(Player player, float x, float y) {
        super(player, x, y);
        this.player = player;

        this.enemysAnimations = new Animations(this, "template.atlas", "template");
        this.setBoundsCustom(60f, 60f); // 54
        // this.setBounds(0, 0, 54, 54);
        this.spawnX = x;
        this.spawnY = y;

        this.speed = 0.2f;
        this.damage = 40;
        this.health = 100;

        this.attackSpeedMax = 100;
        this.attackSpeedCounter = 0;
        this.idleTimer = 0;
        this.idleTimerMax = rnd(50, 200);
        this.waitingTimerMax = rnd(50, 100);
        this.agroTimerMax = 150;
        this.idleRadius = 150 / DreamWalker.PPM;
        this.agroRadius = 100 / DreamWalker.PPM;
        this.attackRadius = 50 / DreamWalker.PPM;
    }

    /**
     * Конструктор
     * 
     * @param player          - обьект игрока
     * @param enemySpawnPoint - позиция врага
     */
    public Goblin(Player player, Vector2 enemySpawnPoint) {
        this(player, enemySpawnPoint.x, enemySpawnPoint.y);
    }

    @Override
    public void attack() {
        if (this.isPlayerInArea()) {
            this.setIsAttacking(true);
            if (attackSpeedCounter < attackSpeedMax) {
                attackSpeedCounter++;
            } else {
                attackSpeedCounter = 0;
                this.player.receiveDamage(this.damage);
            }
        } else {
            this.setIsAttacking(false);
        }
    }

    @Override
    public void idle() {
        this.idleTimer++;
        // Создать временные точки для перемещения
        if (Vector2.dst(super.getX(), super.getY(), player.getX(), player.getY()) < this.agroRadius) {
            this.status = "agro";
            Vector2 playerPosition = new Vector2(player.getX(), player.getY());
            Vector2 goblinViewPoint = playerPosition.sub(super.getEnemysBody().getPosition());
            this.setViewAngle(Math.toDegrees(goblinViewPoint.angleRad()));
            super.getEnemysBody().setTransform(super.getEnemysBody().getPosition(), goblinViewPoint.angleRad());
            super.getEnemysBody().setLinearVelocity(new Vector2((player.getX() - super.getX()) * this.speed,
                    (player.getY() - super.getY()) * this.speed));
        }
        if ((this.status == "agro") && (this.idleTimer == this.agroTimerMax)) {
            this.status = "waiting";
            super.getEnemysBody().setLinearVelocity(new Vector2(0, 0));
        }
        if ((this.idleTimer == this.idleTimerMax) || ((this.tempX == super.getX()) && (this.tempY == super.getY()))) {
            // Если таймер(время ожидания) истек, или непись уже на месте.
            // Меняем статус и обнулялем таймер
            super.getEnemysBody().setLinearVelocity(new Vector2(0, 0));
            this.status = "waiting";
            this.idleTimer = 0;

        }
        if ((this.status == "waiting") && (this.idleTimer == this.waitingTimerMax)) {
            // Если непись постоял на месте н-ое еол-во секунд,
            // запускаем его на случаную коордианту и меняем статус
            this.tempX = rnd(Math.round(this.spawnX - this.idleRadius), Math.round(this.spawnX + this.idleRadius));
            this.tempY = rnd(Math.round(this.spawnY - this.idleRadius), Math.round(this.spawnY + this.idleRadius));
            this.idleTimer = 0;
            this.status = "idleGo";
        }
        if (this.status == "idleGo") {
            // Повернуть непися
            Vector2 goblinViewPoint = new Vector2(this.tempX, this.tempY).sub(super.getEnemysBody().getPosition());
            this.setViewAngle(Math.toDegrees(goblinViewPoint.angleRad()));
            super.getEnemysBody().setTransform(super.getEnemysBody().getPosition(), goblinViewPoint.angleRad());
            // Задать ему скорость
            super.getEnemysBody().setLinearVelocity(
                    new Vector2((this.tempX - super.getX()) * this.speed, (this.tempY - super.getY()) * this.speed));
        }
        this.setPosition(this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (this.isAlive()) {
            this.draw(batch);
            this.drawBar(batch);
        }
    }

    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
