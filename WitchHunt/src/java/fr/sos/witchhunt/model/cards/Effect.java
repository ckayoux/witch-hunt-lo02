package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>Defines behaviors that are common between {@link WitchEffect Witch? effects} and {@link HuntEffect Hunt! effects}.</b></p>
 * <p>A {@link RumourCard Rumour card} has one {@link WitchEffect Witch? effect} and one {@link HuntEffect Hunt! effect} (relation of composition).</p>
 * <p>Effects are supposed to be instantiated as anonymous classes with a redefinition of the {@link #perform()} for each type of {@link RumourCard Rumour card}.</p>
 * 
 * <p>An effect has a description (used for display),
 * an integer value (used by {@link fr.sos.witchhunt.model.players.CPUPlayer CPU Players'} {@link fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy strategies} to make their decisions),
 * a {@link #isAllowed() method checking whether the effect is playable in the current situation or not},
 * and a {@link #perform() method defining the effects behavior when it is triggered}.</p>
 * <p>Also defines useful methods which are often used by both {@link WitchEffect Witch? effects} and {@link HuntEffect Hunt! effects}, like
 * {@link #takeNextTurn() taking the next turn}, {@link #chooseNextPlayer() choosing the next player}, or {@link #getMyself()} getting the reference of
 * the Player triggering this effect.</p>
 */
public abstract class Effect {
	/**
	 * <p><b>The effect's description, which composes the card's display.</b></p>
	 * <p>The "/+/" sequence can be added before all lines (but the first one) to make multiline dynamically indented paragraphs.</p>
	 * @see fr.sos.witchhunt.controller.DisplayMediator#displayWitchEffect(RumourCard) DisplayMediator::displayWitchEffect(RumourCard)
	 * @see fr.sos.witchhunt.controller.DisplayMediator#displayHuntEffect(RumourCard) DisplayMediator::displayHuntEffect(RumourCard)
	 */
	private String description;
	/**
	 * <p>The effect's default integer value. Composes its {@link RumourCard#getDefaultValue() Rumour card's default value}.</p>
	 * <p>Used by {@link fr.sos.witchhunt.model.players.CPUPlayer CPU Players'} {@link fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy strategies} to make their decisions.
	 * @see RumourCard#getDefaultValue()
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue CardValue
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy CPUStrategy
	 */
	private int value;
	
	/*public Effect () {
		value=1;
		description="";
	}*/
	
	/**
	 * <b>Instantiates an extension of Effect with its {@link #description} and its {@link #value integer value}.</b>
	 * @param desc This effect's {@link #description}. Each "newline" character can be followed by a "/+/" sequence to build multiline, dynamically indented, paragraphs. 
	 * @param value This effect's {@link #value} default value. Corresponds to the value given by a {@link fr.sos.witchhunt.model.players.CPUPlayer CPU Players'} {@link fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy strategies} to this effect in its initial state. 
	 * @see #description
	 * @see #value
	 */
	public Effect (String desc,int value) {
		this.value=value;
		this.description=desc;
	}
	
	/**
	 * <p><b>Must be redefined by children classes to define the effect's behavior when it is triggered.</b></p>
	 */
	public abstract void perform();
	
	/**
	 * <p>Most effects are playable in any situation.</p>
	 * <p><b>However, some, like {@link PetNewt Pet Newt's Hunt! effect}, can only be played at some conditions.
	 * Any {@link RumourCard Rumour card} that is playable only at some conditions should redefine this method.</b></p>
	 * @return <i>true</i> if the effect is currently allowed (default), <i>false</i> if the conditions are not met (define conditions by overriding).
	 * @see PetNewt Pet Newt's Hunt! effect can be triggered only if there is at least one player with a {@link RumourCard#isRevealed() revealed Rumour card} in their hand.
	 */
	public boolean isAllowed() {
		return true; //has to be redefined for some cards
	}
	
	/**
	 * <b>Makes {@link #getMyself() the player triggering this effect} take the next turn.</b>
	 * @see #getMyself()
	 * @see fr.sos.witchhunt.model.players.Player#takeNextTurn() Player::takeNextTurn()
	 */
	protected void takeNextTurn() {
		getMyself().takeNextTurn();
	}
	
	/**
	 * <b>Makes {@link #getMyself() the player triggering this effect} choose an adversary to play the next turn.</b>
	 * @see #getMyself()
	 * @see fr.sos.witchhunt.model.players.Player#chooseNextPlayer() Player::chooseNextPlayer()
	 */
	protected Player chooseNextPlayer() {
		Player nextPlayer = getMyself().chooseNextPlayer();
		nextPlayer.takeNextTurn();;
		return nextPlayer;
	}
	
	//GETTERS
	public String getDescription() {
		return description;
	}
	public int getValue() {
		return value;
	}
	
	/**
	 * @return <b>The player triggering this effect</b> : for {@link WitchEffect}, should return the {@link fr.sos.witchhunt.model.flow.Tabletop#getAccusedPlayer() accused player}, whereas for {@link HuntEffect}, should return the {@link fr.sos.witchhunt.model.flow.Tabletop#getHunter() the hunting player}.
	 */
	protected abstract Player getMyself();
	/**
	 * @return <b>The player targetted by this effect</b> : for most {@link WitchEffect Witch? effects}, should return the {@link fr.sos.witchhunt.model.flow.Tabletop#getAccusator() accusator}, whereas for most {@link HuntEffect Hunt! effects}, should return the {@link fr.sos.witchhunt.model.flow.Tabletop#getHuntedPlayer() the hunted player}.
	 */
	protected abstract Player getTarget();
}
