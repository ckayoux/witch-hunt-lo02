package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.players.Player;

public final class PointedHat extends RumourCard {
	//TODO : default value ?
	public PointedHat () {
		this.witchEffect = new Effect() {
			private Player me;
			@Override
			public void perform() {
				//choose a revealed card from your hand and reset() it
				takeNextTurn();
			}
			
			@Override
			public boolean isAllowed() {
				//only playable if I have a revealed rumour card
				return true; //TEMPORARY !!
			}
			
		};
		
		this.huntEffect = new Effect() {
			private Player me;
			@Override
			public void perform() {
				//choose a revealed card from your hand and reset() it
				chooseNextPlayer();
			}
			
			@Override
			public boolean isAllowed() {
				//only playable if I have a revealed rumour card
				return true; //TEMPORARY !!
			}
			
		};
	}

}
