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

/**
 * <b>The initial strategy of all {@link fr.sos.witchhunt.model.players.CPUPlayer CPU-controlled players}, and the one adopted when the situation
 * is not clear enough to choose a more angular strategy.</b>
 * <p>Most choices are done randomly.</p>
 * <p>Tries keeping the {@link CardValueMap#getCardsWithMaxOverallValue(RumourCardsPile) best cards} (in terms of {@link fr.sos.witchhunt.model.cards.CardValue overall value}) for the end of the round.</p>
 * <p>A witch using this strategy {@link fr.sos.witchhunt.model.Identity#WITCH witch} will never reveal their identity unless 
 * {@link fr.sos.witchhunt.model.players.Player#canWitch() they have no more playable Witch? effects in their hand}.</p>
 * @see PlayStrategy
 * @see CPUStrategy
 * @see fr.sos.witchhunt.model.players.CPUPlayer
 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseStrategy()
 * @see CardValueMap
 * @see CardValue
 */
public final class GropingStrategy extends CPUStrategy {
	

	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will select an identity randomly.
	 * <i>{@link fr.sos.witchhunt.model.Identity#WITCH WITCH}</i> and <i>{@link fr.sos.witchhunt.model.Identity#VILLAGER VILLAGER}</i> have the same chances to be chosen.</b>
	 */
	@Override
	public Identity selectIdentity() {
		//selects an identity randomly
		int n = (int) Math.round(Math.random());
		return (n==0) ? Identity.VILLAGER : Identity.WITCH;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will select a random player to accuse.</b>
	 */
	@Override
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList) {
		//accuses a random accusable player
		return accusablePlayersList.get((int) (Math.random() * accusablePlayersList.size()) );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will select a random player to take the next turn.</b>
	 */
	@Override
	public Player selectNextPlayer(List<Player> list) {
		//this strategy selects the next player randomly
		return list.get(list.size()*(int)Math.random());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will select a random card with a 
	 * {@link CardValueMap#getCardsWithMaxWitchValue(RumourCardsPile rcp) positive value} for its {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}.</b>
	 */
	@Override
	public RumourCard selectHuntCard(RumourCardsPile rcp) {
		return cvm.getCardsWithPositiveHuntValue(rcp).getRandomCard();
	}
	
	@Override
	public String toString() {
		return "a groping playstyle";
	}

}
