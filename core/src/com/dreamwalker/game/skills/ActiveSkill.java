package com.dreamwalker.game.skills;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class ActiveSkill implements Skill{
    protected int hotKey;
    protected Vector2 mousePosition;

    protected ActiveSkill(int hotKey){
        this.hotKey = hotKey;
    }

    public void setMousePosition(Vector2 mousePosition){
        this.mousePosition = mousePosition;
    }
}
