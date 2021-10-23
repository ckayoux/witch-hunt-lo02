package fr.sos.witchhunt.model.players;

public final class CPUPlayer extends Player {
	public CPUPlayer(int id) {
		super(id);
		this.name="CPU "+Integer.toString(id);
	}
}
