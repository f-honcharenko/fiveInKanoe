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
import com.dreamwalker.game.player.Player;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Enemy extends Sprite {
    // Физический мир, в котором находится враг
    private World world;
    // Физическое "тело" врага
    private Body box2DBody;
    private Body attackArea;

    protected double health;
    protected double damage;
    protected double armor;
    protected float speed;

    private Player player;

    private Texture texture;

    /**
     * Конструктор
     * 
     * @param world - физический мир, в котором будет находится враг
     * @param x     - стартовая позиция врага по х
     * @param y     - стартовая позиция врага по у
     */
    public Enemy(Player player, float x, float y) {
        // Текстура врага для отрисовки
        // !В будущем заменить на атлас
        this.texture = new Texture("badlogic.jpg");
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
        shape.setRadius(15);

        fixtureDef.shape = shape;
        this.box2DBody.createFixture(fixtureDef);
        // Удаляем фигуру, которая была создана для "тела" врага
        shape.dispose();

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
        dmgSectorShape.dispose();
    }

    /**
     * Конструктор
     * 
     * @param world           - физический мир, в котором будет находится враг
     * @param enemySpawnPoint - стартовая позиция игрока
     */
    public Enemy(Player player, Vector2 enemySpawnPoint) {
        this(player, enemySpawnPoint.x, enemySpawnPoint.y);
    }

    /**
     * Mетод, отвечающий за отрисовку
     * 
     * @param coords      - глоабальные координаты
     * @param spriteBatch - кисть мира
     */
    public void draw(Vector3 coords, SpriteBatch spriteBatch) {
        // System.out.println("Enemy posotion:" + this.getX() + "|" + this.getY());
        spriteBatch.draw(this.texture, coords.x - 15f, coords.y - 15f, 30f, 30f);
    }

    abstract public void move();

    abstract public void meleeAttack();

    abstract public void idle();

    /**
     * Метод, отвечающий за наложение эффектов на врага
     */
    public void setBuff() {
        throw new NotImplementedException();
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

    public double getHealth() {
        return this.health;
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
     *
     * @return - позиция врага по х
     */
    public float getX() {
        return this.box2DBody.getPosition().x;
    }

    /**
     *
     * @return - позиция врага по х
     */
    public float getY() {
        return this.box2DBody.getPosition().y;
    }

}