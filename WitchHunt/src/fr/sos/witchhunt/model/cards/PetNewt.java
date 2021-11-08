package fr.sos.witchhunt.model.cards;

public final class PetNewt extends RumourCard {
	//TODO : default value ?
	public PetNewt () {
		this.witchEffect = new Effect() {
			
			@Override
			public void perform() {
				takeNextTurn();
			}
		};
		
		this.huntEffect = new Effect() {
			@Override
			public void perform() {
				//take a revealed (and not random) rumour card from any other player into your hand + reset it ?
				chooseNextPlayer();
			}
			
		};
	}

}
