package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.model.Menu;

public final class Game {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON

	//ATTRIBUTES
	private static volatile Game instance = null;
	private Tabletop tabletop;
	private final static int minPlayersNumber = 3;
	private final static int maxPlayersNumber = 6;
	private final static int totalRumourCardsCount = 12;
	
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
	
	public static int getTotalRumourCardsCount() {
		return totalRumourCardsCount;
	}
	
	
	public static void exit() {
		Application.displayController.crlf();
		Application.displayController.drawStarsLine();
		Application.displayController.passLog("See you soon !");
		System.exit(0);
	}
	
}
