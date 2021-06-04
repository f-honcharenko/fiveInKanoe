package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.entities.player.Player;

public abstract class Item implements Disposable {
    protected int id;
    protected int count;
    protected String name;
    protected String description;
    private Texture texture;

    public abstract void usage(Player player);

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
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
