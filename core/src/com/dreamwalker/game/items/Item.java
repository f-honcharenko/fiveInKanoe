package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.entities.Entity;

public abstract class Item implements Disposable {
    protected int id;
    protected int count;
    protected String name;
    protected String description;
    private Texture texture;

    public abstract void usage(Entity entity);

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    };

    public Texture getTexture() {
        return this.texture;
    };
}
