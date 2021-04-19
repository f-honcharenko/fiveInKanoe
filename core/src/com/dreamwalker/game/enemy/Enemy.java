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


    /**
     * Конструктор
     *
     *
     * @param x     - стартовая позиция врага по х
     * @param y     - стартовая позиция врага по у
     */
    public Enemy(Player player, float x, float y) {
        // Текстура врага для отрисовки
        // !В будущем заменить на атлас
        super(new Texture("badlogic.jpg"));
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
        this.box2DBody.getFixtureList().get(0).setUserData(this);
        // Удаляем фигуру, которая была создана для "тела" врага
        shape.dispose();

        this.setBounds(0, 0, 30, 30);
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

    abstract public void move();

    abstract public void meleeAttack();

    abstract public void idle();

    /**
     * Метод, отвечающий за наложение эффектов на врага
     */
    public void setBuff() {
        throw new NotImplementedException();
    }

    public void receiveDamage(double damage){
        this.health -= damage;
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