package fr.sos.witchhunt.model.cards;

public abstract class RumourCard extends Card {
	int value;

	//public void discard(){}
	public abstract boolean witch();
	public abstract boolean hunt();
}
