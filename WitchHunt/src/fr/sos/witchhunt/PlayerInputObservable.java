package fr.sos.witchhunt;

import fr.sos.witchhunt.model.Menu;

public interface PlayerInputObservable {
	public void setInputObserver(PlayerInputObserver pio);
	public int makeChoice(Menu m);
}
