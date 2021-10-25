package fr.sos.witchhunt.controller;

import java.util.List;

import fr.sos.witchhunt.model.players.Player;

public final class Round {
	
	//ATTRIBUTES
	private static Player currentPlayer;
	private static Player nextPlayer;
	private static int roundNumber=0;
	private Turn currentTurn;
	
	//CONSTRUCTOR
	public Round() {
		//For the first round, a random player begins.
		if(roundNumber == 0) currentPlayer=Tabletop.getInstance().getPlayersList().get((int)Math.random()*Tabletop.getInstance().getPlayersCount());
		//For all other rounds, the last unrevealed player begins.
		else currentPlayer=Tabletop.getInstance().getLastUnrevealedPlayer();
		
		roundNumber++;
		Turn.setTurnNumber(0);
		
		Application.displayController.crlf();
		Application.displayController.passLog("Round "+roundNumber+" :");
		//at the start of a round, before the first turn, each player has to choose an Identity and Rumour cards.
		distributeIdentity();
		distributeHand();
		
		do {
			setNextPlayerClockwise(); //By default, the player who takes the next turn is the one after the current player in Tabletop's players list.
			new Turn(currentPlayer);
			currentPlayer=nextPlayer; //Rumour cards played during the turn might have changed the next player.
		}while(!isOver()); //We keep starting new turns until the round is over.
		
		Tabletop.getInstance().setLastUnrevealedPlayer(getNextPlayer());//TODO : pass last unrevealed player instead.
		
		Application.displayController.crlf();
		Application.displayController.passLog("Round "+roundNumber+" is over.");
		Application.displayController.crlf();
		Application.displayController.drawDashedLine();
	}
	
	//UTILS METHODS
	private void distributeIdentity() {
		
	}
	private void distributeHand() {
		
	}
	private boolean isOver() {
		/*The round is over when :
		 * - Whether only one player is still unrevealed
		 * - or one player reached a score of 5*/
		return (Turn.getTurnNumber()<5)?false:true;//TEMPORARY
	}
	
	//GETTERS
	public int getRoundNumber() {
		return this.roundNumber;
	}
	public static Player getCurrentPlayer() {
		return currentPlayer;
	}
	public static Player getNextPlayer() {
		return nextPlayer;
	}
	public Turn getCurrentTurn() {
		return currentTurn;
	}
	
	//SETTERS
	public void setNextPlayer(Player p) {
		nextPlayer = nextPlayer;
	}
	public void setNextPlayerClockwise() {
		//automatically sets the next player to the one after the current player in Tabletop's players list
		List <Player> L = Tabletop.getInstance().getPlayersList();
		nextPlayer = L.get((L.indexOf(currentPlayer)+1) % L.size());
	}
}
