package com.dreamwalker.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.dreamwalker.game.entities.player.Player;

public class PotionMP extends Item {
    private float regenMana;

    public PotionMP(int count) {
        this.regenMana = 8.2f;
        this.id = 1;
        this.name = "MP Potion";
        this.description = "Regenerate mana points.";
        this.count = count;
        this.setTexture(new Texture("MPpot_icon.png"));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void usage(Player player) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.M) && this.count != 0) {
            player.addMana(this.regenMana);
            this.count--;
        }
    }
}
