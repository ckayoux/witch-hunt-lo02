package fr.sos.witchhunt.model;

/**
 * <p><b>Generic interface implemented by <i>visitable</i> entities ({@link https://refactoringguru.cn/design-patterns/visitor <i>Visitor</i> Design Pattern}).</b></p> 
 * <p>Player-entities in particular, implement this interface. They indeed accept a visit from the game's {@link fr.sos.witchhunt.model.flow.ScoreCounter ScoreCounter} each time their score changes</p>
 * @see Visitor
 * @see fr.sos.witchhunt.model.players.Player Player
 */
public interface Visitable {
	/**
	 * <p><b>Method calling for a given visitor's visit</b></p>
	 * <p>Called in particular when a player's score changes.</p>
	 * @param visitor : the visitor whose visit is requested
	 * @see Visitor
	 * @see Visitor#visit(Visitable)
	 * @see fr.sos.witchhunt.model.players.Player#accept(Visitor) Player::accept(Visitor)
	 * @see fr.sos.witchhunt.model.players.Player#addScore(int) Player::addScore(int)
	 */
	public void accept(Visitor visitor);
}
