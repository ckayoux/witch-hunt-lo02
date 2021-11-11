package fr.sos.witchhunt.model.cards;

import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.players.Player;

public final class DuckingStool extends RumourCard {
	//TODO : default value ?
	public DuckingStool () {
		
		DuckingStool cardInstance = this;
		
		this.witchEffect = new WitchEffect() {
			
			@Override
			public void perform() {
				chooseNextPlayer();
			}
		};
		
		this.huntEffect = new HuntEffect() {
			@Override
			public void perform() {
				/*TODO
				 * choose a player. they must reveal their identity or discard a RC from their hand.
				 * witch : you gain 1 pt, you take next turn
				 * villager : you loose 1 pt, they take next turn
				 * they discard: they take next turn
				 * 
				 * 
				 *the eligible players are all those who are not revealed and are not immunized by a revealed Wart card
				 * List <Player> eligiblePlayers = Tabletop.getInstance().getUnrevealedPlayersList().stream().filter(p -> !p.isImmunizedAgainstRumourCard(cardInstance)).toList();
				 * Player target = me.chooseHuntedTarget(eligiblePlayers);
				 */
			}
			
		};
	}

}
