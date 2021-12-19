package fr.sos.witchhunt.model.players;

import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.cpustrategies.DefensiveStrategy;
import fr.sos.witchhunt.model.players.cpustrategies.GropingStrategy;
import fr.sos.witchhunt.model.players.cpustrategies.OffensiveStrategy;
import fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy;

/**
 * <p><b>This class represents a CPU-controlled player.</b></p>
 * <p>It overrides all of {@link Player}'s abstract methods.</p>
 * <p>All choices are made using a {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy strategy}, which behaves as an artificial intelligence with a behavior that is suited for specific situations.
 * CPU-controlled players regularly choose one Strategy among others depending on the situation (their position in the ranking, the leading player's score, their number of cards, their score...).
 * This is an implementation of the {@link https://refactoring.guru/fr/design-patterns/strategy Strategy design pattern}.</p>
 * @see Player
 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy
 * @see #chooseStrategy()
 */
public final class CPUPlayer extends Player {
	/**
	 * <p><b>The current {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy strategy} chosen by the CPUPlayer.</b></p>
	 * <p>Updated by {@link #chooseStrategy()}, which is called regularly (on {@link #playTurn() turn start}, on {@link #reset() round end}, on {@link #accuse(Player) accusation}, when {@link #chooseDefenseAction() defending}...</p>
	 * <p>The initial strategy, chosen when the match starts, is {@link fr.sos.witchhunt.model.players.cpustrategies.GropingStrategy GropingStrategy}.</p>
	 * <p>A strategy is an instance of a class implementing {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy PlayStrategy}, an interface specifying all sorts of choices an AI must be capable to make.</p>
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy
	 */
	private PlayStrategy chosenStrategy=new GropingStrategy();
	/**
	 * @deprecated This attribute was used to notify the view of a change of strategy. The notifications are still sent but the view does not take it into account anymore.
	 * @see #chooseStrategy() 
	 */
	@Deprecated
	private PlayStrategy oldStrategy = chosenStrategy;
	/**
	 * <p><b>A reference to a player known as a {@link fr.sos.witchhunt.model.Identity#WITCH Witch}.</b></p>
	 * <p>When using the {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} of card {@link fr.sos.witchhunt.model.cards.TheInquisition The Inquisiton},
	 * CPU-controlled players {@link #lookAtPlayersIdentity(Player) look at their target's identity} and will remember it if it happens to be <i>Witch</i>.
	 * That known witch will then become a high-priority target when {@link #choosePlayerToAccuse() choosing a player to accuse} or {@link #chooseTarget(List) choosing a player to hunt}.</p>
	 * @see #lookAtPlayersIdentity(Player)
	 * @see #choosePlayerToAccuse()
	 * @see #chooseTarget(List)
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see fr.sos.witchhunt.model.cards.TheInquisition TheInquisiton
	 */
	private Player knownWitch=null;
	
	/**
	 * <p><b>A short delay is added between all actions performed by CPU Players to let the users keep the track of what happens during their turns.</b></p>
	 * <p>Expressed in milliseconds.</p>
	 */
	private int actionsDelay = 750;
	
	/**
	 * CPU players' default name is determined by the number of existing CPU Players at instanciation.
	 * @param nthCpuPlayer This CPUPlayer will be the nth one
	 * @see Player#Player(int)
	 */
	public CPUPlayer(int id, int nthCpuPlayer) {
		super(id);
		this.name="CPU "+Integer.toString(nthCpuPlayer);
	}
	
	/**
	 * <b>{@link #chooseStrategy() Chooses a strategy}, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) updates its behavior}</b>
	 * and start playing a turn.</b>
	 * @see #chooseStrategy()
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) PlayStrategy::updateBehavior(boolean, Identity, RumourCardsPile)
	 * @see #chooseTurnAction()
	 * @see Player#playTurn()
	 */
	@Override
	public void playTurn() {
		this.delayGame(2*actionsDelay);
		this.chooseStrategy();
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		super.playTurn();
	}
	
	/**
	 * <b>{@link Player#reset() Resets the player} and {@link #chooseStrategy() lets them choose a strategy}.</b>
	 * The chosen strategy holds sway on the next round's chosen identity, this is why it is important to choose a strategy even at the end of a round.
	 * @see Player#reset()
	 * @see #chooseStrategy()
	 */
	@Override
	public void reset() {
		super.reset();
		this.chooseStrategy();
	}
	
