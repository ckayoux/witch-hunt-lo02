package fr.sos.witchhunt;

import fr.sos.witchhunt.model.Menu;

public interface PlayerInputObserver {
	public int makeChoice(Menu possibilities);
}
