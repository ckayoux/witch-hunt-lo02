package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.players.Player;

public final class PetNewt extends RumourCard {
	//TODO : default value ?
	public PetNewt () {
		this.witchEffect = new WitchEffect() {
			
			@Override
			public void perform() {
				takeNextTurn();
			}
		};
		
		this.huntEffect = new HuntEffect("Take a revealed rumour card from any other player into your hand.\n"
				+"/+/Choose next player.",2) {
			//cpu players target player with the best revealed rumour cards first
			@Override
			public void perform() {
				//take a revealed (and not random) rumour card from any other player into your hand + reset it ?
				chooseNextPlayer();
			}
			
			@Override
			public boolean isAllowed() {
				//there must be at least 1 player with a revealed rumour card
				for (Player p : Tabletop.getInstance().getActivePlayersList()) {
					if (p.getRevealedSubhand().getCardsCount()>0) return true;
				}
				return false;
			}
			
		};
	}

}
