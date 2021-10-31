package fr.sos.witchhunt.model.players.cpustrategies;

import fr.sos.witchhunt.model.Identity;

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
}
