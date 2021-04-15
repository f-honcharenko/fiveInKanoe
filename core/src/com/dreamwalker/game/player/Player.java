package com.dreamwalker.game.player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import com.badlogic.gdx.utils.Array;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class Player extends Sprite {
    private TextureAtlas atlas;

    private enum State {STANDING_NORTH, STANDING_EAST, STANDING_SOUTH, STANDING_WEST,
                        RUNNING_NORTH, RUNNING_EAST, RUNNING_SOUTH, RUNNING_WEST,
                        MELEE_ATTACKING};

    private State currentState;
    private State prevState;
    private State prevDirection;

    private TextureRegion playerStandSouth;
    private TextureRegion playerStandWest;
    private TextureRegion playerStandNorth;
    private TextureRegion playerStandEast;
    private Animation playerRunSouth;
    private Animation playerRunWest;
    private Animation playerRunNorth;
    private Animation playerRunEast;

    private float stateTimer;

    // Физический мир, в котором находится игрок
    private World world;
    // Физическое "тело" игрока
    private Body playersBody;
    private Body attackArea;

    private double health;
    private double mana;
    private double damage;
    private double armor;
    private float speed;


    /**
     * Конструктор
     * 
     * @param world - физический мир, в котором будет находится игрок
     * @param x     - стартовая позиция игрока по х
     * @param y     - стартовая позиция игрока по у
     */
    public Player(World world, float x, float y) {
        this.world = world;
        // Текстура игрока для отрисовки
        this.atlas = new TextureAtlas("player.atlas");
        this.setRegion(this.atlas.findRegion("player"));

        this.playerStandEast = new TextureRegion(this.getTexture(), 0, 11 * 64, 64, 64);
        this.playerStandSouth = new TextureRegion(this.getTexture(), 0, 10 * 64, 64, 64);
        this.playerStandWest = new TextureRegion(this.getTexture(), 0, 9 * 64, 64, 64);
        this.playerStandNorth = new TextureRegion(this.getTexture(), 0, 8 * 64, 64, 64);

        this.currentState = State.STANDING_SOUTH;
        this.prevState = State.STANDING_SOUTH;
        this.prevDirection = State.STANDING_SOUTH;
        this.stateTimer = 0;

        this.playerRunNorth = initAnimation(9);
        this.playerRunWest = initAnimation(10);
        this.playerRunSouth = initAnimation(11);
        this.playerRunEast = initAnimation(12);


        this.definePlayer(x, y);
        this.speed = 80.5f;
        this.health = 100;
        this.mana = 0;

        this.setBounds(0, 10 * 64, 54, 54);
        this.setRegion(this.playerStandSouth);
    }

    private void definePlayer(float x, float y){
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

        float scalar = shape.getRadius() * 3;
        this.attackArea = this.world.createBody(bodyDef);
        FixtureDef attackFixture = new FixtureDef();
        PolygonShape dmgSectorShape = new PolygonShape();

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
        this.attackArea.createFixture(attackFixture);
        dmgSectorShape.dispose();
    }

    private Animation initAnimation(int rowOfAtlas){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 1; i < 8; i++){
            frames.add(new TextureRegion(this.getTexture(), i * 64, (rowOfAtlas - 1) * 64, 64, 64));
        }
        Animation newAnim = new Animation(0.1f, frames);
        frames.clear();
        return newAnim;
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


    public void update(float deltaTime){
        this.setPosition(this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2);
        this.mana += 0.01;
        this.setRegion(this.getFrame(deltaTime));
    }

    private TextureRegion getFrame(float deltaTime){
        this.currentState = getState();
        TextureRegion region;
        switch (this.currentState){
            case RUNNING_NORTH:
                region = (TextureRegion)this.playerRunNorth.getKeyFrame(this.stateTimer, true);
                break;
            case RUNNING_EAST:
                region = (TextureRegion)this.playerRunEast.getKeyFrame(this.stateTimer, true);
                break;
            case RUNNING_SOUTH:
                region = (TextureRegion)this.playerRunSouth.getKeyFrame(this.stateTimer, true);
                break;
            case RUNNING_WEST:
                region = (TextureRegion)this.playerRunWest.getKeyFrame(this.stateTimer, true);
                break;
            case STANDING_NORTH:
                region = this.playerStandNorth;
                break;
            case STANDING_EAST:
                region = this.playerStandEast;
                break;
            case STANDING_WEST:
                region = this.playerStandWest;
                break;
            case STANDING_SOUTH:
            default:
                region = this.playerStandSouth;
                break;
        }
        this.stateTimer = (this.currentState == this.prevState) ? this.stateTimer + deltaTime : 0;
        this.prevState = this.currentState;
        return region;
    }

    private State getState(){
        State currentState;
        if(this.playersBody.getLinearVelocity().x > 0){
            currentState = State.RUNNING_EAST;
            this.prevDirection = State.STANDING_EAST;
        }
        else if(this.playersBody.getLinearVelocity().x < 0){
            currentState = State.RUNNING_WEST;
            this.prevDirection = State.STANDING_WEST;
        }
        else if(this.playersBody.getLinearVelocity().y > 0){
            currentState = State.RUNNING_NORTH;
            this.prevDirection = State.STANDING_NORTH;
        }
        else if(this.playersBody.getLinearVelocity().y < 0){
            currentState = State.RUNNING_SOUTH;
            this.prevDirection = State.STANDING_SOUTH;
        }
        else{
            if(this.prevDirection == State.STANDING_NORTH){
                currentState = State.STANDING_NORTH;
            }
            else if(this.prevDirection == State.STANDING_EAST){
                currentState = State.STANDING_EAST;
            }
            else if(this.prevDirection == State.STANDING_SOUTH){
                currentState = State.STANDING_SOUTH;
            }
            else{
                currentState = State.STANDING_WEST;
            }
        }
        return currentState;
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
        this.attackArea.setTransform(this.playersBody.getPosition(), angle);

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

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){

        }

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

    public double getMana() {
        return mana;
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
