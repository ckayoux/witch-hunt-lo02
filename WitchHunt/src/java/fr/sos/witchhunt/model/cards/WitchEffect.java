package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>A <i>Witch?</i> effect.</b></p>
 * <p>Supposed to exist only within a {@link RumourCard Rumour card} (relation of composition).</p>
 * <p><i>abstract</i>, instantiated anonymously with a redefinition of the {@link #perform()} method. The {@link #isAllowed()} method can be overriden too if needed.</p> 
 */
public abstract class WitchEffect extends Effect {
	/**
	 * <p>Plural <i>Witch?</i> effects make you {@link #takeNextTurn()} take the next turn.</p> 
	 * <p>Makes sure the {@link #getDescription() description} and {@link #getValue() default value} of these effects is shared.</p>
	 * <p><b>The {@link #perform()} method must be redefined with {@link #takeNextTurn()} only.</b></p>
	 */
	public WitchEffect () {
		super("Take next turn.",1);
	}
	/**
	 * <p><b>Instantiates a <i>Witch?</i> effect with its {@link #description} and its {@link #value integer default value}.</b></p>
	 * <p>{@link #perform()} must, and {@link #isAllowed()} can, be redefined.</p>
	 * @param desc This effect's {@link #description}. Each "newline" character can be followed by a "/+/" sequence to build multiline, dynamically indented, paragraphs. 
	 * @param value This effect's {@link #value} default value. Corresponds to the value given by a {@link fr.sos.witchhunt.model.players.CPUPlayer CPU Players'} {@link fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy strategies} to this effect in its initial state. 
	 * @see #description
	 * @see #value
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue#getWitchValue() CardValue::getWitchValue()
	 */
	public WitchEffect (String desc,int value) {
		super(desc,value);
	}
	
	/**
	 * @return The player triggering this <i>Witch?</i> effect, that is to say {@link fr.sos.witchhunt.controller.Tabletop#getAccusedPlayer() the currently accused player}.
	 * @see fr.sos.witchhunt.controller.Tabletop#getAccusedPlayer() Tabletop::getAccusedPlayer()
	 */
	@Override
	protected Player getMyself() {
		return Tabletop.getInstance().getAccusedPlayer();
	}

	/**
	 * @return The player triggering this <i>Witch?</i> effect, that is to say, the most often, {@link fr.sos.witchhunt.controller.Tabletop#getAccusator() the accusator}. Can be redefined.
	 * @see fr.sos.witchhunt.controller.Tabletop#getAccusator() Tabletop::getAccusator()
	 */
	@Override
	protected Player getTarget() {
		return Tabletop.getInstance().getAccusator();
	}
}
