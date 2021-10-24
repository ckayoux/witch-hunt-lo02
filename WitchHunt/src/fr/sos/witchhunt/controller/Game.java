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
	}
	
	private void addPlayers() {
		Application.displayController.drawDashedLine();
		int n=0;
		Application.displayController.passLog("Add 3 to 6 players :\n");
		while(n<3) {
			n++;
			makePlayer(n);
		}
		while(n<6 && askYesNoQuestion("\tWould you like to add another player ?")) {
			n++;
			makePlayer(n);
		}
		log("\nAll "+Integer.toString(n)+" players have been successfully added.");
		Application.displayController.drawDashedLine();
		log("");
	}
	
	private void makePlayer(int n) {
		log("\tPlayer "+Integer.toString(n)+" : ");
		boolean human = askYesNoQuestion("\tHuman controlled ?");
		if(human) {
			log("\tName ?");
			String name = Application.inputController.getStringInput();
			tabletop.addPlayer(new HumanPlayer(name,n));
		}
		else tabletop.addPlayer(new CPUPlayer(n));
		log("");
	}
	
	private boolean askYesNoQuestion(String q) {
		Application.displayController.displayYesNoQuestion(q);
		return Application.inputController.answerYesNoQuestion();
	}
	private void log(String str) {
		Application.displayController.passLog(str);
	}
	private String gets() {
		return Application.inputController.getStringInput();
	}
	public static void exit() {
		Application.displayController.passLog("See you soon !");
	}
	
}
