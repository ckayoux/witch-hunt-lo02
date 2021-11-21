package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.ExistingRumourCards;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;
import fr.sos.witchhunt.model.cards.*;

public interface PlayStrategy {
	
	
	
	public Identity selectIdentity();
	public TurnAction selectTurnAction(Identity identity, RumourCardsPile myHand,boolean canHunt);
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList);
	public DefenseAction selectDefenseAction(boolean canWitch,RumourCardsPile myHand);
	public default Player selectTarget(List<Player> eligiblePlayers) {
		return selectPlayerToAccuse(eligiblePlayers);
	}
	
	public RumourCard selectWitchCard(RumourCardsPile rcp);
	public RumourCard selectHuntCard(RumourCardsPile rcp);
	public RumourCard selectCardToDiscard(RumourCardsPile rcp);
	public Player selectNextPlayer(List<Player> list);
	public RumourCard selectBestCard(RumourCardsPile rcp, boolean seeUnrevealedCards);
	public DefenseAction revealOrDiscard(Identity identity,RumourCardsPile rcp);

	

}
