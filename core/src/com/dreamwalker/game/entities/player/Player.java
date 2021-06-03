package com.dreamwalker.game.entities.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.Entity;
import com.dreamwalker.game.entities.controllers.MeeleAnimationController;
import com.dreamwalker.game.entities.enemy.Enemy;
import com.dreamwalker.game.skills.Skill;
import com.dreamwalker.game.skills.FlyingSword;

import java.util.ArrayList;

public class Player extends Entity implements Disposable {

    private Control playerControl;

    private double mana;
    private double manaMax;

    private Inventory inventory;

    private ArrayList<Enemy> enemiesInRange;
    private boolean enemyInArea;
    public boolean roomChanged;

    private ArrayList<Skill> skillPanel;

    /**
     * Конструктор
     *
     * @param world - физический мир, в котором будет находится игрок
     * @param x     - стартовая позиция игрока по х
     * @param y     - стартовая позиция игрока по у
     */
    public Player(World world, float x, float y) {
        this.world = world;
        this.animationController = new MeeleAnimationController(this, "player.atlas", "player");
        this.playerControl = new Control(this);
        this.spawnPoint = new Vector2(x, y);
        this.definePlayer();

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
        this.entityBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();

        // Физические границы игрока
        CircleShape shape = new CircleShape();
        shape.setRadius(9 / DreamWalker.PPM);

        fixtureDef.shape = shape;
        this.entityBody.createFixture(fixtureDef);
        this.entityBody.getFixtureList().get(0).setUserData(this);

        // Удаляем фигуру, которая была создана для "тела" игрока
        shape.dispose();

        float scalar = shape.getRadius() * 3.5f;
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

    @Override
    public void render(SpriteBatch batch) {
        this.draw(batch);
        for (Skill skill : this.skillPanel) {
            skill.render(batch);
        }
    }

    public void update(float deltaTime, Vector2 mousePosition) {
        if (this.roomChanged) {
            this.definePlayer();
            this.roomChanged = false;
        }
        this.setPosition(this.getX() - this.getWidth() / 2, this.getY() + 13 / DreamWalker.PPM - this.getHeight() / 2);
        if (this.getCurrentHealth() <= 0) {
            // this.world.destroyBody(this.playersBody);
            // this.world.destroyBody(this.attackArea);
            // this
            this.health = 0;
            this.isAlive = false;
        }
        this.regeneration();
        this.inventory.update(this);
        this.setRegion(this.animationController.getFrame(deltaTime));
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

    public void setWorld(World world) {
        if (this.world != world) {
            this.world.destroyBody(this.entityBody);
            this.world.destroyBody(this.attackArea);
            this.world = world;
            this.roomChanged = true;
        }
    }

    public double getCurrentMana() {
        return this.mana;
    }

    public double getMaxMana() {
        return this.manaMax;
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

    boolean isEnemyInArea() {
        return this.enemyInArea;
    }

    public ArrayList<Skill> getSkillPanel() {
        return this.skillPanel;
    }

    public void setSpawnPoint(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
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
