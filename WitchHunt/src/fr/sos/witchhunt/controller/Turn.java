package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.model.players.Player;

public final class Turn {
	//ATTRIBUTES
	private static int turnNumber=0;
	private Player accused;
	private Player accusator;
	private Player hunted;
	private Player hunter;
	
	//CONSTRUCTOR
	public Turn (Player p) {
		Tabletop.getInstance().getCurrentRound().setCurrentTurn(this);
		turnNumber++;
		p.playTurn();
		Application.displayController.crlf();
		Application.displayController.drawWeakDashedLine();
		Application.displayController.crlf();
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
	public Player getHuntedPlayer() {
		return this.hunted;
	}
	public Player getHunter() {
		return this.hunter;
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
	public void setHuntedPlayer(Player huntedPlayer) {
		this.hunted = huntedPlayer;
	}
	public void setHunter(Player hunter) {
		this.hunter = hunter;
	}
	

}
