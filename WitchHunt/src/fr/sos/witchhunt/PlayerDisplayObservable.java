package fr.sos.witchhunt;

import fr.sos.witchhunt.PlayerDisplayObserver;

public interface PlayerDisplayObservable {

	public void requestLog(String msg);
	public void setDisplayObserver(PlayerDisplayObserver pdo);
}
