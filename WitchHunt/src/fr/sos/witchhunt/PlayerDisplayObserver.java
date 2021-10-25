package fr.sos.witchhunt;

public interface PlayerDisplayObserver {
	public void passLog(String msg);
	public void displayPossibilities(Menu possibilities);
}
