package com.dreamwalker.game.entities.attacktypes;

import com.dreamwalker.game.entities.player.Player;

public class MeleeAttack implements AttackStratagy {

		        private double damage;

				public void attack(Player player) {
		        	
		        	
		        	if (this.isPlayerInArea()) {
		                this.setIsAttacking(true);
		                int attackSpeedCounter = 0;
						int attackSpeedMax = 0;
						if (attackSpeedCounter < attackSpeedMax) {
		                    attackSpeedCounter++;
		                } else {
		                    attackSpeedCounter = 0;
		                    player.receiveDamage(this.damage);
		                }
		            } else {
		                this.setIsAttacking(false);
		            }
		        	
		        	
		        	
		        	
		        }

				private void setIsAttacking(boolean b) {
					// TODO Auto-generated method stub
					
				}

				private boolean isPlayerInArea() {
					// TODO Auto-generated method stub
					return false;
				}

					
				

}
