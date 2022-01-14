package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.List;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.CPUPlayer;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

public interface PlayStrategy {
	
	/**
	 * <p><b>Chooses an {@link fr.sos.witchhunt.model.Identity identity}.</b></p>
	 * <p><i>{@link fr.sos.witchhunt.model.Identity#VILLAGER VILLAGER}</i> is more suited for a player with an aggressive playstyle, while
	 * <i>{@link fr.sos.witchhunt.model.Identity#WITCH WITCH}</i> corresponds better to a player with a defensive or careful playstyle</p>
	 * @return Either <i>{@link fr.sos.witchhunt.model.Identity#VILLAGER VILLAGER}</i> or <i>{@link fr.sos.witchhunt.model.Identity#WITCH WITCH}</i>.
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseIdentity() CPUPlayer::chooseIdentity()
	 */
	public Identity selectIdentity();
	
	/**
	 * <p><b>Selects a {@link fr.sos.witchhunt.model.players.TurnAction turn action}.</b></p>
	 * <p>A defensive and careful strategy will tend to avoid hunting.</p>
	 * <p>To make this choice, requires knowledge of the calling player's identity, hand and ability to play a {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}.</p>
	 * @param identity The calling {@link fr.sos.witchhunt.model.players.CPUPlayer cpu-controlled player}'s {@link Player#getIdentity() identity}.
	 * @param myHand The calling {@link fr.sos.witchhunt.model.players.CPUPlayer cpu-controlled player}'s {@link Player#getHand() hand}.
	 * @param canHunt A boolean indicating whether the calling {@link fr.sos.witchhunt.model.players.CPUPlayer cpu-controlled player} can or can't hunt.
	 * @return Either <i>{@link fr.sos.witchhunt.model.players.TurnAction#ACCUSE ACCUSE}</i> or <i>{@link fr.sos.witchhunt.model.players.TurnAction#HUNT HUNT}</i>.
	 * @see fr.sos.witchhunt.model.players.TurnAction TurnAction
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseTurnAction() CPUPlayer::chooseTurnAction()
	 */
	public TurnAction selectTurnAction(Identity identity, RumourCardsPile myHand,boolean canHunt);
	
	/**
	 * <p><b>Selects an {@link fr.sos.witchhunt.model.players.DefenseAction answer to an accusation}.</b></p>
	 * <p>To make this choice, requires knowledge of the calling player's identity, hand and ability to play a {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect}.</p>
	 * @param myIdentity The calling {@link fr.sos.witchhunt.model.players.CPUPlayer cpu-controlled player}'s {@link Player#getIdentity() identity}.
	 * @param myHand The calling {@link fr.sos.witchhunt.model.players.CPUPlayer cpu-controlled player}'s {@link Player#getHand() hand}.
	 * @param canWitch A boolean indicating whether the calling {@link fr.sos.witchhunt.model.players.CPUPlayer cpu-controlled player} can or can't play a {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect}.
	 * @return Either <i>{@link fr.sos.witchhunt.model.players.DefenseAction#REVEAL REVEAL}</i> or <i>{@link fr.sos.witchhunt.model.players.DefenseAction#WITCH WITCH}</i>.
	 * @see fr.sos.witchhunt.model.players.DefenseAction DefenseAction
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#defend() Player::defend()
	 */
	public DefenseAction selectDefenseAction(Identity myIdentity,RumourCardsPile myHand,boolean canWitch);
	
