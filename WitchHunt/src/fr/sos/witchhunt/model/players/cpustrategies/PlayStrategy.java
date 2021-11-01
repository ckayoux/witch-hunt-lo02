package fr.sos.witchhunt.model.players.cpustrategies;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

public interface PlayStrategy {
	public Identity chooseIdentity();
	public TurnAction chooseTurnAction();
	public Player selectPlayerToAccuse();
}
