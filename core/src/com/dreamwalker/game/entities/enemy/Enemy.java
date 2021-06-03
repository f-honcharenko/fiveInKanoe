package com.dreamwalker.game.entities.enemy;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.Entity;
import com.dreamwalker.game.entities.player.Player;
import com.dreamwalker.game.items.Item;
import com.dreamwalker.game.items.ItemInWorld;
import com.dreamwalker.game.items.PotionHP;
import com.dreamwalker.game.items.PotionMP;
import com.dreamwalker.game.location.Location;

import java.util.Random;

public abstract class Enemy extends Entity implements Disposable {
    protected int attackSpeedMax;
    protected int attackSpeedCounter;

    private int attackedFilterTimerMax;
    private int attackedFilterTimer;
    private Boolean attackedFilterFlag;

    private float BarWidth;
    private float BarHeight;

    private float BoundsWidth;
    private float BoundsHeight;

    private boolean playerInArea;

    private Texture HPTexture;
    private Texture HPBarTexture;

    protected int respawnTime;
    protected int respawnCounter;

    private Random rnd;
    private String status = "waiting";

    private float tempX;
    private float tempY;
    private int idleTimer;
    protected float spawnX;
    protected float spawnY;
    protected float idleRadius;
    protected float agroRadius;
    protected int waitingTimerMax;
    protected int agroTimerMax;
    protected int idleTimerMax;

    public boolean haveToDropped;

    protected Location location;

    /**
     * Конструктор
     *
     *
     * @param x - стартовая позиция врага по х
     * @param y - стартовая позиция врага по у
     */
    public Enemy(Location location, float x, float y) {
        this.location = location;
        this.world = location.getWorld();
        this.spawnPoint = new Vector2(x, y);
        this.defineEnemy(this.spawnPoint);

        this.haveToDropped = false;
        // Изменяемые параметры
        this.speed = 1.5f;
        this.isAlive = true;
        this.isAttacking = false;
        this.isDamageDealt = false;
        this.health = this.healthMax = 100;
        this.BarWidth = this.BoundsWidth;
        this.BarHeight = 10 / DreamWalker.PPM;
        // this.BoundsWidth = 54 / DreamWalker.PPM;
        this.BoundsWidth = 54 / DreamWalker.PPM;
        this.BoundsHeight = 54 / DreamWalker.PPM;

        this.attackedFilterTimerMax = 15;
        this.attackedFilterTimer = 0;
        this.attackSpeedCounter = 0;

        this.playerInArea = false;
        this.attackSpeedCoefficient = 1.5f;
        this.respawnCounter = 0;

        this.idleTimer = 0;
    }

