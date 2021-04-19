package com.dreamwalker.game.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.*;
import com.dreamwalker.game.enemy.Enemy;
import com.dreamwalker.game.player.Player;


public class AttackListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData() != null && fixtureB.getUserData() != null){
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy;
            //boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if(variant1){
                Player player = (Player) fixtureA.getUserData();
                player.getEnemiesInRange().add((Enemy) fixtureB.getUserData());
                player.setEnemyInArea(true);
            }
            /*if(variant2){
                Player player = (Player) fixtureB.getUserData();
                player.getEnemiesInRange().add((Enemy) fixtureA.getUserData());
                player.setEnemyInArea(true);
            }*/
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData() != null && fixtureB.getUserData() != null){
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy;
            //boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if(variant1){
                Player player = (Player) fixtureA.getUserData();
                player.getEnemiesInRange().remove((Enemy) fixtureB.getUserData());
                player.setEnemyInArea(true);
            }
            /*if(variant2){
                Player player = (Player) fixtureB.getUserData();
                player.getEnemiesInRange().remove((Enemy) fixtureA.getUserData());
                player.setEnemyInArea(true);
            }*/
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
