package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class AngryMob extends RumourCard {


	
	public AngryMob() {
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
				//TODO : select another unrevealed player.
				//reveal their identity.
				//+2 pts if witch, then takeNextTurn()
				//-2 pts if villager, then set the next player to them
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
	
	@Override
	public boolean witch() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hunt() {
		// TODO Auto-generated method stub
		return false;
	}

}
