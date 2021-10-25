package fr.sos.witchhunt.controller;

import java.util.List;
import java.util.ArrayList;

import fr.sos.witchhunt.model.players.Player;

public final class Tabletop {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON
	
	//ATTRIBUTES
	private static volatile Tabletop instance = null;
	private List <Player> playersList;
	private Round currentRound;
	private Player lastUnrevealedPlayer;

	//CONSTRUCTOR
	private Tabletop () {
		playersList = new ArrayList<Player> ();
	}
	
	public static Tabletop getInstance() {
		if(Tabletop.instance == null) {
			synchronized(Tabletop.class) {
				if(Tabletop.instance == null) {
					Tabletop.instance = new Tabletop();
				}
			}
		}
		return Tabletop.instance;
	}
	
	//UTILS METHODS
	public static void addPlayer(Player p) {
		instance.playersList.add(p);
	}
	private boolean gameIsOver() {
		return (getRoundNumber() < 5)?false:true; //TEMPORARY
	}
	private void resetStates() {
		//TODO
	}
	
	//GAME ACTION METHODS
	public void startPlaying() {
		Application.displayController.crlf();
		Application.displayController.passLog("Let the witch hunt begin !");
		Application.displayController.drawHardLine();
		
		currentRound = new Round();
		while (!gameIsOver()){
			resetStates();
			currentRound = null;
			currentRound = new Round();
		}
		
		Application.displayController.crlf();
		Application.displayController.passLog("The game is over.");
		//TODO : determine who is the winner
		Application.displayController.passLog("Sorry, but my developpers are very lazy and I have yet no clue who the winner might possibly be.");
		Application.displayController.crlf();
		Application.displayController.drawHardLine();
	}
	
	//GETTERS
	public int getRoundNumber() {
		return currentRound.getRoundNumber();
	}
	
	public Round getCurrentRound() {
		return currentRound;
	}
	
	public Turn getCurrentTurn() {
		return currentRound.getCurrentTurn();
	}
	
	public Player getAccusedPlayer() {
		return currentRound.getCurrentTurn().getAccusedPlayer();
	}
	
	public Player getAccusator() {
		return currentRound.getCurrentTurn().getAccusator();
	}
	
	public List<Player> getPlayersList() {
		return playersList;
	}
	
	public int getPlayersCount() {
		return playersList.size();
	}
	
	public Player getLastUnrevealedPlayer() {
		return lastUnrevealedPlayer;
	}
	
	//SETTERS
	public void setLastUnrevealedPlayer(Player p) {
		lastUnrevealedPlayer = p;
	}
}
