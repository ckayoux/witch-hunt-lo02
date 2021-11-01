package fr.sos.witchhunt.controller;

import java.util.ArrayList;
import java.util.List;

import fr.sos.witchhunt.model.cards.AngryMob;
import fr.sos.witchhunt.model.cards.BlackCat;
import fr.sos.witchhunt.model.cards.Broomstick;
import fr.sos.witchhunt.model.cards.Cauldron;
import fr.sos.witchhunt.model.cards.DuckingStool;
import fr.sos.witchhunt.model.cards.EvilEye;
import fr.sos.witchhunt.model.cards.HookedNose;
import fr.sos.witchhunt.model.cards.PetNewt;
import fr.sos.witchhunt.model.cards.PointedHat;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.cards.TheInquisition;
import fr.sos.witchhunt.model.cards.Toad;
import fr.sos.witchhunt.model.cards.Wart;
import fr.sos.witchhunt.model.players.Player;

public final class Tabletop {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON
	
	//ATTRIBUTES
	private static volatile Tabletop instance = null;
	private List <Player> playersList;
	private Round currentRound;
	private Player lastUnrevealedPlayer;
	private ScoreCounter scoreCounter;
	private RumourCardsPile allCardsPile;

	//CONSTRUCTOR
	private Tabletop () {
		playersList = new ArrayList<Player> ();
		
		allCardsPile = new RumourCardsPile();
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
		allCardsPile.addCard(new Wart());
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
		allCardsPile.getCards().forEach(rc -> rc.reset());
		playersList.forEach(p -> p.reset());
	}
	
	//GAME ACTION METHODS
	public void startPlaying() {
		Application.displayController.displayMatchStartScreen();

		scoreCounter = new ScoreCounter();
		
		currentRound = new Round();
		while (!gameIsOver()){
			Application.displayController.displayScoreTable(scoreCounter);
			Application.inputController.wannaContinue();
			resetStates();
			currentRound = null;
			currentRound = new Round();
		}
		currentRound.setRoundNumber(0);
		currentRound = null;
		
		Application.displayController.displayMatchEndScreen();
		Application.displayController.displayWinner("winner's name",5); //TODO : use real values
		Application.displayController.displayClassment(scoreCounter.getClassment());
		Application.displayController.displayScoreTable(scoreCounter);
		
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
	
	public Turn getCurrentTurn() {
		return currentRound.getCurrentTurn();
	}
	
	public Player getAccusedPlayer() {
		return currentRound.getCurrentTurn().getAccusedPlayer();
	}
	
	public Player getAccusator() {
		return currentRound.getCurrentTurn().getAccusator();
	}
	
	public Player getCurrentPlayer() {
		return currentRound.getCurrentPlayer();
	}
	public List<Player> getPlayersList() {
		return playersList;
	}
	public List<Player> getActivePlayersList() {
		List <Player> l = new ArrayList<Player> ();
		playersList.forEach(p -> {if (p.isActive()) l.add(p);});
		return l;
	}
	public ScoreCounter getScoreCounter() {
		return scoreCounter;
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

	public List<Player> getAccusablePlayersList() {
		List <Player> l = new ArrayList<Player>();
		playersList.forEach(p -> { if(p.isAccusable()) l.add(p); });
		return l;
	}
}
