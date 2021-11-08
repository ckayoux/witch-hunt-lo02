package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.players.Player;

public abstract class Effect {
	private String description;
	private int value;
	
	public Effect () {
		value=1;
	}
	public Effect (int value) {
		this.value=value;
	}
	
	public abstract void perform(); //has to be redefined
	public boolean isAllowed() {
		return true; //has to be redefined for some cards
	}
	
	public void takeNextTurn() {
		//TODO
	}
	
	public void chooseNextPlayer() {
		//TODO
	}
	
	//GETTERS
	public String getDescription() {
		return description;
	}
	public int getValue() {
		return value;
	}
	public Player getMyself() {
		return Tabletop.getInstance().getHunter();
	}
	public Player getTarget() {
		return Tabletop.getInstance().getHuntedPlayer();
	}
}
