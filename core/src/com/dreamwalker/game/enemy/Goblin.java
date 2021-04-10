package com.dreamwalker.game.enemy;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dreamwalker.game.player.Player;

public class Goblin extends Enemy {

    private Player player;

    private int idleMax = 100; // такты?
    private int idleCounter = 0; // такты?

    private int attackSpeedMax = 100;
    private int attackSpeedCounter = 0;

    private float tempX;
    private float tempY;

    private float spawnX;
    private float spawnY;
    private float idleRadius;

    private Boolean attackFlag = true;
    private Boolean idleFlag = true;

    private Random random;

    /**
     * Конструктор
     * 
     * @param world  - физический мир, в котором будет находится враг
     * @param player - обьект игрока
     * @param x      - стартовая позиция врага по х
     * @param y      - стартовая позиция врага по у
     * @param radius - радус свободной ходьбы
     */
    public Goblin(Player player, float x, float y) {
        super(player, x, y);

        this.player = player;

        this.spawnX = x;
        this.spawnY = y;

        this.idleRadius = 5;
        this.speed = 0.6f;
        this.damage = 20;

        // this.getHe
        random = new Random();

        this.tempX = random.nextInt(Math.round(this.spawnX + this.idleRadius))
                - Math.round(this.spawnX - this.idleRadius);
        this.tempY = random.nextInt(Math.round(this.spawnY + this.idleRadius))
                - Math.round(this.spawnY - this.idleRadius);
        this.idleMax = random.nextInt(500);// количество тиков спокойствия перед движением.
    }

    /**
     * Конструктор
     * 
     * @param world           - физический мир, в котором будет находится враг
     * @param player          - обьект игрока
     * @param enemySpawnPoint - позиция врага
     * @param radius          - радус свободной ходьбы
     */
    public Goblin(Player player, Vector2 enemySpawnPoint) {
        this(player, enemySpawnPoint.x, enemySpawnPoint.y);
    }

    @Override
    public void move() {
        if (Vector2.dst(super.getX(), super.getY(), player.getX(), player.getY()) < 100) {
            Vector2 playerPosition = new Vector2(player.getX(), player.getY());
            Vector2 zombieViewPoint = playerPosition.sub(super.getBox2DBody().getPosition());
            super.getBox2DBody().setTransform(super.getBox2DBody().getPosition(), zombieViewPoint.angleRad());
            super.getBox2DBody().setLinearVelocity(new Vector2((player.getX() - super.getX()) * this.speed,
                    (player.getY() - super.getY()) * this.speed));
            idleFlag = false;
            // super.getBox2DBody().setLinearVelocity(player.getX(), player.getY());
            // прикольный єффект, враг бежит от плеера
        } else {
            // плеер вне зоны зрения врага
            idleFlag = true;
            this.tempX = random.nextInt(Math.round(this.spawnX + this.idleRadius))
                    - Math.round(this.spawnX - this.idleRadius);
            this.tempY = random.nextInt(Math.round(this.spawnY + this.idleRadius))
                    - Math.round(this.spawnY - this.idleRadius);
            super.getBox2DBody().setLinearVelocity(new Vector2(0, 0));
            // написать скрипт для возврата на свой спавнпоинт
        }
    }

    @Override
    public void meleeAttack() {
        if (Vector2.dst(super.getX(), super.getY(), player.getX(), player.getY()) < 50) {
            attackFlag = true;
        } else {
            attackFlag = false;
        }

        // Передлать перезарядку
        if (attackFlag) {
            if (attackSpeedCounter < attackSpeedMax) {
                attackSpeedCounter++;
            } else {
                attackSpeedCounter = 0;

                System.out.println("DAMAGED!" + player.damaged((float) this.getDamage(), 1));
            }
        }
    }

    @Override
    public void setBuff() {
        // TODO Auto-generated method stub
        super.setBuff();
    }

    @Override
    public void idle() {
        if (this.idleFlag) {

            if ((this.tempX == super.getX()) || (this.tempY == super.getY())) {
                System.out.println("generate new integers");
                this.tempX = random.nextInt(Math.round(this.spawnX + this.idleRadius))
                        - Math.round(this.spawnX - this.idleRadius);
                this.tempY = random.nextInt(Math.round(this.spawnY + this.idleRadius))
                        - Math.round(this.spawnY - this.idleRadius);

                if (this.idleMax > this.idleCounter) {
                    this.idleCounter++;
                } else {

                    System.out.println("idle go");
                    this.idleMax = random.nextInt(500);// количество тиков спокойствия перед движением.
                    this.idleCounter = 0;
                    this.idleFlag = false;

                }
            } else {
                // пускаем зомби на случайную координату в радиусе
                Vector2 newPointPosition = new Vector2(this.tempX, this.tempY);

                super.getBox2DBody().setTransform(super.getBox2DBody().getPosition(), newPointPosition.angleRad());

                super.getBox2DBody().setLinearVelocity(new Vector2(tempX, tempY));
            }

        }
    }

    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
