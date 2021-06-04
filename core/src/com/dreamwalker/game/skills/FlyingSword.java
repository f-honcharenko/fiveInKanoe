package com.dreamwalker.game.skills;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.entities.player.Player;

import java.util.ArrayList;

public class FlyingSword extends Skill implements Disposable {

    private final ArrayList<Sword> swordsOnScreen;

    private final float damage;
    private final float lifeTime;
    private final int maxSwordsCount;
    private final Sound flyingSwordSound;


    public FlyingSword(int hotKey) {
        super(hotKey);
        this.flyingSwordSound = Gdx.audio.newSound(Gdx.files.internal("sounds/flyingSword.mp3"));
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
                        this.flyingSwordSound.play(0.4f);
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
                currentSword.getWorld().destroyBody(currentSword.getBody());
            } else {
                currentSword.move();
                currentSword.decreaseLifeTime();
            }
        }
    }

    @Override
    public void dispose() {
        this.flyingSwordSound.dispose();
    }
}
