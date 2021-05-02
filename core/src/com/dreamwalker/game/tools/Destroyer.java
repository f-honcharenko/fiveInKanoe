package com.dreamwalker.game.tools;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Destroyer {
    // список для удаления
    static ArrayList<Body> removableObj = new ArrayList<Body>();

    World world;

    public Destroyer(World world) {
        this.world = world;
    }

    public void destroyBody2D() {
        if (this.removableObj.size() > 0) {
            for (Body obj : removableObj) {
                if (obj != null) {
                    this.world.destroyBody(obj);
                }
            }
        }
    }

    public void addToDestroyList(Body obj) {
        this.removableObj.add(obj);
    }
}
