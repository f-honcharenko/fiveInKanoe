package com.dreamwalker.game.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.dreamwalker.game.entities.enemy.Enemy;

public class Control {

    private final Player player;

    Control(Player player) {
        this.player = player;
    }

    void handle(Vector2 mousePosition) {
        this.meleeAttack();
        this.move(mousePosition);
        this.useSkills(mousePosition);
    }

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
            this.player.getSoundController().playAttackSound(0.37f);
            this.player.setIsAttacking(true);
        }
    }

    private void move(Vector2 mousePosition) {
        float speed = this.player.getSpeed();
        Body playersBody = this.player.getBody();

        Vector2 playersViewPoint = mousePosition.sub(playersBody.getPosition());
        float angle = playersViewPoint.angleRad();
        this.player.setViewAngle(Math.toDegrees(angle));
        if (this.player.getViewAngle() < 0) {
            this.player.setViewAngle(180 + Math.abs(this.player.getViewAngle() + 180));
        }

        playersBody.setTransform(playersBody.getPosition(), angle);
        this.player.getAttackArea().setTransform(playersBody.getPosition(), angle);

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playersBody.setLinearVelocity(0, speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playersBody.setLinearVelocity(0, -speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playersBody.setLinearVelocity(-speed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playersBody.setLinearVelocity(speed, 0);
        }

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

        boolean isMoving = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D);

        boolean conflictX = Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D);
        boolean conflictY = Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S);

        if (!isMoving || conflictX || conflictY || player.isAttacking()) {
            playersBody.setLinearVelocity(0, 0);
        }
        else{
            this.player.getSoundController().playStepSound(0.3f);
        }
    }

    private void useSkills(Vector2 mousePosition) {
        this.player.getSkillPanel().get(0).setMousePosition(mousePosition);
        this.player.getSkillPanel().get(0).usage(this.player);

    }
}
