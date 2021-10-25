package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.InputObservable;
import fr.sos.witchhunt.InputObserver;

public final class HumanPlayer extends Player implements InputObservable {
	
	private InputObserver inputObserver;
	
	//CONSTRUCTORS
	public HumanPlayer(String name, int id) {
		super(name,id);
	}
	public HumanPlayer(int id) {
		super(id);
		this.name="Player "+Integer.toString(id);
	}
	
	//INPUT METHODS
	public void setInputObserver(InputObserver io) {
		inputObserver = io;
	}
	
}
