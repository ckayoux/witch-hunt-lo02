package fr.sos.witchhunt.model.cards;

import java.util.List;

import fr.sos.witchhunt.model.players.cpustrategies.CardValue;

public final class Broomstick extends RumourCard {
	//TODO : default value ?
	private int defaultAdditionnalValue = 1; //this card has an additionnal effect (immunity against AngryMob).
	//ToutDoux : set this value to 0 in CardValueMap if AngryMob is revealed
	
	public Broomstick() {
		
		this.witchEffect = new Effect() {

			@Override
			public void perform() {
				takeNextTurn();
			}
			
		};
		
		 this.huntEffect =  new Effect() {

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
	@Override
	public CardValue getDefaultValue() {
		return new CardValue(witchEffect.getValue(),huntEffect.getValue(),defaultAdditionnalValue);
	};

}
