package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.model.players.Player;

public final class Turn {
	//ATTRIBUTES
	private static int turnNumber=0;
	private Player accused;
	private Player accusator;
	
	//CONSTRUCTOR
	public Turn (Player p) {
		turnNumber++;
		Application.displayController.crlf();
		p.playTurn();
		Application.displayController.crlf();
		Application.displayController.drawWeakDashedLine();
	}
	
	//GETTERS
	public static int getTurnNumber() {
		return turnNumber;
	}
	public Player getAccusedPlayer() {
		return accused;
	}
	public Player getAccusator() {
		return accusator;
	}
	
	//SETTERS
	public static void setTurnNumber(int turnNumber) {
		Turn.turnNumber = turnNumber;
	}
	public void setAccusedPlayer(Player accused) {
		this.accused = accused;
	}
	public void setAccusator(Player accusator) {
		this.accusator = accusator;
	}
	

}
