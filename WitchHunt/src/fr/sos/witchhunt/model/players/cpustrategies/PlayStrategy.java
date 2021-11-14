package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.List;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

public interface PlayStrategy {
	
	public Identity selectIdentity();
	public TurnAction selectTurnAction();
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList);
	public default DefenseAction selectDefenseAction(boolean canWitch) {
		if (canWitch) return DefenseAction.WITCH;
		else return DefenseAction.REVEAL;
		/*Tout Doux : do better.
		 * distinguer le cas d'un villageois, qui au pire peut être révélé mais reste dans la partie
		 * de celui d'une sorcière pour qui c'est finito si elle se fait accuser*/
	}
	public default Player selectTarget(List<Player> eligiblePlayers) {
		return selectPlayerToAccuse(eligiblePlayers);
	}
	
	public RumourCard selectWorstCard(RumourCardsPile rcp);
	public RumourCard selectWitchCard(RumourCardsPile rcp);
	public default RumourCard selectCardToDiscard(RumourCardsPile rcp) {
		return selectWorstCard(rcp);
	}
	public Player selectNextPlayer(List<Player> list);
	public RumourCard selectBestCard(RumourCardsPile rcp, boolean seeUnrevealedCards);
	

}
