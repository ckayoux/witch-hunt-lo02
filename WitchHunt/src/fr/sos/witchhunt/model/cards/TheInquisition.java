package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class TheInquisition extends RumourCard {
	//TODO : default value ?
	
	public TheInquisition () {
		this.witchEffect = new Effect() {
			
			@Override
			public void perform() {
				Player me = getMyself();
				me.discard(); //select a card to discard from your hand
				takeNextTurn();
			}
		};
		
		this.huntEffect = new Effect() {
			Player me;
			@Override
			public void perform() {
				//secretly look at their identity
				chooseNextPlayer();
			}
			
			@Override
			public boolean isAllowed() {
				me=getMyself();
				//only playable if you have been revealed as a villager
				return (me.isRevealed() && me.getIdentity()==Identity.VILLAGER);
			}
			
		};
	}

}