    public void respawn() {
        if (!this.isAlive) {
            this.respawnCounter++;
            this.entityBody.getFixtureList().get(0).setSensor(true);
            if (this.respawnCounter == this.respawnTime) {
                this.entityBody.getFixtureList().get(0).setSensor(false);
                this.entityBody.setTransform(this.spawnPoint.x, this.spawnPoint.y, (float) this.viewAngle);
                this.health = this.healthMax;
                this.isAlive = true;
                this.respawnCounter = 0;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (this.isAlive()) {
            this.draw(batch);
            this.drawBar(batch);
        }
    }

    private void defineEnemy(Vector2 spawnPoint) {
        // Задача физических свойств для "тела" врага
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnPoint);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Создаем физическое "тело" врага в игровом мире на основе свойств
        this.entityBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        // Физические границы врага
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / DreamWalker.PPM);

        fixtureDef.shape = shape;
        this.entityBody.createFixture(fixtureDef);

        this.entityBody.getFixtureList().get(0).setUserData(this);
        // Удаляем фигуру, которая была создана для "тела" врага
        shape.dispose();
        this.HPTexture = new Texture(createProceduralPixmap(1, 1, 1, 0, 0));
        this.HPBarTexture = new Texture("EnemyHPBar.png");
        this.setBounds(0, 0, this.BoundsWidth, this.BoundsHeight);

        // Сектор Атакаи врага
        float scalar = shape.getRadius() * 3;
        this.attackArea = this.world.createBody(bodyDef);
        FixtureDef attackFixture = new FixtureDef();
        PolygonShape dmgSectorShape = new PolygonShape();

        Vector2[] vertices = { new Vector2(0, 0),
                new Vector2(scalar * (float) (Math.cos(5 * Math.PI / 3)), scalar * (float) (Math.sin(5 * Math.PI / 3))),
                new Vector2(scalar * (float) (Math.cos(7 * Math.PI / 4)), scalar * (float) (Math.sin(7 * Math.PI / 4))),
                new Vector2(scalar * (float) (Math.cos(11 * Math.PI / 6)),
                        scalar * (float) (Math.sin(11 * Math.PI / 6))),
                new Vector2(scalar * (float) (Math.cos(0)), scalar * (float) (Math.sin(0))), // -----Середина------
                new Vector2(scalar * (float) (Math.cos(Math.PI / 6)), scalar * (float) (Math.sin(Math.PI / 6))),
                new Vector2(scalar * (float) (Math.cos(Math.PI / 4)), scalar * (float) (Math.sin(Math.PI / 4))),
                new Vector2(scalar * (float) (Math.cos(Math.PI / 3)), scalar * (float) (Math.sin(Math.PI / 3))) };

        dmgSectorShape.set(vertices);
        attackFixture.shape = dmgSectorShape;
        attackFixture.isSensor = true;
        this.attackArea.createFixture(attackFixture);
        this.attackArea.getFixtureList().get(0).setUserData(this);

        dmgSectorShape.dispose();
        this.rnd = new Random();
    }

    public void idle(Player player) {
        this.idleTimer++;
        // Создать временные точки для перемещения
        if (Vector2.dst(super.getX(), super.getY(), player.getX(), player.getY()) < this.agroRadius) {
            this.status = "agro";
            Vector2 playerPosition = new Vector2(player.getX(), player.getY());
            Vector2 goblinViewPoint = playerPosition.sub(super.entityBody.getPosition());
            this.setViewAngle(Math.toDegrees(goblinViewPoint.angleRad()));
            super.entityBody.setTransform(super.entityBody.getPosition(), goblinViewPoint.angleRad());
            super.entityBody.setLinearVelocity(new Vector2((player.getX() - super.getX()) * this.speed,
                    (player.getY() - super.getY()) * this.speed));
        }
        if ((this.status.equals("agro")) && (this.idleTimer == this.agroTimerMax)) {
            this.status = "waiting";
            super.entityBody.setLinearVelocity(new Vector2(0, 0));
        }
        if ((this.idleTimer == this.idleTimerMax) || ((this.tempX == super.getX()) && (this.tempY == super.getY()))) {
            // Если таймер(время ожидания) истек, или непись уже на месте.
            // Меняем статус и обнулялем таймер
            super.entityBody.setLinearVelocity(new Vector2(0, 0));
            this.status = "waiting";
            this.idleTimer = 0;

        }
        if ((this.status.equals("waiting")) && (this.idleTimer == this.waitingTimerMax)) {
            // Если непись постоял на месте н-ое еол-во секунд,
            // запускаем его на случаную коордианту и меняем статус
            this.tempX = rnd(Math.round(this.spawnX - this.idleRadius), Math.round(this.spawnX + this.idleRadius));
            this.tempY = rnd(Math.round(this.spawnY - this.idleRadius), Math.round(this.spawnY + this.idleRadius));
            this.idleTimer = 0;
            this.status = "idleGo";
        }
        if (this.status.equals("idleGo")) {
            // Повернуть непися
            Vector2 goblinViewPoint = new Vector2(this.tempX, this.tempY).sub(super.entityBody.getPosition());
            this.setViewAngle(Math.toDegrees(goblinViewPoint.angleRad()));
            super.entityBody.setTransform(super.entityBody.getPosition(), goblinViewPoint.angleRad());
            // Задать ему скорость
            super.entityBody.setLinearVelocity(
                    new Vector2((this.tempX - super.getX()) * this.speed, (this.tempY - super.getY()) * this.speed));
        }
        this.setPosition(this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2);
    }

