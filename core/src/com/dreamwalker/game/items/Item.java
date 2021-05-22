package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public abstract class Item implements Disposable {
    private int ID;
    private int count;
    private String name;
    private String description;
    private Texture texture;

    public Item(int ID, String name, String description, int count) {
        this.count = 1;
        this.ID = ID;
        this.name = name;
        this.description = description;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.ID;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    };

    public Texture getTexture() {
        return this.texture;
    };
}
