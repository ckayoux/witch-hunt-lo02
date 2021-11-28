package fr.sos.witchhunt.controller;

import java.util.List;

import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;

public final class Round {
	
	//ATTRIBUTES
	private static Player currentPlayer;
	private static Player nextPlayer;
	private static int roundNumber=0;
	private Turn currentTurn;
	private RumourCardsPile commonPile;
	
	//CONSTRUCTOR
	public Round() {
		Tabletop.getInstance().setCurrentRound(this);
		//For the first round, a random player begins.
		if(roundNumber == 0) currentPlayer=Tabletop.getInstance().getActivePlayersList().get((int)(Math.random()*Tabletop.getInstance().getActivePlayersList().size()));
		//For all other rounds, the last unrevealed player begins.
		else {
			currentPlayer=Tabletop.getInstance().getLastUnrevealedPlayer();
			if(Tabletop.getInstance().gameIsTied()) {
				if(!Tabletop.getInstance().getActivePlayersList().contains(currentPlayer)) {
					currentPlayer=Tabletop.getInstance().getActivePlayersList().get((int)(Math.random()*Tabletop.getInstance().getActivePlayersList().size()));
				}
			}
		}
		
		roundNumber++;
		Tabletop.getInstance().getScoreCounter().addRound();
		Turn.setTurnNumber(0);
		
		Application.displayController.displayRoundStartScreen(roundNumber);
		//at the start of a round, before the first turn, each player has to choose an Identity and Rumour cards.
		distributeIdentity();
		commonPile = new RumourCardsPile();
		distributeHand();
		
		do {
			setNextPlayerClockwise(); //By default, the player who takes the next turn is the one after the current player in Tabletop's players list.
			new Turn(currentPlayer);
			if(nextPlayer==null) {
				setNextPlayerClockwise(); 
				System.out.println("!--Error, next player was null.--!");
			}
			else {
				if(!nextPlayer.isActive());
			}
			currentPlayer=nextPlayer; 
		}while(!isOver()); //We keep starting new turns until the round is over.
		
		recycleRumourCards(); //returning all rumourCards to the main pile, of Tabletop's instance
		
		Application.displayController.displayRoundEndScreen(roundNumber);
	}
	
	//UTILS METHODS
	private void distributeIdentity() {
		Application.displayController.chooseIdentityScreen();
		for(Player p : Tabletop.getInstance().getActivePlayersList()) {
			p.chooseIdentity();
		}
		Application.displayController.crlf();
		Application.displayController.drawWeakDashedLine();
	}
	private void distributeHand() {	
		int playersNumber = Tabletop.getInstance().getActivePlayersList().size();
		int totalCardsCount = Game.getTotalRumourCardsCount();
		int distributedCardsNumber = (int) Math.floor(totalCardsCount / (float)playersNumber);
		int discardedCardsNumber = totalCardsCount % playersNumber;
		
		RumourCardsPile allCardsPile = Tabletop.getInstance().getAllCardsPile();
		allCardsPile.shuffle();
		for (int i=0; i<discardedCardsNumber; i++) {
			RumourCard givenCard = allCardsPile.getCards().get(i);
			allCardsPile.giveCard(givenCard, commonPile);
		}
		for (Player p : Tabletop.getInstance().getActivePlayersList()) {
			for(int i=0; i<distributedCardsNumber; i++) {
				RumourCard givenCard = allCardsPile.getCards().get(0);
				p.takeRumourCard(givenCard, allCardsPile);
			}
		}
		
		Application.displayController.distributeHandScreen();
		
	}
	private boolean isOver() {
		/*The round is over when :
		 * - Whether only one player is still unrevealed
		 * - or one player reached a score of 5*/
		return checkLastUnrevealedPlayer(); //Temporary
	}
	
	public boolean checkLastUnrevealedPlayer() {
		List<Player> unrevealedPlayersList = Tabletop.getInstance().getUnrevealedPlayersList();
		switch(unrevealedPlayersList.size()) {
		case 1:
			Player lastManStanding = unrevealedPlayersList.get(0);
			lastManStanding.winRound();
			Application.displayController.crlf();
			Application.displayController.drawWeakDashedLine();
			return true;
		case 2:
			Application.displayController.displayOnlyTwoUnrevealedRemainingScreen();
		default:
			return false;
		}

	}
	
	private void recycleRumourCards() {
		Tabletop.getInstance().getAllCardsPile().eat(commonPile); //returning the common pile to the main cards pile
		for(Player p : Tabletop.getInstance().getPlayersList()) Tabletop.getInstance().getAllCardsPile().eat(p.getHand());
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

	public RumourCardsPile getPile() {
		return this.commonPile;
	}
	
	//SETTERS
	public void setCurrentTurn(Turn t) {
		currentTurn = t;
	}
	public void setNextPlayer(Player p) {
		nextPlayer = p;
	}
	public void setNextPlayerClockwise() {
		//automatically sets the next player to the one after the current player in Tabletop's players list
		List <Player> L = Tabletop.getInstance().getActivePlayersList();
		nextPlayer = L.get((L.indexOf(currentPlayer)+1) % L.size());
	}

	public void setNextPlayerCounterclockwise() {
		//automatically sets the next player to the one before the current player in Tabletop's players list. Used by the rumour card Cauldron
				List <Player> L = Tabletop.getInstance().getActivePlayersList();
				int chosenIndex = L.indexOf(currentPlayer) -1;
				if(chosenIndex<0) chosenIndex=L.size()-1;
				nextPlayer = L.get(chosenIndex);
	}
	public static void setRoundNumber(int n) {
		roundNumber = n;
	}

	
}
