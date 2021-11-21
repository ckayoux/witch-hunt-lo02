package fr.sos.witchhunt.model.cards;

import java.util.List;

import fr.sos.witchhunt.model.players.cpustrategies.CardValue;

public final class Broomstick extends RumourCard {
	//done, must test
	public Broomstick() {
		super("While revealed, you cannot be chosen by the Angry Mob.",1);
		
		this.witchEffect = new WitchEffect() {

			@Override
			public void perform() {
				takeNextTurn();
			}
			
		};
		
		 this.huntEffect =  new HuntEffect() {

				@Override
				public void perform() {
					chooseNextPlayer();
				}
				
			};
	}
	
	@Override
	public boolean grantsImmunityAgainst(RumourCard rc) {
		if(this.revealed && rc.getClass().equals(AngryMob.class)) {
			return true; //grants immunity against AngryMob if revealed
		}
		else {
			return false;
		}
	}

}
