package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.Collections;
import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

public final class ExploringStrategy implements PlayStrategy {
	/*Default strategy.
	 * Most choices are done randomly.
	 * The player will avoid spending its best cards.
	 */
	
	CardValueMap map = new CardValueMap();
	
	@Override
	public Identity chooseIdentity() {
		int n = (int) Math.round(Math.random());
		return (n==0) ? Identity.VILLAGER : Identity.WITCH;
	}

	@Override
	public TurnAction chooseTurnAction() {
		return TurnAction.ACCUSE; //TODO do better
	}

	@Override
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList) {
		//accuses a random accusable player
		return accusablePlayersList.get((int) (Math.random() * accusablePlayersList.size()) );
	}

	@Override
	public RumourCard chooseWorstCard(RumourCardsPile rcp) {
		//useful when choosing a card to discard.
		List<RumourCard> worstCards = map.getCardsWithMinWitchValue(rcp);
		//returns a random card among the ones with the lowest witchEffectValue + huntEffectValue sum.
		return worstCards.get((int)(Math.random()*worstCards.size())); 
	}

}
