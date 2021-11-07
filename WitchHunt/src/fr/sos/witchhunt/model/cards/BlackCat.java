package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class BlackCat extends RumourCard {

	public BlackCat() {
		
		this.witchEffect = new Effect() {
			@Override
			public void perform() {
				takeNextTurn();
			}
		};
		
		this.huntEffect = new Effect() {
			
			private Player me;
			@Override
			public void perform() {
				me = Tabletop.getInstance().getCurrentPlayer();
				Player target = me.chooseTarget(Tabletop.getInstance().getUnrevealedPlayersList());
				switch(target.forcedReveal()) {
					case WITCH:
					me.addScore(2);
					
					case VILLAGER:
					me.addScore(-2);
				}
			}
			
			@Override
			public boolean isAllowed() {
				//This card is only playable if you have been revealed as a villager.
				me = Tabletop.getInstance().getCurrentPlayer();
				if(me.isRevealed() && me.getIdentity() == Identity.VILLAGER) return true;
				else return false;
			}
		};
		
	}

}
