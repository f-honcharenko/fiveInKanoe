package com.dreamwalker.game.handler;

import com.badlogic.gdx.physics.box2d.*;
import com.dreamwalker.game.entities.enemy.Enemy;
import com.dreamwalker.game.generator.Edge;
import com.dreamwalker.game.items.HedgehogAmmo;
import com.dreamwalker.game.location.Location;
import com.dreamwalker.game.items.ItemInWorld;
import com.dreamwalker.game.entities.player.Player;
import com.dreamwalker.game.skills.Sword;
import com.dreamwalker.game.tools.MapChanger;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class ContactHandler implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        this.enterPlayersMelee(contact);
        this.enterEnemiesMelee(contact);
        this.enterExit(contact);
        this.enterItem(contact);
        this.enterFinalExit(contact);
        this.enterFlyingSword(contact);
        this.enterHedgehog(contact);
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

    // Поставить более вменяемое условие
    private void enterFinalExit(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof String;
            boolean variant2 = fixtureA.getUserData() instanceof String && fixtureB.getUserData() instanceof Player;
            if ((variant1 && fixtureB.getUserData().equals("Exit"))
                    || (variant2 && fixtureA.getUserData().equals("Exit"))) {
                ScreenSwitcher.getGameScreen().nextFloor();
            }
        }
    }

    private void enterExit(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            Player player = null;
            Location location = null;
            Body playersExit = null;
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Location;
            if (variant1) {
                player = (Player) fixtureA.getUserData();
                location = (Location) fixtureB.getUserData();
                playersExit = fixtureB.getBody();
            }
            boolean variant2 = fixtureA.getUserData() instanceof Location && fixtureB.getUserData() instanceof Player;
            if (variant2) {
                player = (Player) fixtureB.getUserData();
                location = (Location) fixtureA.getUserData();
                playersExit = fixtureA.getBody();
            }
            if (player != null && location != null) {
                for (Edge edge : MapChanger.getCurrentVertex().getEdges()) {
                    if (playersExit == edge.getExitFirst() || playersExit == edge.getExitSecond()) {
                        MapChanger.changeLocation(MapChanger.getCurrentVertex().getEdges().indexOf(edge));
                        break;
                    }
                }
            }
        }
    }

    private void enterPlayersMelee(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            Player player = null;
            Enemy enemy = null;
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy;
            if (variant1) {
                player = (Player) fixtureA.getUserData();
                enemy = (Enemy) fixtureB.getUserData();
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if (variant2) {
                player = (Player) fixtureB.getUserData();
                enemy = (Enemy) fixtureA.getUserData();
            }
            if (player != null && enemy != null) {
                player.getEnemiesInRange().add(enemy);
                player.setEnemyInArea(true);
            }
        }
    }

    private void quitPlayersMelee(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            Player player = null;
            Enemy enemy = null;
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy;
            if (variant1) {
                player = (Player) fixtureA.getUserData();
                enemy = (Enemy) fixtureB.getUserData();
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if (variant2) {
                player = (Player) fixtureB.getUserData();
                enemy = (Enemy) fixtureA.getUserData();
            }
            if (player != null && enemy != null) {
                player.getEnemiesInRange().remove(enemy);
                if (player.getEnemiesInRange().size() == 0) {
                    player.setEnemyInArea(false);
                }
            }
        }
    }

    private void enterEnemiesMelee(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        // contact
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            boolean variant1 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy;
            if (variant1) {
                if ((fixtureB.isSensor()) && (!fixtureA.isSensor())) {
                    Enemy enemy = (Enemy) fixtureB.getUserData();
                    enemy.setPlayerInArea(true);
                }
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if (variant2) {
                if ((fixtureA.isSensor()) && (!fixtureB.isSensor())) {
                    Enemy enemy = (Enemy) fixtureA.getUserData();
                    enemy.setPlayerInArea(true);
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
                if ((fixtureB.isSensor()) && (!fixtureA.isSensor())) {
                    Enemy enemy = (Enemy) fixtureB.getUserData();
                    enemy.setPlayerInArea(false);
                }
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player;
            if (variant2) {
                if ((fixtureA.isSensor()) && (!fixtureB.isSensor())) {
                    Enemy enemy = (Enemy) fixtureA.getUserData();
                    enemy.setPlayerInArea(false);
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
                Sword sword = (Sword)fixtureA.getUserData();
                Enemy enemy = (Enemy)fixtureB.getUserData();
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

    private void enterHedgehog(Contact contact){
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData() != null &&  fixtureB.getUserData() != null){
            boolean variant1 = fixtureA.getUserData() instanceof HedgehogAmmo && fixtureB.getUserData() instanceof Enemy;
            if(variant1){
                HedgehogAmmo hedgehogAmmo = (HedgehogAmmo)fixtureA.getUserData();
                Enemy enemy = (Enemy)fixtureB.getUserData();
                if(!fixtureB.isSensor()){
                    enemy.receiveDamage(hedgehogAmmo.getDamage());
                    hedgehogAmmo.setLifeTime(0);
                }
            }
            boolean variant2 = fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof HedgehogAmmo;
            if(variant2){
                Enemy enemy = (Enemy)fixtureA.getUserData();
                HedgehogAmmo hedgehogAmmo = (HedgehogAmmo)fixtureB.getUserData();
                if(!fixtureA.isSensor()){
                    enemy.receiveDamage(hedgehogAmmo.getDamage());
                    hedgehogAmmo.setLifeTime(0);
                }
            }
        }
    }

    private void enterItem(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            boolean variant1 = fixtureA.getUserData() instanceof ItemInWorld && fixtureB.getUserData() instanceof Player;
            if (variant1) {
                ItemInWorld item = (ItemInWorld) fixtureA.getUserData();
                Player player = (Player) fixtureB.getUserData();
                if (fixtureA.isSensor()) {
                    if (player.getInventory().putItem(item.getItem())) {
                        item.haveToDestroy = true;
                    }
                }
            }
            boolean variant2 = fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof ItemInWorld;
            if (variant2) {
                Player player = (Player) fixtureA.getUserData();
                ItemInWorld item = (ItemInWorld) fixtureB.getUserData();
                if (fixtureB.isSensor()) {
                    if (player.getInventory().putItem(item.getItem())) {
                        item.haveToDestroy = true;
                    }
                }
            }
        }
    }
}
