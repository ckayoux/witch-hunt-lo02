package fr.sos.witchhunt.model.players.cpustrategies;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

public final class ExploringStrategy implements PlayStrategy {
	/*Default strategy.
	 * Most choices are done randomly.
	 * The player will avoid spending its best cards.
	 */
	
	@Override
	public Identity chooseIdentity() {
		int n = (int) Math.random()*2;
		return (n==0) ? Identity.VILLAGER : Identity.WITCH;
	}

	@Override
	public TurnAction chooseTurnAction() {
		return TurnAction.ACCUSE; //TODO do better
	}

	@Override
	public Player selectPlayerToAccuse() {
		// TODO
		return null;
	} 
}
