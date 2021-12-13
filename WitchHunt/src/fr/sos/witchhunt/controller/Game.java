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
	private boolean sleepingAllowed=false;
	private boolean displayCPUStrategyChange=false;
	public final static int minPlayersNumber = 3;
	public final static int maxPlayersNumber = 6;
	
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
		
		Menu mainMenu = new Menu("main menu","Start new game"/*,"Options"*/,"Exit");
		Application.displayController.displayMenu(mainMenu);
		switch (Application.inputController.makeChoice(mainMenu)) {
				case 1:
					Tabletop.getInstance().startMatch();
					break;
				/*case 2:
					options();
					break;*/
				case 2:
					exit();
					break;
		}
		
	}
	

	
	public static void exit() {
		Application.displayController.crlf();
		Application.displayController.drawStarsLine();
		Application.displayController.passLog("See you soon !");
		System.exit(0);
	}

	private void options() {
		String[] options = {((sleepingAllowed)?"Disable":"Enable")+" delay between CPU players' actions",
			((displayCPUStrategyChange)?"Disable":"Enable")+" displaying CPU players' changes of strategy",
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
				Application.displayController.passLog("CPU players will "+((displayCPUStrategyChange)?"NOT ":"")+ "display their changes of strategy.");
				displayCPUStrategyChange=!displayCPUStrategyChange;
				options();
				break;
			case 3:
				break;
				
		}
	}
	public boolean sleepingIsAllowed() {
		return this.sleepingAllowed;
	}
	public boolean cpuPlayersDisplayChangesOfStrategy() {
		return this.displayCPUStrategyChange;
	}
	
}
