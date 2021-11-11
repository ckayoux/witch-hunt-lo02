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
	public Identity selectIdentity() {
		int n = (int) Math.round(Math.random());
		return (n==0) ? Identity.VILLAGER : Identity.WITCH;
	}

	@Override
	public TurnAction selectTurnAction() {
		return TurnAction.ACCUSE; //TODO do better
	}

	@Override
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList) {
		//accuses a random accusable player
		return accusablePlayersList.get((int) (Math.random() * accusablePlayersList.size()) );
	}

	@Override
	public RumourCard selectWorstCard(RumourCardsPile rcp) {
		/*useful when choosing a card to discard.
		returns a random card among the ones with the lowest witchEffectValue + huntEffectValue sum.*/
		return map.getCardsWithMinOverallValue(rcp).getRandomCard();
	}

	@Override
	public RumourCard selectWitchCard(RumourCardsPile rcp) {
		RumourCardsPile worstWitchCards = map.getCardsWithMinWitchValue(rcp);
		/*returns a random card among the ones with the lowest huntEffectValue among the ones with the lowest witchEffectValue.
		this is not the same as taking the cards with the lowest overall value !
		since this is a groping strategy, we want to avoid spending the best witch effects at the start of the game.*/
		
		return worstWitchCards.getRandomCard(); 
	}

}
