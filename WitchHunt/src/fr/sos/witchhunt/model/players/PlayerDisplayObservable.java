package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.DisplayMediator;

import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;

/**
 * <p><b>Interface implemented by Player and its daughter classes.</b></p>
 * <p>Specifies a set of methods allowing the players to communicate with a display controller implementing {@link DisplayMediator}.</p>
 * <p>All methods specified this interface are meant to trigger their counterpart from the DisplayMediator interface.</p>
 * @see DisplayMediator
 * @see fr.sos.witchhunt.model.players.Player Player
 * @see fr.sos.witchhunt.model.players.CPUPlayer CPUPlayer
 * @see fr.sos.witchhunt.model.players.HumanPlayer HumanPlayer
 */
public interface PlayerDisplayObservable {

	/**
	 * <b>Sets the value of the controller in charge of displaying players-related information</p> 
	 * @param dm : a controller implementing the interface DisplayMediator
	 */
	public void setDisplayMediator(DisplayMediator dm);
	
	public void requestPlayTurnScreen();
	public void requestEndOfTurnScreen();
	public void requestAccusationScreen(Player accused);
	public void requestChooseDefenseScreen();
	public void requestForcedToRevealScreen();
	public void requestHasChosenIdentityScreen();
	public void requestIdentityRevealScreen();
	public void requestScoreUpdateScreen(int scoreUpdatedBy);
	public void requestEliminationScreen(Player victim);
	public void requestLastUnrevealedPlayerScreen();
	
	public void requestNoCardsScreen();
	public void requestSelectCardScreen();
	public void requestSelectUnrevealedCardScreen();
	public void requestSelectRevealedCardScreen();
	public void requestPlayerPlaysWitchEffectScreen(RumourCard rc);
	public void requestPlayerPlaysHuntEffectScreen(RumourCard rc);
	public void requestEmptyRCPScreen(RumourCardsPile rcp);
	public void requestDiscardCardScreen(RumourCard rc);
	public void requestHasChosenCardScreen(RumourCard chosen, boolean forceReveal);
	public void requestHasResetCardScreen(RumourCard chosen);
	
	public void requestLookAtPlayersIdentityScreen(Player target);
	public void requestForcedToAccuseScreen(Player by);
	public void requestStealCardFromScreen(Player stolenPlayer);
	public void requestTakeNextTurnScreen();
	public void requestPlayTurnAgainScreen();
	public void requestLog(String msg);
	public void requestDisplayPossibilities(Menu m);


}
