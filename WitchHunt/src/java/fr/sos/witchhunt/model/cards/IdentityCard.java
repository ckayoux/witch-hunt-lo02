package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.Identity;

/**
 * <b>Class representing an Identity cards.</b>
 * <p>Identity cards are instantiated at a {@link fr.sos.witchhunt.model.players.Player player}'s instantiation and kept for the duration of the whole game.</p>
 * <p>It comes with an {@link fr.sos.witchhunt.model.Identity identity} (Villager or Witch) and by its reveal status.
 * The cards' identity is the same as its owner's and is {@link fr.sos.witchhunt.model.Resettable#reset() reset} at the end of each round.
 * If it is revealed, we consider the owner's identity has been revealed.</p>
 * IdentityCard extends {@link fr.sos.witchhunt.model.cards.Card Card} : 
 * {@inheritDoc}
 * @see fr.sos.witchhunt.model.Identity Identity
 * @see fr.sos.witchhunt.model.Resettable Resettable
 * @see fr.sos.witchhunt.model.players.Player#isRevealed() Player::isRevealed()
 * @see fr.sos.witchhunt.model.players.Player#revealIdentity() Player::revealIdentity()
 * @see fr.sos.witchhunt.model.players.Player#reset() Player::reset()
 */
public class IdentityCard extends Card {
	/**
	 * The {@link fr.sos.witchhunt.model.Identity identity} associated with this card (either <i>Witch</i> or <i>Villager</i>).
	 */
	private Identity chosenIdentity;
	
	
	/**
	 * <p>IdentityCards are reset by their owners when they are requested to reset themselves (at the end of each round).</p>
	 * <p>When reset, the card's reveal status becomes <i>unrevealed</i> and the identity associated with it is reinitialized.</p>
	 * @see fr.sos.witchhunt.model.flow.Round
	 * @see fr.sos.witchhunt.model.players.Player#reset()
	 * @see fr.sos.witchhunt.model.Resettable
	 */
	@Override
	public void reset() {
		super.reset();
		this.chosenIdentity=null;
	}
	
	public void setChosenIdentity(Identity i) {
		this.chosenIdentity = i;
	}
}
