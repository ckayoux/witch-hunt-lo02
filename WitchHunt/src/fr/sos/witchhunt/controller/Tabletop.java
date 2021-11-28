package fr.sos.witchhunt.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import fr.sos.witchhunt.model.cards.ExistingRumourCards;

import fr.sos.witchhunt.model.cards.RumourCardsPile;

import fr.sos.witchhunt.model.players.Player;

public final class Tabletop {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON
	
	//ATTRIBUTES
	private static volatile Tabletop instance = null;
	private List <Player> playersList;
	private Round currentRound;
	private ScoreCounter scoreCounter;
	private RumourCardsPile allCardsPile;
	private Player lastUnrevealedPlayer=null;
	private boolean gameIsTied=false;
	
	//CONSTRUCTOR
	private Tabletop () {
		playersList = new ArrayList<Player> ();
		
		allCardsPile = new RumourCardsPile(ExistingRumourCards.getInstance().getList());
		/*allCardsPile = new RumourCardsPile();
		allCardsPile.addCard(new AngryMob());
		allCardsPile.addCard(new BlackCat());
		allCardsPile.addCard(new Broomstick());
		allCardsPile.addCard(new Cauldron());
		allCardsPile.addCard(new DuckingStool());
		allCardsPile.addCard(new EvilEye());
		allCardsPile.addCard(new HookedNose());
		allCardsPile.addCard(new PetNewt());
		allCardsPile.addCard(new PointedHat());
		allCardsPile.addCard(new TheInquisition());
		allCardsPile.addCard(new Toad());
		allCardsPile.addCard(new Wart());*/
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
		return scoreCounter.hasWinner();
	}
	private void resetStates() {
		int playersCount = this.playersList.size();
		allCardsPile.getCards().forEach(rc -> rc.reset());
		for(int i=0; i<playersCount; i++) {
			playersList.get(i).reset();
		}
		//playersList.stream().forEach(p -> p.reset());
	}
	
	//GAME ACTION METHODS
	public void startPlaying() {
		Application.displayController.displayMatchStartScreen();

		scoreCounter = new ScoreCounter();
		
		new Round();
		while (!gameIsOver()){
			Application.displayController.displayScoreBoard(scoreCounter.getScoreBoard());
			Application.inputController.wannaContinue();
			Application.displayController.crlf();
			resetStates();
			List<Player> potentialWinners = scoreCounter.getPotentialWinners();
			if(!potentialWinners.isEmpty()) {
				playersList.stream().filter(p->!potentialWinners.contains(p)).forEach(p->p.setActive(false));
				Application.displayController.displayGameIsTiedScreen(potentialWinners);
				this.gameIsTied=true;
			}
			currentRound = null;
			currentRound = new Round();
		}
		resetStates();
		this.gameIsTied=false;
		currentRound.setRoundNumber(0);
		currentRound = null;
		
		Application.displayController.displayMatchEndScreen();
		Application.displayController.displayScoreBoard(scoreCounter.getScoreBoard());
		Application.displayController.displayWinnerScreen(scoreCounter.getWinner());
		
		
		scoreCounter=null;
		playersList = null;
		playersList = new ArrayList<Player>();
		
		Application.inputController.wannaContinue();
		Game.getInstance().gotoMainMenu();
	}
	
	//GETTERS
	public int getRoundNumber() {
		return currentRound.getRoundNumber();
	}
	
	public Round getCurrentRound() {
		return currentRound;
	}
	
	public RumourCardsPile getAllCardsPile() {
		return allCardsPile;
	}
	
	public RumourCardsPile getPile() {
		return currentRound.getPile();
	}
	
	public Turn getCurrentTurn() {
		return currentRound.getCurrentTurn();
	}
	
	public Player getAccusedPlayer() {
		return currentRound.getCurrentTurn().getAccusedPlayer();
	}
	
	public Player getAccusator() {
		return getCurrentTurn().getAccusator();
	}
	public Player getHuntedPlayer() {
		return getCurrentTurn().getHuntedPlayer();
	}
	public Player getHunter() {
		return getCurrentTurn().getHunter();
	}
	public Player getCurrentPlayer() {
		return currentRound.getCurrentPlayer();
	}
	public List<Player> getPlayersList() {
		return playersList;
	}
	public List<Player> getActivePlayersList() {
		return this.playersList.stream().filter(p -> p.isActive()).collect(Collectors.toList());
	}
	public ScoreCounter getScoreCounter() {
		return scoreCounter;
	}
	
	public int getPlayersCount() {
		return playersList.size();
	}
	
	public List<Player> getAccusablePlayersList() {
		return this.getActivePlayersList().stream().filter(p -> p.isAccusable()).collect(Collectors.toList());
	}
	
	public List<Player> getUnrevealedPlayersList() {
		return getActivePlayersList().stream().filter(p -> !p.isRevealed()).collect(Collectors.toList());
	}
	
	public Player getLastUnrevealedPlayer() {
		return this.lastUnrevealedPlayer;
	}
	
	public List<Player> getLeadingPlayers() {
		return this.scoreCounter.getLeadingPlayers();
	}
	public List<Player> getLastPlayers() {
		return this.scoreCounter.getLastPlayers();
	}
	public List<Player> getRanking(){
		return this.scoreCounter.getRanking();
	}
	public boolean gameIsTied() {
		return this.gameIsTied;
	}
	
	//SETTERS
	public void setCurrentRound(Round r) {
		currentRound = r;
	}
	public void setAccusedPlayer(Player accusedPlayer) {
		getCurrentTurn().setAccusedPlayer(accusedPlayer);
	}
	public void setAccusator(Player accusator) {
		getCurrentTurn().setAccusator(accusator);
	}
	public void setHuntedPlayer(Player huntedPlayer) {
		getCurrentTurn().setHuntedPlayer(huntedPlayer);
	}
	public void setHunter(Player hunter) {
		getCurrentTurn().setHunter(hunter);
	}
	public void setLastUnrevealedPlayer(Player p) {
		this.lastUnrevealedPlayer=p;
	}
}
