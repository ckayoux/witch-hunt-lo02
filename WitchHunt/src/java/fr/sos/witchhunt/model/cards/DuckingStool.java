package fr.sos.witchhunt.model.cards;

import java.util.List;

import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.Player;

public final class DuckingStool extends RumourCard {
	
	
	public DuckingStool () {
		this.isOffensive=true;
		this.isRisked=true;
		
		DuckingStool cardInstance = this;
		
		this.witchEffect = new WitchEffect("Choose next player.",0) {
			
			@Override
			public void perform() {
				chooseNextPlayer();
			}
		};
		
		this.huntEffect = new HuntEffect("Choose a player.\n"
				+ "/+/They must reveal their identity or discard a card from their hand.\n"
				+ "/+/W -> You gain 1 pt and take next turn.\n"
				+ "/+/V -> You loose 1 pt, they take next turn.\n"
				+ "/+/If they discard -> they take next turn.", 1) {
			//todo : cpu players target suspicious players first with this card, which means the one who have used the more witch effects
			
			Player me;
			List<Player> eligiblePlayers;
			@Override
			public void perform() {
				me = getMyself();
				eligiblePlayers = Tabletop.getInstance().getActivePlayersList()
						.stream().filter(p -> (!p.isRevealed()||p.hasRumourCards()) && 
						!p.isImmunizedAgainstRumourCard(cardInstance)).toList();
				Player target = me.chooseHuntedTarget(eligiblePlayers);
				RumourCard chosenCard=null;
				boolean choseToReveal=false;
				switch(target.revealOrDiscard()) {
					case REVEAL :
						switch(target.revealIdentity()) {
							case WITCH:
								me.eliminate(target);
								me.addScore(1);
								takeNextTurn();
								break;
									
							case VILLAGER:
								me.addScore(-1);
								target.takeNextTurn();
								break;
						}
						break;
						
					case DISCARD :
						target.discard();
						break;
						
					case WITCH:
						//
						break;
				}
				
					
			}
			
			@Override
			public boolean isAllowed() {
				//This card is only playable if you have been revealed as a villager.
				me = getMyself();
				eligiblePlayers = Tabletop.getInstance().getUnrevealedPlayersList().stream().filter(p -> !p.isImmunizedAgainstRumourCard(cardInstance)&&p!=me).toList();
				if(eligiblePlayers.size() > 0) return true;
				else return false;
			}
		};
					
	}
}

