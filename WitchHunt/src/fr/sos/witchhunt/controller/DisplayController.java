package fr.sos.witchhunt.controller;

import java.util.List;
import java.util.ArrayList;

import fr.sos.witchhunt.DisplayObserver;
import fr.sos.witchhunt.Menu;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.std.StdView;

public final class DisplayController implements DisplayObserver {
	
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

	public void distributeHandScreen(int playersNumber) {
		int cardsNumber = (int)Math.ceil(12/(float)playersNumber);
		int discardedCardsNumber = 12%playersNumber;
		console.logHandDistributionMessage(cardsNumber,discardedCardsNumber);
		//TODO : equivalent for gui
	}
}
