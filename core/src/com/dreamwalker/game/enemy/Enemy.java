package com.dreamwalker.game.enemy;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.player.Player;
import com.dreamwalker.game.tools.Destroyer;
import com.badlogic.gdx.graphics.Pixmap;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.items.Item;
import com.dreamwalker.game.items.ItemInWorld;
import com.dreamwalker.game.items.PotionHP;
import com.dreamwalker.game.items.PotionMP;
import com.dreamwalker.game.location.Location;

public abstract class Enemy extends Sprite implements Disposable {
    // Физический мир, в котором находится враг
    private World world;
    private Location location;
    // Физическое "тело" врага
    private Body enemysBody;
    private Body attackArea;

    protected double health;
    protected double healthMax;
    protected double damage;
    protected double armor;
    protected float speed;
    private float attackSpeedCoefficient;

    private int attackedFilterTimerMax;
    private int attackedFilterTimer;
    private Boolean attackedFilterFlag;

    private float BarWidth;
    private float BarHeight;

    private float BoundsWidth;
    private float BoundsHeight;

    private boolean isAttacking;
    private boolean isDamageDealt;
    private boolean isAlive;

    private boolean playerInArea;

    private double viewAngle;
    private Texture HPTexture;
    private Texture HPBarTexture;
    protected Animations enemysAnimations;

    protected int respawnTime;
    protected int respawnCounter;

    private Destroyer dstr;
    private boolean roomChanged;

    private Vector2 standSpawnPoint;

    private Random rnd;

    public boolean haveToDropped;

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
        this.standSpawnPoint = new Vector2(x, y);
        this.defineEnemy(this.standSpawnPoint);

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

        this.playerInArea = false;
        this.attackSpeedCoefficient = 1.5f;
        this.roomChanged = false;
        this.respawnCounter = 0;
    }

    /**
     * Конструктор
     * 
     *
     * @param enemySpawnPoint - стартовая позиция игрока
     */
    public Enemy(Location location, Vector2 enemySpawnPoint) {
        this(location, enemySpawnPoint.x, enemySpawnPoint.y);
    }

    abstract public void attack(Player player);

    abstract public void idle(Player player);

    public void respawn() {
        if (!this.isAlive) {
            this.respawnCounter++;
            this.enemysBody.getFixtureList().get(0).setSensor(true);
            if (this.respawnCounter == this.respawnTime) {
                this.enemysBody.getFixtureList().get(0).setSensor(false);
                this.enemysBody.setTransform(this.standSpawnPoint.x, this.standSpawnPoint.y, (float) this.viewAngle);
                this.health = this.healthMax;
                this.isAlive = true;
                this.respawnCounter = 0;
            }
        }
    }

    public abstract void render(SpriteBatch batch);

    private void defineEnemy(Vector2 spawnPoint) {
        // Задача физических свойств для "тела" врага
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnPoint);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Создаем физическое "тело" врага в игровом мире на основе свойств
        this.enemysBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        // Физические границы врага
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / DreamWalker.PPM);

        fixtureDef.shape = shape;
        this.enemysBody.createFixture(fixtureDef);

        this.enemysBody.getFixtureList().get(0).setUserData(this);
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
        this.dstr = new Destroyer(this.world);

        this.rnd = new Random();
    };

    /**
     * Метод, отвечающий за наложение эффектов на врага
     */
    public void setBuff() {
        throw new NotImplementedException();
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

    public Boolean isAlive() {
        return this.isAlive;
    }

    public void setWorld(World world) {
        if (world == null) {
            throw new IllegalArgumentException();
        }
        if (this.world != world) {
            this.world.destroyBody(this.enemysBody);
            this.world.destroyBody(this.attackArea);
            this.world = world;
            this.roomChanged = true;
        }

    }

    public World getWorld() {
        return this.world;
    }

    public double getCurrentHealth() {
        return this.health;
    }

    public double getMaxHealth() {
        return this.healthMax;
    }

    public double getDamage() {
        return this.damage;
    }

    public Body getAttackArea() {
        return this.attackArea;
    }

    public double getArmor() {
        return this.armor;
    }

    public float getSpeed() {
        return this.speed;
    }

    public Body getEnemysBody() {
        return this.enemysBody;
    }

    /**
     * @return - позиция врага по х
     */
    public float getX() {
        return this.enemysBody.getPosition().x;
    }

    /**
     * @return - позиция врага по х
     */
    public float getY() {
        return this.enemysBody.getPosition().y;
    }

    public float getAttackSpeedCoefficient() {
        return this.attackSpeedCoefficient;
    }

    public void setDamageDealt(boolean damageDealt) {
        this.isDamageDealt = damageDealt;
    }

    public double getViewAngle() {
        return this.viewAngle;
    }

    public void setViewAngle(double viewAngle) {
        this.viewAngle = viewAngle;
        // this.getBox2DBody().setTransform(this.getBox2DBody().getPosition(), (float)
        // viewAngle);

    }

    public boolean isAttacking() {
        return this.isAttacking;
    }

    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void update(float deltaTime, Player player) {
        if (haveToDropped) {
            // ItemInWorld drop = new ItemInWorld(this.getX(), this.getY(), item,
            // this.location);
            this.dropItem(new PotionHP(1), 1, 100d);
            haveToDropped = false;
        }
        if (roomChanged) {
            this.defineEnemy(this.standSpawnPoint);
            this.roomChanged = false;
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
        this.setRegion(this.enemysAnimations.getFrame(deltaTime));
        this.attackArea.setTransform(this.enemysBody.getPosition(), this.enemysBody.getAngle());
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

    };

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