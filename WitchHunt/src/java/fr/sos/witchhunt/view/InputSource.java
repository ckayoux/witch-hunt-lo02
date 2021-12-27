package fr.sos.witchhunt.view;

import fr.sos.witchhunt.model.players.Player;

public interface InputSource {
	public void post(String str);
	public void post();
	public void post(Player p);
}
