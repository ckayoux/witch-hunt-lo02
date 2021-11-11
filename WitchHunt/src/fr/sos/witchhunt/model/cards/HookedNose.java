package fr.sos.witchhunt.model.cards;

public final class HookedNose extends RumourCard {
	//TODO : default value ?
	public HookedNose () {
		this.witchEffect = new WitchEffect() {
			
			@Override
			public void perform() {
				//TODO : take one card (not random) from the hand of the player who accused you (if hasRumourCards)
				takeNextTurn();
			}
		};
		
		this.huntEffect = new HuntEffect() {
			@Override
			public void perform() {
				//Take a random card from their hand and add it to yours
				chooseNextPlayer();
			}
			
		};
	}

}
