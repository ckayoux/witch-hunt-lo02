package fr.sos.witchhunt.model;

import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>Generic interface implemented by entities that visit <i>{@link Visitable visitables}</i> entities.</b></p> 
 * <p><i>Implemented in particular by the game's {@link fr.sos.witchhunt.model.flow.ScoreCounter ScoreCounter}, which updates its score table by visiting players each time their score changes.</i></p>
 * <p>Defining a specific method for each class implementing <i>{@link Visitable}</i> is recommended if all Visitors can be lead to visiting them. Otherwise, create a new interface.</p>
 * @see Visitable
 * @see fr.sos.witchhunt.model.flow.ScoreCounter
 */
public interface Visitor {
	/**
	 * <b>Visit of a <i>{@link Visitable}</i> entity whose class is unknown by the visitor.</b>
	 * @param v A reference to a <i>{@link Visitable}</i> entity
	 */
	void visit(Visitable  v);
	
	/**
	 * <b>Visit of a <i>{@link fr.sos.witchhunt.model.players.Player player}</i>.</b>
	 * @param p The instance of {@link fr.sos.witchhunt.model.players.Player Player} to visit.
	 * @see fr.sos.witchhunt.model.flow.ScoreCounter#visit(Player) ScoreCounter::visit(Player)
	 */
	void visit(Player p);
}
