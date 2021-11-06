package fr.sos.witchhunt.controller;

import java.util.ArrayList;
import java.util.List;

import fr.sos.witchhunt.PlayerDisplayObserver;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.std.StdView;

public final class DisplayController implements PlayerDisplayObserver {
	
	private StdView console;
	
	
	public void displayMenu(Menu m) {
		console.makeMenu(m);
		//T0D0 : makeMenu for GUI view
	}
	
	public void displayPossibilities(Menu possibilities) {
		console.logPossibilities(possibilities);
		//T0D0 : makeMenu for GUI view
	}
	
	public void displayYesNoQuestion(String q) {
		console.yesNoQuestion(q);
		//T0D0 : makeMenu for GUI view
	}
	
	public void drawStarsLine() {
		console.logStarsLine();
	}
	
	public void drawHardLine() {
		console.logHardLine();
	}
	
	public void drawDashedLine() {
		console.logDashedLine();
	}
	
	public void drawWeakDashedLine() {
		console.logWeakDashedLine();
	}
	
	@Override
	public void passLog(String msg) {
		console.log(msg);
	}
	
	public void crlf() {
		console.crlf();
	}
	
	public void displayContinueMessage() {
		console.logContinueMessage();
	}
	
	public void displayMatchStartScreen() {
		console.logMatchStartMessage();
		//TODO : equivalent for gui view
	}

	public void displayMatchEndScreen() {
		console.logMatchEndMessage();
		//TODO : equivalent for gui view
	}
	
	public void displayRoundStartScreen(int roundNumber) {
		console.logRoundStartMessage(roundNumber);
		//TODO : equivalent for gui view
	}

	public void displayRoundEndScreen(int roundNumber) {
		console.logRoundEndMessage(roundNumber);
		//TODO : equivalent for gui view
	}
	
	
	public void displayScoreTable(ScoreCounter sc) {
		String [][] table = new String[1][];//TODO
		console.logScoreTable(table); 
		//TODO : equivalent for gui view
	}
	
	public void displayWinner(String name, int score) {
		console.logWinner(name, score);
		//TODO : equivalent for gui view
	}
	
	public void setConsole(StdView console) {
		this.console=console;
	}
	
	public void displayClassment(List<Player> classment) {
		List<String> names = new ArrayList<String>();
		List<Integer> scores = new ArrayList<Integer>();
		for (Player p : classment) {
			names.add(p.getName());
			scores.add(p.getScore());
		}
		console.logClassment(names,scores);
		//TODO : equivalent for gui view
	}

	public void chooseIdentityScreen() {
		console.logChooseIdentityMessage();
		//TODO : equivalent for gui
	}

	public void distributeHandScreen() {
		int totalRumourCardsCount = Game.getTotalRumourCardsCount();
		int playersCount = Tabletop.getInstance().getPlayersCount();
		int distributedCardsCount = (int)Math.floor( totalRumourCardsCount / (float)playersCount );
		int discardedCardsCount = totalRumourCardsCount % playersCount;
		console.logHandDistributionMessage(distributedCardsCount,discardedCardsCount);
		//TODO : equivalent for gui
	}

	@Override
	public void displayPlayTurnScreen(String playerName) {
		console.logPlayTurnMessage(playerName);
		
	}

	@Override
	public void displayAccusationScreen(Player accusator, Player accused) {
		console.logAccusationMessage(accusator.getName(),accused.getName());
		// TODO equivalent for gui
	}
	
	@Override
	public void displayChooseDefenseScreen() {
		console.logChooseDefenseMessage();
		//ToutDoux : équivalent for gui
	}

	@Override
	public void displayForcedToRevealScreen() {
		console.logForcedToRevealMessage();
		//ToutDoux : equivalent for gui
	}

	@Override
	public void displayIdentityRevealScreen(Player p) {
		console.logGoingToRevealMessage(p.getName());
		switch(p.getIdentity()) {
			case VILLAGER:
				console.logVillagerRevealMessage(p.getName());
				break;
			case WITCH:
				console.logWitchRevealMessage(p.getName());
				break;
		}
		// Tout doux : equivalent for gui
	}

	@Override
	public void displayScoreUpdateScreen(Player p, int points) {
		console.logUpdateScreenMessage(p.getName(),points,p.getScore());
	}

	@Override
	public void displayEliminationScreen(Player eliminator, Player victim) {
		console.logEliminationMessage(eliminator.getName(),victim.getName());
		// Tout doux : equivalent for gui
	}

	public void displayLastUnrevealedPlayerScreen(Player lastUnrevealedPlayer) {
		console.logLastUnrevealedMessage(lastUnrevealedPlayer.getName());
		switch(lastUnrevealedPlayer.getIdentity()) {
			case WITCH:
				console.logWitchRevealMessage(lastUnrevealedPlayer.getName());
				break;
			case VILLAGER:
				console.logVillagerRevealMessage(lastUnrevealedPlayer.getName());
				break;
		}
		// Tout doux : equivalent for gui
	}

}
