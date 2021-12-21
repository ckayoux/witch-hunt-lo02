package fr.sos.witchhunt.model.players;

/**
 * <p><b>This enum represents the possible actions a player can perform when they are {@link fr.sos.witchhunt.model.players.Player#accuse(Player) accused} or targetted by a <i>{@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}</i>.</b></p>
 * <p>When a player is {@link fr.sos.witchhunt.model.players.Player#accuse(Player) accused}, they can choose whether to perform a
 * playable <i>{@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect}</i>
 * of an unrevealed {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} from their hand,
 * or to {@link fr.sos.witchhunt.model.players.Player#revealIdentity() reveal their identity}.</p>
 * <p>Certain {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards}, 
 * such as {@link fr.sos.witchhunt.model.cards.DuckingStool the Ducking Stool} can also make players choose 
 * between {@link fr.sos.witchhunt.model.players.Player#revealIdentity() reveal their identity} and
 * {@link fr.sos.witchhunt.model.players.Player#discard(fr.sos.witchhunt.model.cards.RumourCard) discarding a Rumour card}.</p>
 * @see fr.sos.witchhunt.model.players.Player#chooseDefenseAction() Player::chooseDefenseAction
 */
public enum DefenseAction {
	WITCH, REVEAL,DISCARD;
}
