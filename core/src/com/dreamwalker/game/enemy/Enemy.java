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
import com.badlogic.gdx.physics.box2d.World;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Enemy extends Sprite {
    // Физический мир, в котором находится враг
    private World world;
    // Физическое "тело" врага
    private Body box2DBody;

    private double health;
    private double damage;
    private double armor;
    private float speed;

    protected int test;

    private Texture texture;

    /**
     * Конструктор
     * 
     * @param world - физический мир, в котором будет находится враг
     * @param x     - стартовая позиция врага по х
     * @param y     - стартовая позиция врага по у
     */
    public Enemy(World world, float x, float y) {
        // Текстура врага для отрисовки
        // !В будущем заменить на атлас
        this.texture = new Texture("badlogic.jpg");

        this.world = world;
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
    }

    /**
     * Конструктор
     * 
     * @param world           - физический мир, в котором будет находится враг
     * @param enemySpawnPoint - стартовая позиция игрока
     */
    public Enemy(World world, Vector2 enemySpawnPoint) {
        this(world, enemySpawnPoint.x, enemySpawnPoint.y);
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
     * @return - позиция врага по у
 

}
