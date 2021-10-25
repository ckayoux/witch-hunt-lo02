package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.std.StdView;
import fr.sos.witchhunt.Menu;
import fr.sos.witchhunt.model.players.CPUPlayer;
import fr.sos.witchhunt.model.players.HumanPlayer;

public final class Game {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON

	//ATTRIBUTES
	private static volatile Game instance = null;
	private Tabletop tabletop;
	private final int minPlayersNumber = 3;
	private final int maxPlayersNumber = 6;
	
	private Game() {		
		tabletop = Tabletop.getInstance();
		gotoMainMenu();
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
	
	public void gotoMainMenu() {
		Menu mainMenu = new Menu("main menu","Start new game","Exit");
		Application.displayController.displayMenu(mainMenu);
		switch (Application.inputController.makeChoice(mainMenu)) {
			case 1:
				startGame();
				break;
			case 2:
				exit();
				break;
		}
	}
	
	public void startGame () {
		addPlayers();
		Application.inputController.wannaContinue();
		Tabletop.getInstance().startPlaying();
	}
	
	private void addPlayers() {
		Application.displayController.drawDashedLine();
		int n=0;
		Application.displayController.passLog("~ Add "+minPlayersNumber+" to "+maxPlayersNumber+" players : ~\n");
		while(n<minPlayersNumber) {
			n++;
			tabletop.addPlayer(Application.inputController.createPlayer(n));
		}
		Application.displayController.displayYesNoQuestion("\tWould you like to add another player ?");
		while(n<maxPlayersNumber && Application.inputController.answerYesNoQuestion()) {
			n++;
			tabletop.addPlayer(Application.inputController.createPlayer(n));
		}
		Application.displayController.passLog("\nAll "+Integer.toString(n)+" players have been successfully added.");
		Application.displayController.drawDashedLine();
		Application.displayController.crlf();
	}
	
	
	public static void exit() {
		Application.displayController.crlf();
		Application.displayController.drawStarsLine();
		Application.displayController.passLog("See you soon !");
		System.exit(0);
	}
	
}
