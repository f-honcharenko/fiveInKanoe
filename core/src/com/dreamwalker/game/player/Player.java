package com.dreamwalker.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Player extends Sprite {
    // Физический мир, в котором находится игрок
    private World world;
    // Физическое "тело" игрока
    private Body playersBody;

    private Body damageArea;

    private double health;
    private double damage;
    private double armor;
    private float speed;

    private Texture texture;

    /**
     * Конструктор
     * 
     * @param world - физический мир, в котором будет находится игрок
     * @param x     - стартовая позиция игрока по х
     * @param y     - стартовая позиция игрока по у
     */
    public Player(World world, float x, float y) {
        this.speed = 70.5f;
        // Текстура игрока для отрисовки
        // !В будущем заменить на атлас
        this.texture = new Texture("badlogic.jpg");

        this.world = world;
        // Задача физических свойств для "тела" игрока
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Создаем физическое "тело" игрока в игровом мире на основе свойств
        this.playersBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();

        // Физические границы игрока
        CircleShape shape = new CircleShape();
        shape.setRadius(15);

        fixtureDef.shape = shape;
        this.playersBody.createFixture(fixtureDef);
        // Удаляем фигуру, которая была создана для "тела" игрока
        shape.dispose();

        this.damageArea = this.world.createBody(bodyDef);
        FixtureDef attackFixture = new FixtureDef();
        PolygonShape dmgSectorShape = new PolygonShape();
        int scalar = 35;
        Vector2[] vertices = {
                new Vector2(0,0),
                new Vector2(scalar * (float)(Math.cos(5 * Math.PI / 3)), scalar * (float)(Math.sin(5 * Math.PI / 3))),
                new Vector2(scalar * (float)(Math.cos(7 * Math.PI / 4)), scalar * (float)(Math.sin(7 * Math.PI / 4))),
                new Vector2(scalar * (float)(Math.cos(11 * Math.PI / 6)), scalar * (float)(Math.sin(11 * Math.PI / 6))),
                new Vector2(scalar * (float)(Math.cos(0)), scalar * (float)(Math.sin(0))), //-----Середина------
                new Vector2(scalar * (float)(Math.cos(Math.PI / 6)), scalar * (float)(Math.sin(Math.PI / 6))),
                new Vector2(scalar * (float)(Math.cos(Math.PI / 4)), scalar * (float)(Math.sin(Math.PI / 4))),
                new Vector2(scalar * (float)(Math.cos(Math.PI / 3)), scalar * (float)(Math.sin(Math.PI / 3)))
        };
        dmgSectorShape.set(vertices);
        attackFixture.shape = dmgSectorShape;
        attackFixture.isSensor = true;
        this.damageArea.createFixture(attackFixture);
        dmgSectorShape.dispose();

    }

    /**
     * Конструктор
     * 
     * @param world      - физический мир, в котором будет находится игрок
     * @param spawnPoint - стартовая позиция игрока
     */
    public Player(World world, Vector2 spawnPoint) {
        this(world, spawnPoint.x, spawnPoint.y);
    }

    /**
     * Общий метод, отвечающий за упрваление персонажем
     */

    public void playerControl(Vector2 mousePosition){
        this.move(mousePosition);
        this.meleeAttack();
    }

    /**
     * Mетод, отвечающий за отрисовку
     * 
     * @param coords      - глоабальные координаты
     * @param spriteBatch - кисть мира
     */
    public void draw(Vector3 coords, SpriteBatch spriteBatch) {
        spriteBatch.draw(this.texture, coords.x - 15f, coords.y - 15f, 30f, 30f);

    }

    /**
     * Метод, отвечающий за передвижение персонажа
     * 
     * @param mousePosition - координаты мыши в пространстве игрового мира
     */
    public void move(Vector2 mousePosition) {
        // Вычитаем позицию игрока из позиции мыши
        Vector2 playersViewPoint = mousePosition.sub(this.playersBody.getPosition());
        float angle = playersViewPoint.angleRad();
        // Позиция игрока остается прежней, в то время, как поворот меняется в зависимости от положения мыши
        this.playersBody.setTransform(this.playersBody.getPosition(), angle);
        this.damageArea.setTransform(this.playersBody.getPosition(), angle);

        // Обработка нажатий клавиш WASD
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            //this.box2DBody.applyLinearImpulse(new Vector2(0, this.speed), this.box2DBody.getWorldCenter(), true);
            this.playersBody.setLinearVelocity(new Vector2(0, this.speed));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //this.box2DBody.applyLinearImpulse(new Vector2(-this.speed, 0), this.box2DBody.getWorldCenter(), true);
            this.playersBody.setLinearVelocity(new Vector2(-this.speed, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //this.box2DBody.applyLinearImpulse(new Vector2(this.speed, 0), this.box2DBody.getWorldCenter(), true);
            this.playersBody.setLinearVelocity(new Vector2(this.speed, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            //this.box2DBody.applyLinearImpulse(new Vector2(0, -this.speed), this.box2DBody.getWorldCenter(), true);
            this.playersBody.setLinearVelocity(new Vector2(0, -this.speed));
        }

        // Обработка сочитаний WD WA SD SA
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.playersBody.setLinearVelocity(new Vector2(this.speed, this.speed));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.playersBody.setLinearVelocity(new Vector2(-this.speed, this.speed));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.playersBody.setLinearVelocity(new Vector2(this.speed, -this.speed));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.playersBody.setLinearVelocity(new Vector2(-this.speed, -this.speed));
        }

        // Проверка, нажата ли одна из клавиш WASD
        boolean isMoving = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D);

        // Проверка конфликтующих сочитаний WS AD
        boolean conflictX = Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D);
        boolean conflictY = Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S);

        // Остановить персонажа, если ни одна из клавиш не нажата, или нажаты
        // конфликтующие сочетания
        if (!isMoving || conflictX || conflictY) {
            this.playersBody.setLinearVelocity(0, 0);
        }
    }

    /**
     * Метод, отвечающий за ближнюю атаку игрока
     */
    public void meleeAttack() {
        /*
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            if () {
                this.playersBody.getFixtureList().get(0).testPoint();
            }
        }*/
    }

    /**
     * Метод, отвечающий за наложение эффектов на игрока
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

    public Body getPlayersBody() {
        return this.playersBody;
    }

    /**
     *
     * @return - позиция игрока по х
     */
    public float getX() {
        return this.playersBody.getPosition().x;
    }

    /**
     *
     * @return - позиция игрока по у
     */
    public float getY() {
        return this.playersBody.getPosition().y;
    }

}
