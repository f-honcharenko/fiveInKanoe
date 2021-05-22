package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;

public class PotionMP extends Item {
    private int ID;
    private int count;
    private String name;
    private String description;

    public PotionMP(int count) {
        super(1, "MP Potion", "Regenerate mana points.", count);
        this.setTexture(new Texture("MPpot_icon.png"));
    }

    @Override
    public void dispose() {

    }
}
