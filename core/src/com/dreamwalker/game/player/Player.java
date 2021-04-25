package com.dreamwalker.game.player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.dreamwalker.game.enemy.Enemy;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.ArrayList;


public class Player extends Sprite {
    private TextureAtlas atlas;

    private enum State {
        STANDING_NORTH, STANDING_EAST, STANDING_SOUTH, STANDING_WEST,
        RUNNING_NORTH, RUNNING_EAST, RUNNING_SOUTH, RUNNING_WEST,
        MELEE_ATTACKING_NORTH, MELEE_ATTACKING_EAST, MELEE_ATTACKING_SOUTH,
        MELEE_ATTACKING_WEST
    };

    private State currentState;
    private State prevState;

    private final TextureRegion playerStandSouth;
    private final TextureRegion playerStandWest;
    private final TextureRegion playerStandNorth;
    private final TextureRegion playerStandEast;
    private final Animation playerRunSouth;
    private final Animation playerRunWest;
    private final Animation playerRunNorth;
    private final Animation playerRunEast;

    private final Animation playerMeleeSouth;
    private final Animation playerMeleeWest;
    private final Animation playerMeleeNorth;
    private final Animation playerMeleeEast;

    private float stateTimer;

    // Физический мир, в котором находится игрок
    private World world;
    // Физическое "тело" игрока
    private Body playersBody;
    private Body attackArea;

    private double health;
    private double healthMax;
    private double mana;
    private double manaMax;
    private double damage;
    private double armor;
    private float speed;
    private float attackSpeedCoefficient;

    private double viewAngle;

    private boolean isAlive = true;

    private ArrayList<Enemy> enemiesInRange;
    private boolean enemyInArea;
    private boolean isAttacking;

    /**
     * Конструктор
     * 
     * @param world - физический мир, в котором будет находится игрок
     * @param x     - стартовая позиция игрока по х
     * @param y     - стартовая позиция игрока по у
     */
    public Player(World world, float x, float y) {
        this.enemiesInRange = new ArrayList<>();
        this.enemyInArea = false;
        this.isAttacking = false;

        this.world = world;
        // Текстура игрока для отрисовки
        this.atlas = new TextureAtlas("player.atlas");
        this.setRegion(this.atlas.findRegion("player"));

        this.playerStandEast = new TextureRegion(this.getTexture(), 0, 23 * 64, 64, 64);
        this.playerStandSouth = new TextureRegion(this.getTexture(), 0, 21 * 64, 64, 64);
        this.playerStandWest = new TextureRegion(this.getTexture(), 0, 19 * 64, 64, 64);
        this.playerStandNorth = new TextureRegion(this.getTexture(), 0, 17 * 64, 64, 64);

        this.currentState = State.STANDING_SOUTH;
        this.prevState = State.STANDING_SOUTH;
        this.stateTimer = 0;

        this.playerRunNorth = initAnimation(18, 8);
        this.playerRunWest = initAnimation(20, 8);
        this.playerRunSouth = initAnimation(22, 8);
        this.playerRunEast = initAnimation(24, 8);

        this.playerMeleeNorth = initAnimation(26, 6);
        this.playerMeleeWest = initAnimation(28, 6);
        this.playerMeleeSouth = initAnimation(30, 6);
        this.playerMeleeEast = initAnimation(32, 6);


        this.definePlayer(x, y);
        this.damage = 15;
        this.speed = 80.5f;
        this.health = 100;
        this.mana = 0;
        this.armor = 4;
        this.healthMax = 100;
        this.manaMax = 100;
        this.attackSpeedCoefficient = 1.5f;

        this.setBounds(0, 0, 140, 140); //54
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
        this.playersBody.getFixtureList().get(0).setUserData("Player");

        // Удаляем фигуру, которая была создана для "тела" игрока
        shape.dispose();

        float scalar = shape.getRadius() * 3;
        this.attackArea = this.world.createBody(bodyDef);
        FixtureDef attackFixture = new FixtureDef();
        PolygonShape dmgSectorShape = new PolygonShape();

        Vector2[] vertices = { new Vector2(0, 0),
                new Vector2(scalar * (float) (Math.cos(5 * Math.PI / 3)), scalar * (float) (Math.sin(5 * Math.PI / 3))),
                new Vector2(scalar * (float) (Math.cos(7 * Math.PI / 4)), scalar * (float) (Math.sin(7 * Math.PI / 4))),
                new Vector2(scalar * (float) (Math.cos(11 * Math.PI / 6)),scalar * (float) (Math.sin(11 * Math.PI / 6))),
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
    }

    private Animation initAnimation(int rowOfAtlas, int spritesCount){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 1; i < spritesCount; i++){
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
    public void playerControl(Vector2 mousePosition) {
        this.meleeAttack();
        this.move(mousePosition);
    }

    public void update(float deltaTime) {
        this.setPosition(this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2);
        if (this.getCurrentHealth() <= 0) {
            this.health = 0;
            this.isAlive = false;
        }
        this.regeneration();
        this.setRegion(this.getFrame(deltaTime));
    }

    /**
     * метод, отвечающий регенерацию показателей персонажа
     */
    public void regeneration() {
        if (isAlive) {
            if (this.mana < this.manaMax) {
                this.mana += 0.01;
            }
            if (this.health < this.healthMax) {
                this.health += 0.1;
            }
        }
        this.mana += 0.01;
    }

    /**
     * Метод, отвечающий за ближнюю атаку игрока
     */
    private void meleeAttack() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            this.isAttacking = true;
            if(this.enemyInArea){
                for(Enemy enemy : this.enemiesInRange){
                    System.out.println(enemy.getHealth());
                    enemy.receiveDamage(this.damage);
                }
            }
        }
    }

