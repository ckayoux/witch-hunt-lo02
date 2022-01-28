package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>A <i>Hunt!</i> effect.</b></p>
 * <p>Supposed to exist only within a {@link RumourCard Rumour card} (relation of composition).</p>
 * <p><i>abstract</i>, instantiated anonymously with a redefinition of the {@link #perform()} method. The {@link #isAllowed()} method can be overriden too if needed.</p> 
 */
public abstract class HuntEffect extends Effect {
	/**
	 * <p>Plural <i>Hunt!</i> effects make you {@link #chooseNextPlayer()} choose the next player.</p> 
	 * <p>Makes sure the {@link #getDescription() description} and {@link #getValue() default value} of these effects is shared.</p>
	 * <p><b>The {@link #perform()} method must be redefined with {@link #chooseNextPlayer()} only.</b></p>
	 */
	public HuntEffect () {
		super("Choose next player.",1);
	}
	/**
	 * <p><b>Instantiates a <i>Hunt!</i> effect with its {@link #description} and its {@link #value integer default value}.</b></p>
	 * <p>{@link #perform()} must, and {@link #isAllowed()} can, be redefined.</p>
	 * @param desc This effect's {@link #description}. Each "newline" character can be followed by a "/+/" sequence to build multiline, dynamically indented, paragraphs. 
	 * @param value This effect's {@link #value} default value. Corresponds to the value given by a {@link fr.sos.witchhunt.model.players.CPUPlayer CPU Players'} {@link fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy strategies} to this effect in its initial state. 
	 * @see #description
	 * @see #value
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue#getHuntValue() CardValue::getHuntValue()
	 */
	public HuntEffect (String desc,int value) {
		super(desc,value);
	}
	
	/**
	 * @return The player triggering this <i>Hunt!</i> effect, that is to say {@link fr.sos.witchhunt.model.flow.Tabletop#getHunter() the current hunting player}.
	 * @see fr.sos.witchhunt.model.flow.Tabletop#getHunter() Tabletop::getHunter()
	 */
	@Override
	protected Player getMyself() {
		return Tabletop.getInstance().getHunter();
	}
	/**
	 * Useful only for a few <i>Hunt!</i> effects.
	 * @return The player targetted by this <i>Hunt!</i> effect, that is to say the {@link fr.sos.witchhunt.model.flow.Tabletop#getHuntedPlayer() the current hunted player}.
	 * @see fr.sos.witchhunt.model.flow.Tabletop#getHuntedPlayer() Tabletop::getHuntedPlayer()
	 */
	@Override
	protected Player getTarget() {
		return Tabletop.getInstance().getHuntedPlayer();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 *  <b>Also designates the chosen next player as the {@link #getTarget() hunted target}.</b>
	 */
	@Override
	public Player chooseNextPlayer() {
		Player nextPlayer = getMyself().chooseNextPlayer();
		nextPlayer.beHunted();
		nextPlayer.takeNextTurn();
		return nextPlayer;
	}
	
}
