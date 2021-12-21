package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.players.cpustrategies.CardValue;

public final class Wart extends RumourCard {
	
	private int defaultAdditionnalValue = 1; //this card has an additionnal effect (immunity against DuckingStool).
	//ToutDoux : set this value to 0 in CardValueMap if DuckingStool is revealed
	
	public Wart() {
		super("While revealed, you cannot be chosen by the Ducking Stool.",1);
		
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
		if(this.revealed && rc.getClass().equals(DuckingStool.class)) {
			return true; //grants immunity against AngryMob if revealed
		}
		else {
			return false;
		}
	}

}
