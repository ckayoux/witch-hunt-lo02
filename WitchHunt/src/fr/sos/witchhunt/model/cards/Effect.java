package fr.sos.witchhunt.model.cards;

public abstract class Effect {
	String description;
	int value;
	
	public abstract void perform(); //has to be redefined
	public boolean isAllowed() {
		return true; //has to be redefined for some cards
	}
	
	public void takeNextTurn() {
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
