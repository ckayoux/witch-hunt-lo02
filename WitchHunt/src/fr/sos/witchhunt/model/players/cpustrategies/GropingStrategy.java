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

public final class GropingStrategy extends CPUStrategy { //CPUStrategy implements PlayStrategy
	/*Default strategy, chosen by most cpu players at the start of the game.
	 * Most choices are done randomly.
	 * The player will avoid spending its best cards.
	 */
	public GropingStrategy() {
		this.gameIsTightThresold=2;
	}
	@Override
	public Identity selectIdentity() {
		//selects an identity randomly
		int n = (int) Math.round(Math.random());
		return (n==0) ? Identity.VILLAGER : Identity.WITCH;
	}

	@Override
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList) {
		//accuses a random accusable player
		return accusablePlayersList.get((int) (Math.random() * accusablePlayersList.size()) );
	}


	@Override
	public RumourCard selectWitchCard(RumourCardsPile rcp) {
		//selects the cards with the worse witch effects (groping strategy)
		return super.selectWitchCard(rcp);
	}

	@Override
	public Player selectNextPlayer(List<Player> list) {
		//this strategy selects the next player randomly
		return list.get(list.size()*(int)Math.random());
	}

	@Override
	public RumourCard selectHuntCard(RumourCardsPile rcp) {
		return rcp.getRandomCard();
	}


}
