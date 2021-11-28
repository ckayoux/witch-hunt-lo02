package fr.sos.witchhunt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sos.witchhunt.model.Menu;

public final class Game {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON

	//ATTRIBUTES
	private static volatile Game instance = null;
	private Tabletop tabletop;
	private boolean sleepingAllowed=true;
	private final static int minPlayersNumber = 3;
	private final static int maxPlayersNumber = 6;
	private final static int totalRumourCardsCount = 12;
	private static int cpuPlayersNumber=0;
	
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
		Menu mainMenu = new Menu("main menu","Start new game","Options","Exit");
		Application.displayController.displayMenu(mainMenu);
		switch (Application.inputController.makeChoice(mainMenu)) {
			case 1:
				this.cpuPlayersNumber=0;
				startGame();
				break;
			case 2:
				options();
				break;
			case 3:
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
		List <String> takenNames = new ArrayList<String> ();
 		while(n<minPlayersNumber) {
			n++;
			tabletop.addPlayer(Application.inputController.createPlayer(n,takenNames));
		}
		Application.displayController.displayYesNoQuestion("\tWould you like to add another player ?");
		while(n<maxPlayersNumber && Application.inputController.answerYesNoQuestion()) {
			n++;
			tabletop.addPlayer(Application.inputController.createPlayer(n,takenNames));
			Application.displayController.displayYesNoQuestion("\tWould you like to add another player ?");
		}
		Application.displayController.passLog("\nAll "+Integer.toString(n)+" players have been successfully added.");
		Application.displayController.drawDashedLine();
		Application.displayController.crlf();
	}
	
	public static int getTotalRumourCardsCount() {
		return totalRumourCardsCount;
	}
	
	public static int getCPUPlayersNumber() {
		return cpuPlayersNumber;
	}
	public static void incrementCPUPlayersNumber() {
		cpuPlayersNumber++;
	}
	
	public static void exit() {
		Application.displayController.crlf();
		Application.displayController.drawStarsLine();
		Application.displayController.passLog("See you soon !");
		System.exit(0);
	}

	private void options() {
		String[] options = {((sleepingAllowed)?"Disable":"Enable")+" delay between CPU players' actions",
			"Main menu"};

		Menu optionsMenu = new Menu("game options",options);
		Application.displayController.displayMenu(optionsMenu);
		int input = Application.inputController.makeChoice(optionsMenu);
		switch (input) {
			case 1:
				Application.displayController.passLog("Delay set to : "+((sleepingAllowed)?"OFF":"ON"));
				sleepingAllowed=!sleepingAllowed;
				options();
				break;
			case 2:
				gotoMainMenu();
				break;
				
		}
	}
	public boolean sleepingIsAllowed() {
		return this.sleepingAllowed;
	}
	
}
