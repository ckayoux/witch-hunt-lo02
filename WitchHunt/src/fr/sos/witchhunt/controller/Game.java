package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.Menu;
import fr.sos.witchhunt.view.std.StdView;
import fr.sos.witchhunt.model.players.CPUPlayer;
import fr.sos.witchhunt.model.players.HumanPlayer;

public final class Game {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON

	//ATTRIBUTES
	private static volatile Game instance = null;
	private Tabletop tabletop;
	
	private Game() {		
		tabletop = Tabletop.getInstance();
		gotoMainMenu();
		exit();
	}
	
	public final static Game getInstance() {
        if (Game.instance == null) {
           synchronized(Game.class) {
             if (Game.instance == null) {
               Game.instance = new Game();
             }
           }
        }
        return Game.instance;
    }
	
	private void gotoMainMenu() {
		Menu mainMenu = new Menu("main menu","Start new game","Exit");
		Application.displayController.displayMenu(mainMenu);
		switch (Application.inputController.makeChoice(mainMenu)) {
			case 1:
				startGame();
				break;
			case 2:
				break;
		}
	}
	
	public void startGame () {
		addPlayers();
		Tabletop.getInstance().startPlaying();
	}
	
	private void addPlayers() {
		Application.displayController.drawDashedLine();
		int n=0;
		Application.displayController.passLog("Add 3 to 6 players :\n");
		while(n<3) {
			n++;
			tabletop.addPlayer(Application.inputController.createPlayer(n));
		}
		while(n<6 && askYesNoQuestion("\tWould you like to add another player ?")) {
			n++;
			tabletop.addPlayer(Application.inputController.createPlayer(n));
		}
		Application.displayController.passLog("\nAll "+Integer.toString(n)+" players have been successfully added.");
		Application.displayController.passLog("Each will get "+(int)Math.ceil(12/(float)n)+" Rumour cards.");
		Application.displayController.drawDashedLine();
		Application.displayController.crlf();
	}
	
	
	
	private boolean askYesNoQuestion(String q) {
		Application.displayController.displayYesNoQuestion(q);
		return Application.inputController.answerYesNoQuestion();
	}
	public static void exit() {
		Application.displayController.crlf();
		Application.displayController.passLog("See you soon !");
	}
	
}
