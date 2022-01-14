package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.controller.DisplayMediator;
import fr.sos.witchhunt.controller.interactions.Menu;
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
public interface PlayerDisplayRequester {

	/**
	 * <b>Sets the value of the controller in charge of displaying players-related information</b> 
	 * @param dm : a controller implementing the interface DisplayMediator
	 */
	public void setDisplayMediator(DisplayMediator dm);
	
	public void requestPlayTurnScreen();
	public void requestEndOfTurnScreen();
	public void requestAccusationScreen(Player accused);
	public void requestBeHuntedScreen();
	public void requestChooseDefenseScreen();
	public void requestForcedToRevealScreen();
	public void requestHasChosenIdentityScreen();
	public void requestIdentityRevealScreen();
	public void requestScoreUpdateScreen(int scoreUpdatedBy);
	public void requestEliminationScreen(Player victim);
	public void requestLastUnrevealedPlayerScreen();
	
	public void requestNoCardsScreen();
	public void requestSelectCardScreen(RumourCardsPile from, boolean forcedReveal);
	public void requestSelectUnrevealedCardScreen(RumourCardsPile from, boolean forcedReveal);
	public void requestSelectRevealedCardScreen(RumourCardsPile from, boolean forcedReveal);
	public void requestSelectWitchCardScreen(RumourCardsPile from);
	public void requestSelectHuntCardScreen(RumourCardsPile from);
	public void requestPlayerChoseToWitchScreen();
	public void requestPlayerPlaysWitchEffectScreen(RumourCard rc);
	public void requestPlayerPlaysHuntEffectScreen(RumourCard rc);
	public void requestEmptyRCPScreen(RumourCardsPile rcp);
	public void requestPlayerDiscardedCardScreen(RumourCard rc);
	public void requestHasChosenCardScreen(RumourCard chosen, RumourCardsPile from, boolean forceReveal);
	public void requestHasResetCardScreen(RumourCard chosen);
	
	public void requestLookAtPlayersIdentityScreen(Player target);
	public void requestForcedToAccuseScreen(Player by);
	public void requestStealCardFromScreen(Player stolenPlayer);
	public void requestTakeNextTurnScreen();
	public void requestPlayTurnAgainScreen();
	public void requestLog(String msg);
	public void requestDisplayPossibilities(Menu m);
}