	/**
	 * <p><b>Selects a {@link fr.sos.witchhunt.model.players.Player player} to accuse within a list.</b></p>
	 * @param accusablePlayersList The list within which a player must be accused
	 * @return The chosen target
	 * @see fr.sos.witchhunt.model.players.Player#accuse(Player)
	 */
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList);

	/**
	 * <p><b>Selects a {@link fr.sos.witchhunt.model.players.Player player} to target with a {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}.</b></p>
	 * @param eligiblePlayers The list within which a player must be targetted
	 * @return The chosen target
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseHuntedTarget(List)
	 */
	public default Player selectTarget(List<Player> eligiblePlayers) {
		return selectPlayerToAccuse(eligiblePlayers);
	}
	
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} valued for its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect} within
	 * a given {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}.</b>
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards} within which the choice can be made.
	 * @see CPUPlayer#selectWitchCard()
	 * @see CardValue
	 * @see CardValueMap
	 * @return A {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} from the player's hand chosen for its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect} in particular.
	 */
	public RumourCard selectWitchCard(RumourCardsPile rcp);
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} valued for its {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} within
	 * a given {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}.</b>
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards} within which the choice can be made.
	 * @see CPUPlayer#selectHuntCard()
	 * @see CardValue
	 * @see CardValueMap
	 * @return A {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} from the player's hand chosen for its {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} in particular.
	 */
	public RumourCard selectHuntCard(RumourCardsPile rcp);
	
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} to {@link fr.sos.witchhunt.model.players.Player#discard(RumourCard) discard} within a given
	 * a given {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}.</b>
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards} within which a card must be selected to be {@link fr.sos.witchhunt.model.players.Player#discard(RumourCard) discarded}.
	 * @return The selected {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}, supposed to be the worst one according to the strategy
	 * @see fr.sos.witchhunt.model.players.Player#discard(RumourCard) discard
	 * @see CPUStrategy#selectWorstCard(RumourCardsPile)
	 * @see CardValue
	 * @see CardValueMap
	 */
	public RumourCard selectCardToDiscard(RumourCardsPile rcp);
	
	/**
	 * <p><b>Selects a {@link fr.sos.witchhunt.model.players.Player player} to {@link fr.sos.witchhunt.model.players.Player#takeNextTurn() take the next turn}.</b></p>
	 * <p>Most strategies will avoid giving the next turn to the {@link fr.sos.witchhunt.model.flow.Tabletop#getLeadingPlayers() leading players}.</p>
	 * @param list A list of {@link fr.sos.witchhunt.model.players.Player players} within which the next player has to be selected.
	 * @return The chosen player to {@link fr.sos.witchhunt.model.players.Player#takeNextTurn() take the next turn}.
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseNextPlayer() CPUPlayer::chooseNextPlayer()
	 */
	public Player selectNextPlayer(List<Player> list);
	
	/**
	 * <b>Selects the best {@link fr.sos.witchhunt.model.cards.RumourCard card} according to the strategy 
	 * within a given {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}.</b>
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards} within which the best card has to be selected.
	 * @param seeUnrevealedCards If this boolean is true, the strategy will be able to access the properties of unrevealed cards as well.
	 * @return The best {@link fr.sos.witchhunt.model.cards.RumourCard card} according to the strategy.
	 * @see CPUStrategy#selectBestCard(RumourCardsPile, boolean)
	 * @see CardValue
	 * @see CardValueMap
	 */
	public RumourCard selectBestCard(RumourCardsPile rcp, boolean seeUnrevealedCards);
	
	/**
	 * <p><b>Chooses an {@link fr.sos.witchhunt.model.players.DefenseAction action} between revealing your identity and discard(RumourCard) discarding a Rumour card from your hand,
	 * {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#revealOrDiscard(Identity, RumourCardsPile) based on the chosen strategy}.</b></p>
	 * <p>To make this choice, requires knowledge of the calling player's identity and hand.</p>
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#revealOrDiscard() CPUPlayer::revealOrDiscard() 
	 * @see DefenseAction
	 * @see fr.sos.witchhunt.model.cards.DuckingStool DuckingStool
	 * @return Either {@link fr.sos.witchhunt.model.players.DefenseAction DefenseAction#REVEAL} or {@link fr.sos.witchhunt.model.players.DefenseAction DefenseAction#DISCARD}
	 */
	public DefenseAction revealOrDiscard(Identity identity,RumourCardsPile rcp);
	
	/**
	 * <p><b>Updates the behavior the strategy according to the player's {@link fr.sos.witchhunt.model.players.Player#getIdentity() identity}</b>, 
	 * {@link fr.sos.witchhunt.model.players.Player#isRevealed() reveal status} and {@link fr.sos.witchhunt.model.players.Player#getHand() hand}.
	 * @param amIRevealed The {@link fr.sos.witchhunt.model.players.CPUPlayer#isRevealed() reveal status} of the calling player
	 * @param myIdentity The {@link fr.sos.witchhunt.model.players.CPUPlayer#getIdentity() calling player's identity}.
	 * @param myHand The calling player's {@link fr.sos.witchhunt.model.players.Player#getHand() hand}.
	 */
	public void updateBehavior(boolean amIRevealed, Identity myIdentity, RumourCardsPile myHand);

	public String toString();

}
