package com.dreamwalker.game.skills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class ActiveSkill implements Skill {
    protected int hotKey;
    protected Vector2 mousePosition;
    protected int coolDown;
    protected int currentCoolDown;
    protected int manaCost;

    protected ActiveSkill(int hotKey) {
        this.hotKey = hotKey;
        this.currentCoolDown = 0;
    }

    public void setMousePosition(Vector2 mousePosition) {
        this.mousePosition = mousePosition;
    }

    public abstract void render(SpriteBatch batch);

    public int getCoolDown() {
        return this.coolDown;
    }

    public int getCurrentCoolDown() {
        return this.currentCoolDown;
    }

    public int getManaCost() {
        return this.manaCost;
    }
}
