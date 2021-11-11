package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.players.cpustrategies.CardValue;

public final class Wart extends RumourCard {
	//TODO : default value ?
	
	private int defaultAdditionnalValue = 1; //this card has an additionnal effect (immunity against DuckingStool).
	//ToutDoux : set this value to 0 in CardValueMap if DuckingStool is revealed
	
	public Wart() {
		
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
	@Override
	public CardValue getDefaultValue() {
		return new CardValue(witchEffect.getValue(),huntEffect.getValue(),defaultAdditionnalValue);
	};
}
