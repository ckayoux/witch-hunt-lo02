package fr.sos.witchhunt;

import fr.sos.witchhunt.model.Menu;

public interface PlayerDisplayObserver {
	public void passLog(String msg);
	public void displayPossibilities(Menu possibilities);
	public void displayPlayTurnMessage(String playerName);
}
