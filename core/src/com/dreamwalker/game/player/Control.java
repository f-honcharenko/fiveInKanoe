package com.dreamwalker.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.dreamwalker.game.enemy.Enemy;

public class Control {
    private final Player player;

    Control(Player player) {
        this.player = player;
    }

    /**
     * Общий метод, отвечающий за упрваление персонажем
     */
    void handle(Vector2 mousePosition) {
        this.meleeAttack();
        this.move(mousePosition);
        this.useSkills(mousePosition);
    }

    /**
     * Метод, отвечающий за ближнюю атаку игрока
     */
    private void meleeAttack() {
        if (this.player.isEnemyInArea() && this.player.isDamageDealt()) {
            System.out.println("DEAL");
            for (Enemy enemy : this.player.getEnemiesInRange()) {
                System.out.println(enemy.getCurrentHealth() + " " + player.getEnemiesInRange().size());
                enemy.receiveDamage(this.player.getDamage());
            }
            this.player.setDamageDealt(false);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            this.player.setIsAttacking(true);
        }
    }

    /**
     * Метод, отвечающий за передвижение персонажа
     *
     * @param mousePosition - координаты мыши в пространстве игрового мира
     */
    private void move(Vector2 mousePosition) {
        float speed = this.player.getSpeed();
        Body playersBody = this.player.getPlayersBody();
        // Вычитаем позицию игрока из позиции мыши
        Vector2 playersViewPoint = mousePosition.sub(playersBody.getPosition());
        float angle = playersViewPoint.angleRad();
        this.player.setViewAngle(Math.toDegrees(angle));
        if (this.player.getViewAngle() < 0) {
            this.player.setViewAngle(180 + Math.abs(this.player.getViewAngle() + 180));
        }
        // Позиция игрока остается прежней, в то время, как поворот меняется в
        // зависимости от положения мыши
        playersBody.setTransform(playersBody.getPosition(), angle);
        this.player.getAttackArea().setTransform(playersBody.getPosition(), angle);

        // Обработка нажатий клавиш WASD
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            // this.box2DBody.applyLinearImpulse(new Vector2(0, this.speed),
            // this.box2DBody.getWorldCenter(), true);
            playersBody.setLinearVelocity(0, speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            // this.box2DBody.applyLinearImpulse(new Vector2(0, -this.speed),
            // this.box2DBody.getWorldCenter(), true);
            playersBody.setLinearVelocity(0, -speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            // this.box2DBody.applyLinearImpulse(new Vector2(-this.speed, 0),
            // this.box2DBody.getWorldCenter(), true);
            playersBody.setLinearVelocity(-speed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            // this.box2DBody.applyLinearImpulse(new Vector2(this.speed, 0),
            // this.box2DBody.getWorldCenter(), true);
            playersBody.setLinearVelocity(speed, 0);
        }

        // Обработка сочитаний WD WA SD SA
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            playersBody.setLinearVelocity(speed, speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            playersBody.setLinearVelocity(-speed, speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            playersBody.setLinearVelocity(speed, -speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            playersBody.setLinearVelocity(-speed, -speed);
        }

        // Проверка, нажата ли одна из клавиш WASD
        boolean isMoving = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D);

        // Проверка конфликтующих сочитаний WS AD
        boolean conflictX = Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D);
        boolean conflictY = Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S);

        // Остановить персонажа, если ни одна из клавиш не нажата, или нажаты
        // конфликтующие сочетания
        if (!isMoving || conflictX || conflictY || player.isAttacking()) {
            playersBody.setLinearVelocity(0, 0);
        }
    }

    private void useSkills(Vector2 mousePosition) {
        this.player.getSkillPanel().get(0).setMousePosition(mousePosition);
        this.player.getSkillPanel().get(0).usage(this.player);
    }
}
