package com.dreamwalker.game.handler;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.enemy.Enemy;
import com.dreamwalker.game.generator.Edge;
import com.dreamwalker.game.location.Location;
import com.dreamwalker.game.player.Player;
import com.dreamwalker.game.skills.Sword;

public class ContactHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        this.enterPlayersMelee(contact);
        this.enterEnemiesMelee(contact);
        this.enterFlyingSword(contact);
        this.enterExit(contact);
    }

    @Override
    public void endContact(Contact contact) {
        this.quitPlayersMelee(contact);
        this.quitEnemiesMelee(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void enterExit(Contact contact){
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData() != null && fixtureB != null){
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Location;
            if(variant1){
                System.out.println("VAR1");
                Player player = (Player)fixtureA.getUserData();
                Location location = (Location)fixtureB.getUserData();
                Vector2 exitPosition = fixtureB.getBody().getPosition();
                for(Edge edge : location.getCurrentVertex().getEdges()){
                    Rectangle exitArea = edge.getExitFirst();
                    float exitX = (exitArea.getX() + exitArea.getWidth() / 2) / DreamWalker.PPM;
                    float exitY = (exitArea.getY() + exitArea.getHeight() / 2) / DreamWalker.PPM;
                    System.out.println(exitPosition.x + " " + exitPosition.y + '\n' + exitX + ' ' + exitY + "\n-------");
                    if(exitPosition.x == exitX && exitPosition.y == exitY){
                        location.moveTo(location.getCurrentVertex().getEdges().indexOf(edge));
                        player.setPosition(edge.getExitSecond().getX(), edge.getExitSecond().getY());
                    }
                    exitArea = edge.getExitSecond();
                    exitX = (exitArea.getX() + exitArea.getWidth() / 2) / DreamWalker.PPM;
                    exitY = (exitArea.getY() + exitArea.getHeight() / 2) / DreamWalker.PPM;
                    System.out.println(exitPosition.x + " " + exitPosition.y + '\n' + exitX + ' ' + exitY + "\n-------");
                    if(exitPosition.x == exitX && exitPosition.y == exitY){
                        location.moveTo(location.getCurrentVertex().getEdges().indexOf(edge));
                        player.setPosition(edge.getExitFirst().getX(), edge.getExitFirst().getY());
                    }
                }
            }
            boolean variant2 = fixtureA.getUserData() instanceof Location && fixtureB.getUserData() instanceof Player;
            if(variant2){
                System.out.println("VAR2");
                Player player = (Player)fixtureB.getUserData();
                Location location = (Location)fixtureA.getUserData();
                Vector2 exitPosition = fixtureA.getBody().getPosition();
                for(Edge edge : location.getCurrentVertex().getEdges()){
                    Rectangle exitArea = edge.getExitFirst();
                    float exitX = (exitArea.getX() + exitArea.getWidth() / 2) / DreamWalker.PPM;
                    float exitY = (exitArea.getY() + exitArea.getHeight() / 2) / DreamWalker.PPM;
                    System.out.println(exitPosition.x + " " + exitPosition.y + '\n' + exitX + ' ' + exitY + "\n-------");
                    if(exitPosition.x == exitX && exitPosition.y == exitY){
                        location.moveTo(location.getCurrentVertex().getEdges().indexOf(edge));
                        player.setPosition(edge.getExitSecond().getX(), edge.getExitSecond().getY());
                    }
                    exitArea = edge.getExitSecond();
                    exitX = (exitArea.getX() + exitArea.getWidth() / 2) / DreamWalker.PPM;
                    exitY = (exitArea.getY() + exitArea.getHeight() / 2) / DreamWalker.PPM;
                    System.out.println(exitPosition.x + " " + exitPosition.y + '\n' + exitX + ' ' + exitY + "\n-------");
                    if(exitPosition.x == exitX && exitPosition.y == exitY){
                        location.moveTo(location.getCurrentVertex().getEdges().indexOf(edge));
                        player.setPosition(edge.getExitFirst().getX(), edge.getExitFirst().getY());
                    }
                }
            }
        }
    }

    private void enterPlayersMelee(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy;
            if (variant1) {
                Player player = (Player) fixtureA.getUserData();
                player.getEnemiesInRange().add((Enemy) fixtureB.getUserData());
                player.setEnemyInArea(true);
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if (variant2) {
                Player player = (Player) fixtureB.getUserData();
                player.getEnemiesInRange().add((Enemy) fixtureA.getUserData());
                player.setEnemyInArea(true);
            }
        }
    }

    private void quitPlayersMelee(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy;

            if (variant1) {
                Player player = (Player) fixtureA.getUserData();
                boolean test2 = player.getAttackArea().getFixtureList().get(0) == fixtureA;
                player.getEnemiesInRange().remove((Enemy) fixtureB.getUserData());
                if (player.getEnemiesInRange().size() == 0) {
                    player.setEnemyInArea(false);
                }
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if (variant2) {
                Player player = (Player) fixtureB.getUserData();
                player.getEnemiesInRange().remove((Enemy) fixtureA.getUserData());
                player.setEnemyInArea(true);
            }
        }
    }

    private void enterEnemiesMelee(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        // contact
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            System.out.println(fixtureA.getUserData() + "|" + fixtureB.getUserData());

            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy;
            if (variant1) {
                if ((fixtureB.isSensor()) && (!fixtureA.isSensor())) {
                    Player player = (Player) fixtureA.getUserData();
                    Enemy enemy = (Enemy) fixtureB.getUserData();
                    enemy.setPlayerInArea(true);
                    System.out.println(enemy.isPlayerInArea());
                }
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if (variant2) {
                if ((fixtureA.isSensor()) && (!fixtureB.isSensor())) {
                    Enemy enemy = (Enemy) fixtureA.getUserData();
                    Player player = (Player) fixtureB.getUserData();
                    enemy.setPlayerInArea(true);
                    System.out.println(enemy.isPlayerInArea());
                }
            }
        }
    }

    private void quitEnemiesMelee(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy;
            if (variant1) {
                System.out.println("PLayer exit Attack area1");
                if ((fixtureB.isSensor()) && (!fixtureA.isSensor())) {
                    Player player = (Player) fixtureA.getUserData();
                    Enemy enemy = (Enemy) fixtureB.getUserData();
                    enemy.setPlayerInArea(false);
                    System.out.println(enemy.isPlayerInArea());
                }
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if (variant2) {
                System.out.println("PLayer exit Attack area2");
                if ((fixtureA.isSensor()) && (!fixtureB.isSensor())) {
                    Enemy enemy = (Enemy) fixtureA.getUserData();
                    Player player = (Player) fixtureB.getUserData();
                    enemy.setPlayerInArea(false);
                    System.out.println(enemy.isPlayerInArea());
                }

            }
        }
    }

    private void enterFlyingSword(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            boolean variant1 = fixtureA.getUserData() instanceof Sword && fixtureB.getUserData() instanceof Enemy;
            if (variant1) {
                Sword sword = (Sword) fixtureA.getUserData();
                Enemy enemy = (Enemy) fixtureB.getUserData();
                if (!fixtureB.isSensor()) {
                    enemy.receiveDamage(sword.getDamage());
                }
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Sword;
            if (variant2) {
                Enemy enemy = (Enemy) fixtureA.getUserData();
                Sword sword = (Sword) fixtureB.getUserData();
                if (!fixtureA.isSensor()) {
                    enemy.receiveDamage(sword.getDamage());
                }
            }
        }
    }
}
