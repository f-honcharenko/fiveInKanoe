package com.dreamwalker.game.entities.attacktypes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.dreamwalker.game.entities.player.Player;
import com.dreamwalker.game.skills.Sword;

public class RangeAttack implements AttackStratagy{


	private final ArrayList<Arrow> arrowsOnScreen;
	private Object currentArcherCoolDown;
	private Object ArchercoolDown;
	private Object damage;
	private Object swordsOnScreen;

	public void attack(Player player){
        if (this.currentArcherCoolDown == this.ArchercoolDown) {
            this.currentArcherCoolDown = 0;
        }
        if (this.currentArcherCoolDown == 0 ) {
            if (Gdx.input.isKeyJustPressed(super.hotKey)) {
                    
                        Arrow newArrow = new Arrow();
                        Object newSword;
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
                currentArrow.dispose();
                // if()
                currentArrow.getWorld().destroyBody(currentArrow.getBody());
            } else {
                currentArrow.move();
                currentArrow.decreaseLifeTime();
            }
        }
    }
		
	}