    protected static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    public void receiveDamage(double damage) {
        if (this.isAlive) {
            this.health -= damage;
            this.attackedFilterTimer = 0;
            this.setColor(1f, 0f, 0f, 1.0f);
            if (this.health <= 0) {
                this.isAlive = false;
                // dstr.addToDestroyList();
                this.haveToDropped = true;
                // this.dropItem(new PotionMP(1), 1, 10d);
                // this.box2DBody.setTransform(new Vector2(1000, 1000), 0);
                // this.dispose();
            }
        }

    }

    public void drawBar(SpriteBatch sb) {
        float tempHPwidth = (((float) this.health / (float) this.healthMax) * (float) this.BarWidth);
        sb.draw(this.HPTexture, this.getX() - (this.BoundsWidth / 2),
                this.getY() + ((this.BoundsHeight + 5 / DreamWalker.PPM) / 2) - 1 / DreamWalker.PPM, tempHPwidth,
                this.BarHeight - 2 / DreamWalker.PPM);
        sb.draw(this.HPBarTexture, this.getX() - (this.BoundsWidth / 2) - 5 / DreamWalker.PPM,
                this.getY() + (this.BoundsHeight / 2), this.BarWidth + 10 / DreamWalker.PPM, this.BarHeight);

    }

    public void update(float deltaTime, Player player) {
        if (haveToDropped) {
            this.dropItem(new PotionHP(1), 1, 50d);
            this.dropItem(new PotionMP(1), 1, 50d);
            haveToDropped = false;
        }
        if (this.isAlive()) {
            if (this.attackedFilterTimer < this.attackedFilterTimerMax) {
                this.attackedFilterTimer++;
                if (this.attackedFilterTimer == this.attackedFilterTimerMax) {
                    this.attackedFilterTimer = this.attackedFilterTimerMax;
                    this.setColor(1f, 1f, 1f, 1.0f);

                }
            }
            this.idle(player);
            this.attack(player);
        }
        this.setRegion(this.animationController.getFrame(deltaTime));
        this.attackArea.setTransform(this.entityBody.getPosition(), this.entityBody.getAngle());
    }

    private Pixmap createProceduralPixmap(int width, int height, int r, int g, int b) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        pixmap.setColor(r, g, b, 1);
        pixmap.fillRectangle(0, 0, width, height);

        return pixmap;
    }

    public void setBoundsCustom(float width, float height) {
        this.BoundsWidth = width / DreamWalker.PPM;
        this.BoundsHeight = height / DreamWalker.PPM;
        this.BarWidth = this.BoundsWidth;

        this.setBounds(0, 0, this.BoundsWidth, this.BoundsHeight);

    }

    public void setPlayerInArea(boolean playerInArea) {
        this.playerInArea = playerInArea;
    }

    public boolean isPlayerInArea() {
        return this.playerInArea;
    }

    public void dropItem(Item item, int count, double chance) {
        double trigerChance = rnd.nextDouble() * 100;
        if (chance >= 100d) {
            chance = 100d;
        }
        if (chance <= 0) {
            chance = 0d;
        }
        if (trigerChance < chance) {
            // this.haveToDropped = true;
            ItemInWorld drop = new ItemInWorld(this.getX(), this.getY(), item, this.location);
        }

    }

    public void attack(Player player) {
        if (this.isPlayerInArea()) {
            this.soundController.playAttackSound(0.27f);
            this.setIsAttacking(true);
            if (attackSpeedCounter < attackSpeedMax) {
                attackSpeedCounter++;
            } else {
                attackSpeedCounter = 0;
                player.receiveDamage(this.damage);
            }
        } else {
            this.setIsAttacking(false);
        }
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public void dispose() {
        this.getTexture().dispose();
        // this.box2DBody.set
        // this.attackArea.setUserData(null);
        // this.attackArea = null;
        // this.world.destroyBody(this.attackArea);
        // this.box2DBody.setUserData(null);
        // this.box2DBody = null;
        // this.world.destroyBody(this.box2DBody);
    }
}