	/**
	 * <b>Chooses the player's identity for the current {@link fr.sos.witchhunt.controller.Round round}, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectIdentity() based on the chosen strategy}.</b>
	 * <p>Also requests for the display of a special screen notifying the view that the CPUPlayer has chosen their identity.</p>
	 * @see Player#chooseIdentity()
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectIdentity() PlayStrategy::selectIdentity()
	 */
	@Override
	public final void chooseIdentity() {
		this.identity = chosenStrategy.selectIdentity();
		this.identityCard.setChosenIdentity(this.identity);
		requestHasChosenIdentityScreen();
		this.delayGame(actionsDelay);
	}
	
	/**
	 * <p><b>Chooses a player to {@link #accuse(Player) accuse}, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectPlayerToAccuse(List) based on the chosen strategy}.</b></p>
	 * <p>If there is a player who is {@link #knownWitch known as a witch}, accuse them directly instead.</p>
	 * @see Player#choosePlayerToAccuse()
	 * @see #knownWitch
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectPlayerToAccuse(List) PlayStrategy::selectPlayerToAccuse(List)
	 */
	@Override
	protected Player choosePlayerToAccuse() {
		this.delayGame(actionsDelay);
		if(knownWitch==null) return chosenStrategy.selectPlayerToAccuse(getAccusablePlayers());
		else if(!knownWitch.isRevealed()) {
			Player output = knownWitch;
			this.knownWitch=null;
			return output;
		}
		else {
			this.knownWitch=null;
			return chosenStrategy.selectPlayerToAccuse(getAccusablePlayers());
		}
	}
	
	/**
	 * <p><b>Chooses a target {@link fr.sos.witchhunt.model.cards.HuntEffect for a Hunt! effect} among a list of eligible players {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectTarget(List) based on the chosen strategy}.</b></p>
	 * <p>If there is a player who is {@link #knownWitch known as a witch} in the list of eligible players, targets them directly instead.</p>
	 * @see Player#chooseTarget(List)
	 * @see #knownWitch
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectTarget(List) PlayStrategy::selectTarget(List)
	 */
	@Override
	public Player chooseTarget(List<Player> eligiblePlayers) {
		this.delayGame(actionsDelay);
		if(knownWitch==null||!eligiblePlayers.contains(knownWitch)) return chosenStrategy.selectTarget(eligiblePlayers);
		else return knownWitch;
		
	}

	/**
	 * <b>Elects the player taking the next turn, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectNextPlayer(List) based on the chosen strategy}.</b>
	 * @see Player#chooseNextPlayer()
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectNextPlayer(List) PlayStrategy::selectNextPlayer(List)
	 */
	@Override
	public Player chooseNextPlayer() {
		this.delayGame(actionsDelay);
		return chosenStrategy.selectNextPlayer(Tabletop.getInstance().getActivePlayersList().stream().filter(p->p!=this).toList());
	}
	
	/**
	 * <b>Selects an action to perform on the player's turn, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectTurnAction(Identity, RumourCardsPile, boolean) based on the chosen strategy}.</b>
	 * @see TurnAction
	 * @see Player#chooseTurnAction()
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectTurnAction(Identity, RumourCardsPile, boolean) PlayStrategy#selectTurnAction(Identity, RumourCardsPile, boolean)
	 */
	@Override
	public TurnAction chooseTurnAction() {
		this.delayGame(actionsDelay);
		return chosenStrategy.selectTurnAction(this.identity,this.hand,this.canHunt());
	}

	/**
	 * <p><b>Selects an action to perform when accused or targetted by a {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectDefenseAction(Identity, RumourCardsPile, boolean) based on the chosen strategy}.</b>
	 * @see DefenseAction
	 * @see Player#chooseDefenseAction()
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectDefenseAction(Identity, RumourCardsPile, boolean) PlayStrategy#selectDefenseAction(Identity, RumourCardsPile, boolean)
	 */
	@Override
	public DefenseAction chooseDefenseAction() {
		this.delayGame(actionsDelay);
		this.chooseStrategy();
		//must initialize Tabletop's hunter with canHunt, as the player will look at its playable hunt cards to make their decision
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		return chosenStrategy.selectDefenseAction(this.getIdentity(),this.hand,this.canWitch());
	}