    private TextureRegion getFrame(float deltaTime){
        this.currentState = getState();
        TextureRegion region;
        switch (this.currentState){
            case RUNNING_NORTH:
                region = (TextureRegion)this.playerRunNorth.getKeyFrame(this.speed + this.stateTimer, true);
                break;
            case RUNNING_EAST:
                region = (TextureRegion)this.playerRunEast.getKeyFrame(this.speed + this.stateTimer, true);
                break;
            case RUNNING_SOUTH:
                region = (TextureRegion)this.playerRunSouth.getKeyFrame(this.speed + this.stateTimer, true);
                break;
            case RUNNING_WEST:
                region = (TextureRegion)this.playerRunWest.getKeyFrame(this.speed + this.stateTimer, true);
                break;
            case MELEE_ATTACKING_NORTH:
                region = (TextureRegion)this.playerMeleeNorth.getKeyFrame(
                        this.attackSpeedCoefficient * this.stateTimer,
                        true
                );
                if(this.playerMeleeNorth.isAnimationFinished(this.attackSpeedCoefficient * this.stateTimer)){
                    this.isAttacking = false;
                }
                break;
            case MELEE_ATTACKING_EAST:
                region = (TextureRegion)this.playerMeleeEast.getKeyFrame(
                        this.attackSpeedCoefficient * this.stateTimer,
                        true
                );
                if(this.playerMeleeEast.isAnimationFinished(this.attackSpeedCoefficient * this.stateTimer)){
                    this.isAttacking = false;
                }
                break;
            case MELEE_ATTACKING_SOUTH:
                region = (TextureRegion)this.playerMeleeSouth.getKeyFrame(
                        this.attackSpeedCoefficient * this.stateTimer,
                        true
                );
                if(this.playerMeleeSouth.isAnimationFinished(this.attackSpeedCoefficient * this.stateTimer)){
                    this.isAttacking = false;
                }
                break;
            case MELEE_ATTACKING_WEST:
                region = (TextureRegion)this.playerMeleeWest.getKeyFrame(
                        this.attackSpeedCoefficient * this.stateTimer,
                        true
                );
                if(this.playerMeleeWest.isAnimationFinished(this.attackSpeedCoefficient * this.stateTimer)){
                    this.isAttacking = false;
                }
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
        State currentState = null;
        float velocityX = this.playersBody.getLinearVelocity().x;
        float velocityY = this.playersBody.getLinearVelocity().y;
        boolean isMoving = velocityX != 0 || velocityY != 0;
        if(isMoving && !this.isAttacking){
            if((this.viewAngle >= 315 && this.viewAngle <= 360) || (this.viewAngle >= 0 && this.viewAngle <= 45)){
                currentState = State.RUNNING_EAST;
                if(velocityX > 0){
                    this.playerRunEast.setPlayMode(Animation.PlayMode.NORMAL);
                }
                else{
                    this.playerRunEast.setPlayMode(Animation.PlayMode.REVERSED);
                }

            }
            else if(this.viewAngle > 135 && this.viewAngle < 225){
                currentState = State.RUNNING_WEST;
                if(velocityX > 0){
                    this.playerRunWest.setPlayMode(Animation.PlayMode.REVERSED);
                }
                else{
                    this.playerRunWest.setPlayMode(Animation.PlayMode.NORMAL);
                }
            }
            else if(this.viewAngle > 45 && this.viewAngle < 135){
                currentState = State.RUNNING_NORTH;
                if(velocityY > 0){
                    this.playerRunNorth.setPlayMode(Animation.PlayMode.NORMAL);
                }
                else{
                    this.playerRunNorth.setPlayMode(Animation.PlayMode.REVERSED);
                }
            }
            else if(this.viewAngle >= 225 && this.viewAngle <= 315){
                currentState = State.RUNNING_SOUTH;
                if(velocityX > 0){
                    this.playerRunSouth.setPlayMode(Animation.PlayMode.REVERSED);
                }
                else{
                    this.playerRunSouth.setPlayMode(Animation.PlayMode.NORMAL);
                }
            }
        }
        else{
            if(this.isAttacking){
                if(this.viewAngle > 45 && this.viewAngle < 135){
                    currentState = State.MELEE_ATTACKING_NORTH;
                }
                else if(this.viewAngle > 135 && this.viewAngle < 225){
                    currentState = State.MELEE_ATTACKING_WEST;
                    this.playerMeleeWest.setPlayMode(Animation.PlayMode.REVERSED);
                }
                else if(this.viewAngle >= 225 && this.viewAngle <= 315){
                    currentState = State.MELEE_ATTACKING_SOUTH;
                }
                else{
                    currentState = State.MELEE_ATTACKING_EAST;
                    this.playerMeleeEast.setPlayMode(Animation.PlayMode.REVERSED);
                }
            }
            else{
                if(this.viewAngle > 45 && this.viewAngle < 135){
                    currentState = State.STANDING_NORTH;
                }
                else if(this.viewAngle > 135 && this.viewAngle < 225){
                    currentState = State.STANDING_WEST;
                }
                else if(this.viewAngle >= 225 && this.viewAngle <= 315){
                    currentState = State.STANDING_SOUTH;
                }
                else{
                    currentState = State.STANDING_EAST;
                }
            }
        }
        return currentState;
    }

    /**
     * Метод, отвечающий за передвижение персонажа
     * 
     * @param mousePosition - координаты мыши в пространстве игрового мира
     */
    private void move(Vector2 mousePosition) {
        // Вычитаем позицию игрока из позиции мыши
        Vector2 playersViewPoint = mousePosition.sub(this.playersBody.getPosition());
        float angle = playersViewPoint.angleRad();
        this.viewAngle = angle * 180 / Math.PI;
        if(this.viewAngle < 0){
            this.viewAngle = 180 + Math.abs(this.viewAngle + 180);
        }
        // Позиция игрока остается прежней, в то время, как поворот меняется в зависимости от положения мыши
        this.playersBody.setTransform(this.playersBody.getPosition(), angle);
        this.attackArea.setTransform(this.playersBody.getPosition(), angle);

        // Обработка нажатий клавиш WASD
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            //this.box2DBody.applyLinearImpulse(new Vector2(0, this.speed), this.box2DBody.getWorldCenter(), true);
            this.playersBody.setLinearVelocity(0, this.speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            //this.box2DBody.applyLinearImpulse(new Vector2(0, -this.speed), this.box2DBody.getWorldCenter(), true);
            this.playersBody.setLinearVelocity(0, -this.speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //this.box2DBody.applyLinearImpulse(new Vector2(-this.speed, 0), this.box2DBody.getWorldCenter(), true);
            this.playersBody.setLinearVelocity(-this.speed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //this.box2DBody.applyLinearImpulse(new Vector2(this.speed, 0), this.box2DBody.getWorldCenter(), true);
            this.playersBody.setLinearVelocity(this.speed, 0);
        }

        // Обработка сочитаний WD WA SD SA
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.playersBody.setLinearVelocity(this.speed, this.speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.playersBody.setLinearVelocity(-this.speed, this.speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.playersBody.setLinearVelocity(this.speed, -this.speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.playersBody.setLinearVelocity(-this.speed, -this.speed);
        }


        // Проверка, нажата ли одна из клавиш WASD
        boolean isMoving = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D);

        // Проверка конфликтующих сочитаний WS AD
        boolean conflictX = Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D);
        boolean conflictY = Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S);

        // Остановить персонажа, если ни одна из клавиш не нажата, или нажаты
        // конфликтующие сочетания
        if (!isMoving || conflictX || conflictY || this.isAttacking) {
            this.playersBody.setLinearVelocity(0, 0);
        }
        System.out.println(this.isAttacking);

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

    public void setEnemyInArea(boolean enemyInArea) {
        this.enemyInArea = enemyInArea;
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

    public double getCurrentMana() {
        return this.mana;
    }

    public double getMaxMana() {
        return this.manaMax;
    }

    public double getDamage() {
        return this.damage;
    }

    public double getMagArmor() {
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

    public ArrayList<Enemy> getEnemiesInRange() {
        return this.enemiesInRange;
    }

    /**
     * Нанести урон игроку
     * 
     * @param damage     - Урон
     * @param damageType - Тип урона (0 - чистый, 1 - физ, 2 - маг)
     * @return Нанесенный урон
     */
    public float damaged(float damage, int damageType) {
        damage = Math.abs(damage);
        switch (damageType) {
        case 1:
            // Будем считать что 1 брона = 1хп.
            if (this.getArmor() < damage) {
                this.health = this.getCurrentHealth() - (damage - this.getArmor());
                return (float) (damage - this.getArmor());
            } else {
                return 0;
            }
        case 2:
            // Будем считать что 1 брона = 1хп.
            if (this.getArmor() < damage) {
                this.health = this.getCurrentHealth() - (damage - this.getMagArmor());
                return (float) (damage - this.getMagArmor());
            } else {
                return 0;
            }
        default:
            this.health = this.getCurrentHealth() - (damage);
            return (float) (damage);
        }
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
