package fr.sos.witchhunt;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.players.Player;

public interface PlayerDisplayObserver {
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
}
