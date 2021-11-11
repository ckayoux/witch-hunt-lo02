package fr.sos.witchhunt;

import fr.sos.witchhunt.model.Menu;

public interface PlayerInputObservable {
	public void setInputMediator(InputMediator im);
	public int makeChoice(Menu m);
}
