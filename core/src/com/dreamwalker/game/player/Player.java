package com.dreamwalker.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Player extends Sprite {
    // Физический мир, в котором находится игрок
    private World world;
    // Физическое "тело" игрока
    private Body box2DBody;

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
        this.box2DBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();

        // Физические границы игрока
        CircleShape shape = new CircleShape();
        shape.setRadius(15);

        fixtureDef.shape = shape;
        this.box2DBody.createFixture(fixtureDef);
        // Удаляем фигуру, которая была создана для "тела" игрока
        shape.dispose();
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
    public void playerControl() {
        throw new NotImplementedException();
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
        Vector2 playersViewPoint = mousePosition.sub(this.box2DBody.getPosition());
        // Позиция игрока остается прежней, в то время, как поворот меняется в
        // зависимости от положения мыши
        this.box2DBody.setTransform(this.box2DBody.getPosition(), playersViewPoint.angleRad());

        // Обработка нажатий клавиш WASD
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            // this.box2DBody.applyLinearImpulse(new Vector2(0, this.speed),
            // this.box2DBody.getWorldCenter(), true);
            this.box2DBody.setLinearVelocity(new Vector2(0, this.speed));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            // this.box2DBody.applyLinearImpulse(new Vector2(-this.speed, 0),
            // this.box2DBody.getWorldCenter(), true);
            this.box2DBody.setLinearVelocity(new Vector2(-this.speed, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            // this.box2DBody.applyLinearImpulse(new Vector2(this.speed, 0),
            // this.box2DBody.getWorldCenter(), true);
            this.box2DBody.setLinearVelocity(new Vector2(this.speed, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            // this.box2DBody.applyLinearImpulse(new Vector2(0, -this.speed),
            // this.box2DBody.getWorldCenter(), true);
            this.box2DBody.setLinearVelocity(new Vector2(0, -this.speed));
        }

        // Обработка сочитаний WD WA SD SA
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.box2DBody.setLinearVelocity(new Vector2(this.speed, this.speed));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.box2DBody.setLinearVelocity(new Vector2(-this.speed, this.speed));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.box2DBody.setLinearVelocity(new Vector2(this.speed, -this.speed));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.box2DBody.setLinearVelocity(new Vector2(-this.speed, -this.speed));
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
            this.box2DBody.setLinearVelocity(0, 0);
        }
    }

    /**
     * Метод, отвечающий за ближнюю атаку игрока
     */
    public void meleeAttack() {
        throw new NotImplementedException();
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

    public Body getBox2DBody() {
        return this.box2DBody;
    }

    /**
     *
     * @return - позиция игрока по х
     */
    public float getX() {
        return this.box2DBody.getPosition().x;
    }

    /**
     *
     * @return - позиция игрока по у
     */
    public float getY() {
        return this.box2DBody.getPosition().y;
    }

}
