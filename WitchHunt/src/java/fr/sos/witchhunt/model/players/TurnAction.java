package fr.sos.witchhunt.model.players;

/**
 * <p><b>This enum represents the possible actions a player can perform during his turn.</b></p>
 * <p>During his turn, a player can either choose to {@link fr.sos.witchhunt.model.players.Player#accuse(Player) Player::accuse(Player)} another player, 
 * or choose to perform the <i>{@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}</i> of one of their cards.</p>
 * @see fr.sos.witchhunt.model.players.Player#chooseTurnAction() Player::chooseTurnAction
 */
public enum TurnAction {
	ACCUSE, HUNT;
}
