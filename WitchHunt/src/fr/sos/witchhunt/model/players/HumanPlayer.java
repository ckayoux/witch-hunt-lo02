package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.InputObservable;

public final class HumanPlayer extends Player implements InputObservable {
	public HumanPlayer(String name, int id) {
		super(name,id);
	}
	public HumanPlayer(int id) {
		super(id);
		this.name="Player "+Integer.toString(id);
	}
}
