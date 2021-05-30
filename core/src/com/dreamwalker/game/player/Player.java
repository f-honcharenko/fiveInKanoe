package com.dreamwalker.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.enemy.Enemy;
import com.dreamwalker.game.items.PotionHP;
import com.dreamwalker.game.skills.ActiveSkill;
import com.dreamwalker.game.skills.FlyingSword;
import com.dreamwalker.game.skills.PassiveSkill;
import com.dreamwalker.game.skills.Skill;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Sprite implements Disposable {
    // Физический мир, в котором находится игрок
    private World world;
    // Физическое "тело" игрока
    private Body playersBody;
    private Body attackArea;

    private Animations playersAnimations;
    private Control playerControl;

    private double health;
    private double healthMax;
    private double mana;
    private double manaMax;
    private double damage;
    private double armor;

    private Inventory inventory;

    private float speed;
    private float attackSpeedCoefficient;

    private double viewAngle;

    private boolean isAlive;

    private ArrayList<Enemy> enemiesInRange;
    private boolean enemyInArea;
    private boolean isAttacking;
    private boolean isDamageDealt;
    public boolean roomChanged;

    private ArrayList<PassiveSkill> passiveSkills;
    private ArrayList<ActiveSkill> skillPanel;
    private Vector2 spawnPoint;

    /**
     * Конструктор
     *
     * @param world - физический мир, в котором будет находится игрок
     * @param x     - стартовая позиция игрока по х
     * @param y     - стартовая позиция игрока по у
     */
    public Player(World world, float x, float y) {
        this.world = world;
        this.playersAnimations = new Animations(this, "player.atlas");
        this.playerControl = new Control(this);
        this.spawnPoint = new Vector2(x, y);
        this.definePlayer();

        this.passiveSkills = new ArrayList<>();
        this.skillPanel = new ArrayList<>();
        this.skillPanel.add(new FlyingSword(Input.Keys.E));

        this.enemiesInRange = new ArrayList<>();
        this.enemyInArea = false;
        this.isAttacking = false;
        this.isDamageDealt = false;
        this.roomChanged = false;

        this.inventory = new Inventory(10);

        this.isAlive = true;
        this.damage = 2;
        this.speed = 1.5f;
        this.health = 100;
        this.mana = 50;
        this.armor = 4;
        this.healthMax = 100;
        this.manaMax = 100;
        this.attackSpeedCoefficient = 1.5f;

        this.setBounds(0, 0, 54 / DreamWalker.PPM, 54 / DreamWalker.PPM); // 54
    }

    private void definePlayer() {
        // Задача физических свойств для "тела" игрока
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(this.spawnPoint);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Создаем физическое "тело" игрока в игровом мире на основе свойств
        this.playersBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();

        // Физические границы игрока
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / DreamWalker.PPM);

        fixtureDef.shape = shape;
        this.playersBody.createFixture(fixtureDef);
        this.playersBody.getFixtureList().get(0).setUserData(this);

        // Удаляем фигуру, которая была создана для "тела" игрока
        shape.dispose();

        float scalar = shape.getRadius() * 2;
        this.attackArea = this.world.createBody(bodyDef);
        FixtureDef attackFixture = new FixtureDef();
        PolygonShape dmgSectorShape = new PolygonShape();

        Vector2[] vertices = { new Vector2(0, 0),
                new Vector2(scalar * (float) (Math.cos(5 * Math.PI / 3)), scalar * (float) (Math.sin(5 * Math.PI / 3))),
                new Vector2(scalar * (float) (Math.cos(7 * Math.PI / 4)), scalar * (float) (Math.sin(7 * Math.PI / 4))),
                new Vector2(scalar * (float) (Math.cos(11 * Math.PI / 6)), scalar * (float) (Math.sin(11 * Math.PI / 6))),
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

    /**
     * Конструктор
     *
     * @param world      - физический мир, в котором будет находится игрок
     * @param spawnPoint - стартовая позиция игрока
     */
    public Player(World world, Vector2 spawnPoint) {
        this(world, spawnPoint.x, spawnPoint.y);
    }

    public void render(SpriteBatch batch) {
        this.draw(batch);
        for (ActiveSkill skill : this.skillPanel) {
            skill.render(batch);
        }
    }

    public void update(float deltaTime, Vector2 mousePosition) {
        if(this.roomChanged){
            this.definePlayer();
            this.roomChanged = false;
        }
        this.setPosition(this.getX() - this.getWidth() / 2, this.getY() + 0.04f - this.getHeight() / 2);
        if (this.getCurrentHealth() <= 0) {
            // this.world.destroyBody(this.playersBody);
            // this.world.destroyBody(this.attackArea);
            // this
            this.health = 0;
            this.isAlive = false;
        }
        this.regeneration();
        this.setRegion(this.playersAnimations.getFrame(deltaTime));
        this.playerControl.handle(mousePosition);
    }

    /**
     * метод, отвечающий регенерацию показателей персонажа
     */
    public void regeneration() {
        if (isAlive) {
            if (this.mana < this.manaMax) {
                this.mana += 0.1;
            }
            if (this.health < this.healthMax) {
                this.health += 0.1;
            }
        }
    }

    public void receiveDamage(double damage) {
        this.health -= damage;
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
                return damage;
        }
    }

    /**
     * Метод, отвечающий за наложение эффектов на игрока
     */
    public void setBuff() {
        throw new NotImplementedException();
    }

    public void setWorld(World world) {
        if(this.world != world){
            this.world.destroyBody(this.playersBody);
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

    public ArrayList<Enemy> getEnemiesInRange() {
        return this.enemiesInRange;
    }

    public void setEnemyInArea(boolean enemyInArea) {
        this.enemyInArea = enemyInArea;
    }

    public boolean isDamageDealt() {
        return this.isDamageDealt;
    }

    public void setDamageDealt(boolean damageDealt) {
        this.isDamageDealt = damageDealt;
    }

    public Body getPlayersBody() {
        return this.playersBody;
    }

    boolean isAttacking() {
        return this.isAttacking;
    }

    void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    float getAttackSpeedCoefficient() {
        return this.attackSpeedCoefficient;
    }

    double getViewAngle() {
        return this.viewAngle;
    }

    void setViewAngle(double viewAngle) {
        this.viewAngle = viewAngle;
    }

    public Body getAttackArea() {
        return this.attackArea;
    }

    boolean isEnemyInArea() {
        return this.enemyInArea;
    }

    public ArrayList<ActiveSkill> getSkillPanel() {
        return this.skillPanel;
    }

    public void setSpawnPoint(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public boolean isAlive() {
        return this.isAlive;
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

    public double manaSpend(int count) {
        this.mana -= count;
        return this.mana;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void dispose() {

        this.getTexture().dispose();
    }
}
