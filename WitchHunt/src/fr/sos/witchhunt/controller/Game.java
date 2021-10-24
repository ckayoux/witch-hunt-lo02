package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.Menu;
import fr.sos.witchhunt.view.std.StdView;
import fr.sos.witchhunt.model.players.HumanPlayer;

public final class Game {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON

	//ATTRIBUTES
	private static volatile Game instance = null;
	private Tabletop tabletop;
	
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
	
	private void gotoMainMenu() {
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
	}
	private void addPlayers() {
		Application.displayController.passLog("T0D0 : make the \"ADD PLAYERS\" feature");
	}
	public static void exit() {
		Application.displayController.passLog("See you soon !");
	}
	
}
