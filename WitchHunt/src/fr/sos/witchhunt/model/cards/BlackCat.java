package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class BlackCat extends RumourCard {
	//TODO : default value ?
	public BlackCat() {
		
		this.witchEffect = new WitchEffect() {
			@Override
			public void perform() {
				takeNextTurn();
			}
		};
		
		this.huntEffect = new HuntEffect() {
			

			@Override
			public void perform() {
				//TODO add one discarded card to your hand, and then discard this card.
				
			}
			
		};
		
	}

}
