package fr.sos.witchhunt.model.cards;

import java.util.List;

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
				Player me = getMyself();
				List<Player> eligiblePlayers = Tabletop.getInstance().getActivePlayersList().stream()
						.filter(p -> (p != getMyself() && p.hasRevealedRumourCards() )).toList();
				Player target = me.chooseHuntedTarget(eligiblePlayers);
				RumourCard chosenCard = me.chooseRevealedCard(target.getRevealedSubhand());
				me.requestHasChosenCardScreen(chosenCard, false);
				me.takeRumourCard(chosenCard, target);
				chosenCard.reset();
				chooseNextPlayer();
			}
			
			@Override
			public boolean isAllowed() {
				//there must be at least 1 other player with a revealed rumour card
				List<Player> eligiblePlayers = Tabletop.getInstance().getActivePlayersList().stream()
						.filter(p -> (p != getMyself() && p.hasRevealedRumourCards() )).toList();
				return (!eligiblePlayers.isEmpty());
			}
			
		};
	}

}
