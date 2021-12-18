package fr.sos.witchhunt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sos.witchhunt.DisplayMediator;
import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Menu;

public final class Game {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON

	//ATTRIBUTES
	private static volatile Game instance = null;
	private Tabletop tabletop;
	private DisplayMediator displayMediator;
	private InputMediator inputMediator;
	private boolean sleepingAllowed=false;
	private boolean displayCPUStrategyChange=false;
	public final static int minPlayersNumber = 3;
	public final static int maxPlayersNumber = 6;
	
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
		displayMediator.displayMenu(mainMenu);
		switch (inputMediator.makeChoice(mainMenu)) {
				case 1:
					tabletop = Tabletop.getInstance();
					tabletop.setDisplayMediator(this.displayMediator);
					tabletop.setInputMediator(this.inputMediator);
					Thread matchThread = new Thread(tabletop);
					matchThread.start();
					break;
				/*case 2:
					options();
					break;*/
				case 2:
					exit();
					break;
		}
		
	}
	

	
	public void exit() {
		displayMediator.displayExitingGameScreen();
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
	
	//SETTERS
	
	public void setDisplayMediator(DisplayMediator dm) {
		this.displayMediator = dm;
	}

	public void setInputMediator(InputMediator im) {
		this.inputMediator = im;
	}
	
}
