package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import java.util.List;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class AngryMob extends RumourCard {
	//TODO : default value ?
	public AngryMob() {
		AngryMob cardInstance = this;
		
		this.witchEffect = new WitchEffect() {
			
			@Override
			public void perform() {
				takeNextTurn();
			}
		};
		
		this.huntEffect = new HuntEffect() {
			
			private Player me;
			@Override
			public void perform() {
				me = getMyself();
				//the eligible players are all those who are not revealed and are not immunized by a revealed BroomStick card
				List <Player> eligiblePlayers = Tabletop.getInstance().getUnrevealedPlayersList().stream().filter(p -> !p.isImmunizedAgainstRumourCard(cardInstance)).toList();
				Player target = me.chooseHuntedTarget(eligiblePlayers);
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
