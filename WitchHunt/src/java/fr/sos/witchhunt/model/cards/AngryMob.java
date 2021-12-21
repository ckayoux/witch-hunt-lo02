package fr.sos.witchhunt.model.cards;

import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class AngryMob extends RumourCard {
	
	//done, must test
	public AngryMob() {
		AngryMob cardInstance = this;
		this.isRisked=true;
		this.isOffensive=true;
		
		this.witchEffect = new WitchEffect() {
			
			@Override
			public void perform() {
				takeNextTurn();
			}
		};
		
		this.huntEffect = new HuntEffect("(Only playable if you've been revealed as a Villager)\n"
				+ "/+/Reveal another player's identity.\n"
				+ "/+/W -> You gain 2pts and take next turn.\n"
				+ "/+/V -> You loose 2 pts, they take next turn.", 0) {
			
			private Player me;
			@Override
			public void perform() {
				me = getMyself();
				//the eligible players are all those who are not revealed and are not immunized by a revealed BroomStick card
				List <Player> eligiblePlayers = Tabletop.getInstance().getUnrevealedPlayersList().stream().filter(p -> !p.isImmunizedAgainstRumourCard(cardInstance)).toList();
				Player target = me.chooseHuntedTarget(eligiblePlayers);
				switch(target.revealIdentity()) {
					case WITCH:
					me.addScore(2);
					me.playTurnAgain();
					break;
					
					case VILLAGER:
					me.addScore(-2);
					Tabletop.getInstance().getCurrentRound().setNextPlayer(target);
					break;
				}
			}
			
			@Override
			public boolean isAllowed() {
				//This card is only playable if you have been revealed as a villager.
				me = getMyself();
				List <Player> eligiblePlayers = Tabletop.getInstance().getUnrevealedPlayersList().stream().filter(p -> !p.isImmunizedAgainstRumourCard(cardInstance)&&p!=me).toList();
				if(me.isRevealed() && me.getIdentity() == Identity.VILLAGER &&
						eligiblePlayers.size() > 0) return true;
				else return false;
			}
		};
		
	}

}
