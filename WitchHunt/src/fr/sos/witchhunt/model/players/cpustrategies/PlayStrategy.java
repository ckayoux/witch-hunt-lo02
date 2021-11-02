package fr.sos.witchhunt.model.players.cpustrategies;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

public interface PlayStrategy {
	public Identity chooseIdentity();
	public TurnAction chooseTurnAction();
	public Player selectPlayerToAccuse();
	public default DefenseAction chooseDefenseAction(boolean canWitch) {
		if (canWitch) return DefenseAction.WITCH;
		else return DefenseAction.REVEAL;
		/*Tout Doux : do better.
		 * distinguer le cas d'un villageois, qui au pire peut être révélé mais reste dans la partie
		 * de celui d'une sorcière pour qui c'est finito si elle se fait accuser*/
	}
}
