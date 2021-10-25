package fr.sos.witchhunt.model.cards;

public abstract class Card {
	protected boolean revealed=false;
	
	public void reveal() {
		this.revealed=true;
	}
	
	//GETTERS
	public boolean isRevealed() {
		return this.revealed;
	}
}
