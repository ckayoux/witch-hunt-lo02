package fr.sos.witchhunt.controller;

import java.util.List;

import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>Class representing a round.</b></p>
 * <p>In charge of initializing the round and starting new {@link Turn turns} while the {@link #isOver() end-of-round-conditions} are not met.</p>
 * <p>Instantiated by {@link Tabletop}. Only one instance is supposed to be active at a given time.</p>
 * @see Tabletop
 * @see Turn
 */
public final class Round {
	
	//ATTRIBUTES
	/**
	 * The {@link fr.sos.witchhunt.model.players.Player player} playing their turn.
	 */
	private static Player currentPlayer;
	/**
	 * The next player who is going to get elected to play their turn, unless another takes their place.
	 */
	private static Player nextPlayer;
	private static int roundNumber=0;
	/**
	 * A reference to the {@link Turn turn} that is currently played.
	 */
	private Turn currentTurn;
	/**
	 * A reference to the common pile of discarded cards.
	 */
	private RumourCardsPile commonPile;
	
	//CONSTRUCTOR
	/**
	 * <p><b>The constructor does it all : initialize the round and play turns until one only player's identity is unrevealed.</b></p>
	 * <p>The first player to play is chosen randomly the first time. For all other rounds (except when the game is tied), the last unrevealed player is chosen to start.</p>
	 * <p>At the start of a round, every player {@link #distributeIdentity() chooses their identity} and {@link #distributeHand() their hand is distributed}.</p>
	 * <p>The round will keep creating {@link Turn turns} played by the {@link #nextPlayer next player} until it detects only one player's identity has not been revealed.</p> 
	 * <p>By default, the {@link #nextPlayer next player} is the one standing to the left of the current player ({@link #setNextPlayerClockwise() clockwise}). Cards and accusations change it.</p>
	 * <p>The {@link ScoreCounter score counter} will save the score obtained at the end of each round for each player.</p>
	 * <p>At the end of the round, the score board is displayed and every Rumour Card of the pile is returned to Tabletop's list of existing cards. Afterwise, the object's life ends and it is ready to get collected by the GC.</p>
	 */
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
		Tabletop.getInstance().getScoreCounter().addRound(); //adding an entry to the scoreboard
		Turn.setTurnNumber(0);
		
		Application.displayController.displayRoundStartScreen(roundNumber);
		//at the start of a round, before the first turn, each player has to choose an Identity and Rumour cards.
		distributeIdentity();
		commonPile = new RumourCardsPile();
		distributeHand();
		
		do {
			setNextPlayerClockwise(); //By default, the player who takes the next turn is the one after the current player in Tabletop's players list.
			new Turn(currentPlayer); //creating a new turn.
			if(nextPlayer==null) {
				setNextPlayerClockwise(); 
				System.out.println("!--Error, next player was null.--!");
			}
			else {
				if(!nextPlayer.isActive());
			}
			currentPlayer=nextPlayer; 
		}while(!isOver()); //We keep starting new turns until the round is over.
		
		commonPile.reset(); //returning all rumourCards to the main pile, of Tabletop's instance
		
		Application.displayController.displayRoundEndScreen(roundNumber);
	}
	
	//UTILS METHODS
	/**
	 * <b>Requests each player of the round to choose their identity.</b>
	 * @see fr.sos.witchhunt.model.players.Player Player
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see fr.sos.witchhunt.model.players.Player#chooseIdentity() Player::chooseIdentity()
	 */
	private void distributeIdentity() {
		Application.displayController.chooseIdentityScreen();
		for(Player p : Tabletop.getInstance().getActivePlayersList()) {
			p.chooseIdentity();
		}
		Application.displayController.crlf();
		Application.displayController.drawWeakDashedLine();
	}
	/**
	 * <p><b>Distribute their hand to each of the round's players.</b></p>
	 * <p>Each player gets a number of cards equal to the existing Rumour Cards number's quotient in the division by the number of players.</p>
	 * <p>If the division has a remainder, the cards that weren't distributed are placed in the {@link #commonPile pile}.</p>
	 */
	private void distributeHand() {	
		int playersNumber = Tabletop.getInstance().getActivePlayersList().size();
		int totalCardsCount = Tabletop.getInstance().getTotalRumourCardsCount();
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
	
	/**
	 * <p><b>Checks whether the end-of-round conditions are met or not</b></p>
	 * <p>The round is over when there is only one last unrevealed player remaining.</p>
	 * @return {@link #checkLastUnrevealedPlayer()}
	 */
	private boolean isOver() {
		return checkLastUnrevealedPlayer(); 
	}
	
	/**
	 * <p><b>Checks whether there is only one unrevealed player left or more</b></p>
	 * <p>The last unrevealed player will be notified and therefore {@link fr.sos.witchhunt.model.players.Player#winRound() update their score)} depending on their identity.</p>
	 * @return true if it is the case, false otherwise.
	 * @see fr.sos.witchhunt.model.players.Player#winRound() Player::winRound()
	 * @see Tabletop#getUnrevealedPlayersList()
	 */
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
	/**
	 * <b>Automatically sets the next player to the one after the current player in Tabletop's active players list.</b>
	 * @see Tabletop#getActivePlayersList()
	 */
	public void setNextPlayerClockwise() {
		List <Player> L = Tabletop.getInstance().getActivePlayersList();
		nextPlayer = L.get((L.indexOf(currentPlayer)+1) % L.size());
	}

	/**
	 * <b>Automatically sets the next player to the one before the current player in Tabletop's active players list.</b>
	 * @see Tabletop#getActivePlayersList()
	 * @see fr.sos.witchhunt.model.cards.Cauldron
	 */
	public void setNextPlayerCounterclockwise() {
				List <Player> L = Tabletop.getInstance().getActivePlayersList();
				int chosenIndex = L.indexOf(currentPlayer) -1;
				if(chosenIndex<0) chosenIndex=L.size()-1;
				nextPlayer = L.get(chosenIndex);
	}
	public static void setRoundNumber(int n) {
		roundNumber = n;
	}

	
}
