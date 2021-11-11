package fr.sos.witchhunt;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;

public interface DisplayMediator {
	public void passLog(String msg);
	public void displayPossibilities(Menu possibilities);
	public void displayPlayTurnScreen(String playerName);
	public void displayAccusationScreen(Player accusator, Player accused);
	public void displayChooseDefenseScreen();
	public void displayForcedToRevealScreen();
	public void displayIdentityRevealScreen(Player p);
	public void displayScoreUpdateScreen(Player p, int points);
	public void displayEliminationScreen(Player eliminator,Player victim);
	public void displayLastUnrevealedPlayerScreen(Player p);
	public void displayNoCardsScreen(Player p);
	
	public void displayCard(RumourCard rc,boolean forcedReveal);
	public void displayWitchEffect(RumourCard rc);
	public void displayHuntEffect(RumourCard rc);
	public void displayCards(RumourCardsPile rcp,boolean forcedReveal);
	public void displayWitchEffects(RumourCardsPile rcp);
	public void displayHuntEffects(RumourCardsPile rcp);
	public void showCards(Player p);
	public void displaySelectCardScreen();
	public void displaySelectUnrevealedCardScreen();
	public void displaySelectRevealedCardScreen();
	public void displayPlayerPlaysEffectScreen(Player p);
}
