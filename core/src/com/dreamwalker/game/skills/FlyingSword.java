package com.dreamwalker.game.skills;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.dreamwalker.game.player.Player;

import java.util.ArrayList;

public class FlyingSword extends ActiveSkill {

    private final ArrayList<Sword> swordsOnScreen;

    private float damage;
    private float lifeTime;
    private int maxSwordsCount;

    public FlyingSword(int hotKey) {
        super(hotKey);
        this.swordsOnScreen = new ArrayList<>();

        this.coolDown = 100;

        this.mousePosition = null;
        this.maxSwordsCount = 3;
        this.damage = 15;
        this.lifeTime = 35;
        this.manaCost = 15;
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Sword sword : this.swordsOnScreen) {
            sword.draw(batch);
        }
    }

    @Override
    public void usage(Player player) {
        if (this.currentCoolDown == this.coolDown) {
            this.currentCoolDown = 0;
        }
        if (this.currentCoolDown == 0 && this.swordsOnScreen.size() != 3) {
            if (Gdx.input.isKeyJustPressed(super.hotKey)) {
                if (player.getCurrentMana() >= this.manaCost) {
                    player.manaSpend(this.manaCost);
                    if (this.swordsOnScreen.size() <= this.maxSwordsCount) {
                        Sword newSword = new Sword(player, this.damage, this.lifeTime);
                        this.swordsOnScreen.add(newSword);
                    }
                }
            }
        } else {
            this.currentCoolDown++;
        }

        for (int i = 0; i < this.swordsOnScreen.size(); i++) {
            Sword currentSword = this.swordsOnScreen.get(i);
            if (currentSword.getLifeTime() <= 0) {
                this.swordsOnScreen.remove(i);
                currentSword.dispose();
            } else {
                currentSword.move();
                currentSword.decreaseLifeTime();
            }
        }
    }

    public ArrayList<Sword> getSwordsOnScreen() {
        return this.swordsOnScreen;
    }

}
