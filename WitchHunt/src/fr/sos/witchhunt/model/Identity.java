package fr.sos.witchhunt.model;

/**
 * <p><b>This enum represents the possible identities players can choose.</b></p>
 * <p>A player can either choose to play as a <i>Villager</i>, or as a <i>Witch</i>.</p>
 * <p>At the start of each {@link fr.sos.witchhunt.controller.Round round}, players are asked to choose their identity.</p>
 * <p>The identity of a player can allow them to play specific {@link fr.sos.witchhunt.model.cards.RumourCard rumour cards} and has an influence
 * on the score they earn when they {@link fr.sos.witchhunt.model.players.Player#accuse(fr.sos.witchhunt.model.players.Player) accuse} an adversary or remain unrevealed the whole round.</p> 
 * @see fr.sos.witchhunt.model.players.Player Player
 * @see fr.sos.witchhunt.model.players.Player#chooseIdentity() Player::chooseIdentity()
 * @see fr.sos.witchhunt.model.players.Player#revealIdentity() Player::revealIdentity()
 * @see fr.sos.witchhunt.model.players.Player#winRound() Player::winRound() (triggered when a player is the last one whose identity wasn't revealed)
 * @see fr.sos.witchhunt.model.cards.Effect#isAllowed() Effect::isAllowed()
 * */
public enum Identity {
	WITCH,VILLAGER
}