	/**
	 * <p><b>When targetted by a {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}, {@link #chooseStrategy() chooses a strategy} and {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) updates its behavior}.</b></p>
	 * <p>This allows the CPU Player to adapt their behavior to their situation when they are targetted by a hunt effect, as their situation can have changed between their turn and that attack.</p>
	 * @see Player#beHunted()
	 * @see #chooseStrategy()
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) PlayStrategy::updateBehavior(boolean, Identity, RumourCardsPile)
	 */
	@Override
	public void beHunted() {
		this.delayGame(actionsDelay);
		this.chooseStrategy();
		super.beHunted();
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
	}
	
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} valued for its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect}, 
	 * {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectWitchCard(RumourCardsPile) based on the chosen strategy}.</b>
	 * @see Player#selectWitchCard()
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectWitchCard(RumourCardsPile) PlayStrategy::selectWitchCard(RumourCardsPile)
	 * @return A {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} from the player's hand chosen for its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect} in particular.
	 */
	@Override
	public RumourCard selectWitchCard() {
		this.delayGame(actionsDelay);
		return this.chosenStrategy.selectWitchCard(this.hand.getPlayableWitchSubpile());
	}

	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} valued for its {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}, 
	 * {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectHuntCard(RumourCardsPile) based on the chosen strategy}.</b>
	 * @see Player#selectHuntCard()
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectHuntCard(RumourCardsPile) PlayStrategy::selectHuntCard(RumourCardsPile)
	 * @return A {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} from the player's hand chosen for its {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} in particular.
	 */
	@Override
	public RumourCard selectHuntCard() {
		this.delayGame(actionsDelay);
		return this.chosenStrategy.selectHuntCard(this.hand.getPlayableHuntSubpile());
	}
	
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} to {@link #discard()}, 
	 * {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectCardToDiscard(RumourCardsPile) based on the chosen strategy}.</b>
	 * The {@link #chosenStrategy} is beforehand {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) updated} in order to have the most accurate possible behavior.
	 * @see Player#selectCardToDiscard(RumourCardsPile)
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectCardToDiscard(RumourCardsPile) PlayStrategy::selectCardToDiscard(RumourCardsPile)
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) PlayStrategy::updateBehavior(boolean, Identity, RumourCardsPile)
 	 * @param in A {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}.
	 * @return The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} that was chosen to be {@link #discard(RumourCard) discarded}.
	 */
	@Override
	public RumourCard selectCardToDiscard(RumourCardsPile in) {
		this.delayGame(actionsDelay);
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		if(!in.getUnrevealedSubpile().isEmpty()) {
			return chosenStrategy.selectCardToDiscard(in.getUnrevealedSubpile());
		}
		else return chosenStrategy.selectCardToDiscard(in);
	}
	

	/**
	 * <p><b>Chooses the best card (revealed or not) within a pile of Rumour cards, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectBestCard(RumourCardsPile, boolean) based on the chosen strategy}.</b></p>
	 * <p>The {@link #chosenStrategy} is beforehand {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) updated} in order to have the most accurate possible behavior.</p>
	 * <p>It is possible to keep secret the properties of unrevealed cards to the {@link #chosenStrategy}.</p>
	 * @param from The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards} within which a card must be chosen.
	 * @param seeUnrevealedCards If this boolean is <i>true</i>, the {@link #chosenStrategy} will be able to access the information of unrevealed cards as well to make their choice.
	 * @return The best {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} within the given pile, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectBestCard(RumourCardsPile, boolean) according to the chosen strategy}.
	 * @see Player#chooseAnyCard(RumourCardsPile, boolean)
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectBestCard(RumourCardsPile, boolean) PlayStrategy::selectBestCard(RumourCardsPile, boolean)
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) PlayStrategy::updateBehavior(boolean, Identity, RumourCardsPile)
	 */
	@Override
	public RumourCard chooseAnyCard(RumourCardsPile rcp, boolean seeUnrevealedCards) {
		this.delayGame(actionsDelay);
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		if(targetPileContainsCards(rcp)) {
			return chosenStrategy.selectBestCard(rcp,seeUnrevealedCards);
		}
		else return null;
	}
	
	/**
	 * <b>Chooses the best revealed card within a pile of cards, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectBestCard(RumourCardsPile, boolean) based on the chosen strategy}.</b>
	 * The {@link #chosenStrategy} is beforehand {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) updated} in order to have the most accurate possible behavior.
	 * @param from The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards} within which a revealed card must be chosen.
	 * @return The best revealed {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} within this pile, {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#selectBestCard(RumourCardsPile, boolean) according to the chosen strategy}.
	 * @see Player#chooseRevealedCard(RumourCardsPile)
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) PlayStrategy::updateBehavior(boolean, Identity, RumourCardsPile)
	 */
	@Override
	public RumourCard chooseRevealedCard(RumourCardsPile from) {
		this.delayGame(actionsDelay);
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		if(targetPileContainsCards(from.getRevealedSubpile())) {
			return chosenStrategy.selectBestCard(from.getRevealedSubpile(),false);
		}
		else return null;
	}
	
	/**
	 * <p><b>Sneakily looks at a given player's {@link #identity}.</b></p>
	 * <p>Called when using card {@link fr.sos.witchhunt.model.cards.TheInquisition The Inquisiton}.</p>
	 * <p>CPU-controlled players will {@link #knownWitch remember the target's reference} if their identity happens to be <i>Witch</i>, making them a high-priority target to {@link #choosePlayerToAccuse() accuse} or to {@link #chooseTarget(List) hunt}.</p>
	 * @param target The player of which the identity is going to be looked at.
	 * @return The targetted player's identity
	 * @see Player#lookAtPlayersIdentity(Player)
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see fr.sos.witchhunt.model.cards.TheInquisition TheInquisiton
	 * @see #knownWitch
	 * @see #choosePlayerToAccuse()
	 * @see #chooseTarget(List)
	 */
	@Override
	public Identity lookAtPlayersIdentity(Player target) {
		this.delayGame(actionsDelay);
		Identity id = super.lookAtPlayersIdentity(target);
		if(id==Identity.WITCH) this.knownWitch=target;
		return id;
	}

	/**
	 * <p><b>Chooses an {@link DefenseAction action} between {@link #revealIdentity() revealing your identity} and {@link #discard(RumourCard) discarding a Rumour card from your hand},
	 * {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#revealOrDiscard(Identity, RumourCardsPile) based on the chosen strategy}.</b></p>
	 * <p>Called when targetted by the {@link fr.sos.witchhunt.model.cards.DuckingStool Ducking Stool} card's Hunt! effect.</p>
	 * <p>If the player has no cards to discard, or if they are already revealed, they will directly choose their only option and will request the view to display suitable screens.</p>
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#revealOrDiscard(Identity, RumourCardsPile) PlayStrategy::revealOrDiscard(Identity, RumourCardsPile) 
	 * @see DefenseAction
	 * @see #revealIdentity()
	 * @see #discard(RumourCard)
	 * @see fr.sos.witchhunt.model.cards.DuckingStool DuckingStool
	 * @return Either {@link DefenseAction#REVEAL} or {@link DefenseAction#DISCARD}
	 */
	@Override
	public DefenseAction revealOrDiscard() {
		this.delayGame(actionsDelay);
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		if(this.hasRumourCards()&&!this.isRevealed()) {
			return chosenStrategy.revealOrDiscard(this.getIdentity(),this.getHand());
		}
		else if(!this.hasRumourCards()&&!this.isRevealed()) {
			requestNoCardsScreen();
			requestForcedToRevealScreen();
			return DefenseAction.REVEAL;
		}
		else { //cannot be chosen by ducking stool if is revealed and has no rumour cards
			return DefenseAction.DISCARD;
		}
	}
	
	/**
	 * Simulates a delay after winning the round.
	 * @see Player#winRound()
	 */
	@Override
	public void winRound() {
		super.winRound();
		this.delayGame(2*actionsDelay);
	}
	
	/**
	 * Simulates a delay after stealing a card
	 * @see Player#requestStealCardFromScreen(Player)
	 */
	@Override
	public void takeRumourCard(RumourCard rc,Player stolenPlayer) {
		super.takeRumourCard(rc, stolenPlayer);
		this.delayGame(actionsDelay);
	}

	/**
	 * <p><b>Chooses a {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy strategy} based on the player's current situation.</b><p>
	 * 
	 * <p>If the player is in relatively good position (score close to 5, is leading or has many cards), the player will opt for an {@link fr.sos.witchhunt.model.players.cpustrategies.OffensiveStrategy offensive strategy},
	 * allowing them to take more risks, {@link #hunt()} more often, to focus the weakest players, to value the cards with offensive effects and causes them to be more likely to choose 
	 * to play as {@link fr.sos.witchhunt.model.Identity#VILLAGER villagers}.</p>
	 * 
	 * <p>If the {@link fr.sos.witchhunt.controller.Tabletop#gameIsTied() game is tied}, if the leading players are far above in score,
	 *  if this player has no cards left, if this player plays as a {@link fr.sos.witchhunt.model.Identity#WITCH witch} and is not close to victory,
	 * they will opt for a {@link fr.sos.witchhunt.model.players.cpustrategies.DefensiveStrategy defensive strategy},
	 * making them more likely to play as witches, stingier on cards and valuing cards based on their {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect} above all,
	 * focus the leading players (or players with no cards left), and more likely to choose to play as {@link fr.sos.witchhunt.model.Identity#WITCH witches}.</p>
	 * 
	 * <p>By default at game start, or if they are not in a situation where they can choose to play offensively or defensively,
	 * the player will opt for a {@link fr.sos.witchhunt.model.players.cpustrategies.GropingStrategy groping strategy}, with which
	 * the chosen identity and the accused players are chosen randomly, the cards are considered for their overall value, and with which
	 * the player tries to keep their best cards for later, when he can choose a more defined strategy.</p>
	 * 
	 * <p>The {@link #chosenStrategy} will then be used for making every choice the CPU-controlled player has to make.</p>
	 * 
	 * <p>The chosen strategy's behavior can be {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) updated} to 
	 * actualize {@link fr.sos.witchhunt.model.players.cpustrategies.CardValue the value given to each card}.</p>
	 * 
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy PlayStrategy
	 * @see #chosenStrategy
	 * 
	 * @see fr.sos.witchhunt.model.players.cpustrategies.OffensiveStrategy OffensiveStrategy
	 * @see fr.sos.witchhunt.model.players.cpustrategies.DefensiveStrategy DefensiveStrategy
	 * @see fr.sos.witchhunt.model.players.cpustrategies.GropingStrategy GropingStrategy
	 * 
	 * @see Tabletop.getInstance().getRanking() Tabletop::getRanking()
	 * @see Tabletop.getInstance().getLeadingPlayers() Tabletop::getLeadingPlayers()
	 * @see Tabletop.getInstance().getLastPlayers() Tabletop::getLastPlayers()
	 * @see #getScore()
	 * @see #getIdentity()
	 * @see #getHand()
	 * 
	 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy#updateBehavior(boolean, Identity, RumourCardsPile) PlayStrategy:updateBehavior(boolean, Identity, RumourCardsPile)
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue CardValue
	 */
	public void chooseStrategy() {
		Player leadingPlayer = Tabletop.getInstance().getRanking().get(0);
		List<Player> lastPlayersList = Tabletop.getInstance().getLastPlayers();
		List<Player> leadingPlayersList = Tabletop.getInstance().getLeadingPlayers();
		
		if((this!=leadingPlayer)
			&&(Tabletop.getInstance().getLastPlayers().contains(this)
				|| (leadingPlayer.getScore()>=this.score+1 && this.score<=3 )
				||(this.getHand().isEmpty()&&this.getIdentity()!=null)
				||Tabletop.getInstance().gameIsTied()
				||(this.identity==Identity.WITCH&&this.score<=3&&this.getHand().getCardsCount()<=4)
			)){
			this.chosenStrategy = new DefensiveStrategy();
		}	
		else if(Tabletop.getInstance().getLeadingPlayers().contains(this)
				||this.score >=3
				|| (this.hand.getCardsCount()>=2 && Math.random()<0.2)
				|| this.hand.getCardsCount()>=5 && this.score>=1) {
			this.chosenStrategy=new OffensiveStrategy();
		}
		else if(Tabletop.getInstance().getPlayersList().stream().filter(p->!lastPlayersList.contains(p)&&!leadingPlayersList.contains(p)).toList().contains(this)
				&&this.score<3)
				this.chosenStrategy=new GropingStrategy();
		else this.chosenStrategy=new DefensiveStrategy();
		if(chosenStrategy.getClass()!=oldStrategy.getClass()) displayMediator.displayStrategyChange(this,this.chosenStrategy); //no effect
		oldStrategy=chosenStrategy;
	}
	
}
