package fr.sos.witchhunt.model.cards;

public abstract class Effect {
	String description;
	int value;
	
	public abstract void perform();
	public abstract boolean isAllowed() ;
	
	private void takeNextTurn() {
		//TODO
	}
	
	//GETTERS
	public String getDescription() {
		return description;
	}
	public int getValue() {
		return value;
	}
}
