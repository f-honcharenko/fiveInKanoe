package com.dreamwalker.game.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dreamwalker.game.player.Player;
import com.dreamwalker.game.tools.Destroyer;
import com.badlogic.gdx.graphics.Pixmap;
import com.dreamwalker.game.DreamWalker;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Enemy extends Sprite implements Disposable {
    // Физический мир, в котором находится враг
    private World world;
    // Физическое "тело" врага
    private Body box2DBody;
    private Body attackArea;

    protected double health;
    protected double healthMax;
    protected double damage;
    protected double armor;
    protected float speed;
    private float attackSpeedCoefficient;

    private float BarWidth;
    private float BarHeight;

    private float BoundsWidth;
    private float BoundsHeight;

    private boolean isAttacking;
    private boolean isDamageDealt;
    private boolean isAlive;
    private Player player;

    private double viewAngle;
    private Body enemysBody;
    private Texture HPTexture;
    protected Animations enemysAnimations;

    private Destroyer dstr;

    /**
     * Конструктор
     *
     *
     * @param x - стартовая позиция врага по х
     * @param y - стартовая позиция врага по у
     */
    public Enemy(Player player, float x, float y) {
        this.defineEnemy(player, x, y);

        // Изменяемые параметры
        this.speed = 1.5f;
        this.isAlive = true;
        this.isAttacking = false;
        this.isDamageDealt = false;
        this.health = this.healthMax = 100;
        this.BarWidth = this.BoundsWidth;
        this.BarHeight = 5 / DreamWalker.PPM;
        // this.BoundsWidth = 54 / DreamWalker.PPM;
        this.BoundsWidth = 54 / DreamWalker.PPM;
        this.BoundsHeight = 54 / DreamWalker.PPM;
    }

    /**
     * Конструктор
     * 
     *
     * @param enemySpawnPoint - стартовая позиция игрока
     */
    public Enemy(Player player, Vector2 enemySpawnPoint) {
        this(player, enemySpawnPoint.x, enemySpawnPoint.y);
    }

    abstract public void meleeAttack();

    abstract public void idle();

    public abstract void render(SpriteBatch batch);

    private void defineEnemy(Player player, float x, float y) {
        this.player = player;
        this.world = player.getWorld();

        // Задача физических свойств для "тела" врага
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Создаем физическое "тело" врага в игровом мире на основе свойств
        this.box2DBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        // Физические границы врага
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / DreamWalker.PPM);

        fixtureDef.shape = shape;
        this.box2DBody.createFixture(fixtureDef);

        this.box2DBody.getFixtureList().get(0).setUserData(this);
        // Удаляем фигуру, которая была создана для "тела" врага
        shape.dispose();
        this.HPTexture = new Texture(createProceduralPixmap(1, 1, 1, 0, 0));
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
    };

    /**
     * Метод, отвечающий за наложение эффектов на врага
     */
    public void setBuff() {
        throw new NotImplementedException();
    }

    public void receiveDamage(double damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.isAlive = false;
            dstr.addToDestroyList(this.box2DBody);
            // this.box2DBody.setTransform(new Vector2(1000, 1000), 0);
            // this.dispose();
            // this.game.addToDestroyList();
        }

    }

    public void drawBar(SpriteBatch sb) {
        float tempHPwidth = (((float) this.health / (float) this.healthMax) * (float) this.BarWidth);
        System.out.println("tempHPwidth:" + tempHPwidth);
        sb.draw(this.HPTexture, this.getX() - (this.BoundsWidth / 2), this.getY() + (this.BoundsHeight / 2),
                tempHPwidth, this.BarHeight);

    }

    public Boolean isAlive() {
        return this.isAlive;
    }

    public void setWorld(World world) {
        if (world == null) {
            throw new IllegalArgumentException();
        }
        this.world = world;
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

    public Body getBox2DBody() {
        return this.box2DBody;
    }

    /**
     * @return - позиция врага по х
     */
    public float getX() {
        return this.box2DBody.getPosition().x;
    }

    /**
     * @return - позиция врага по х
     */
    public float getY() {
        return this.box2DBody.getPosition().y;
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

    public void update(float deltaTime) {
        if (this.isAlive()) {
            this.idle();
            this.meleeAttack();
            this.setRegion(this.enemysAnimations.getFrame(deltaTime));
        }
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