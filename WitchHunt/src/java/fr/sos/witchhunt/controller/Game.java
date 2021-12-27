package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.DisplayMediator;
import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.model.Menu;

public final class Game {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON

	//ATTRIBUTES
	private static volatile Game instance = null;
	private Tabletop tabletop;
	private DisplayMediator displayMediator;
	private InputMediator inputMediator;
	private boolean sleepingAllowed=true;
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
		
		Menu mainMenu = new Menu("MAIN MENU","Start new game","Options","Exit");
		displayMediator.displayMenu(mainMenu);
		switch (inputMediator.makeChoice(mainMenu)) {
				case 1:
					tabletop = Tabletop.getInstance();
					tabletop.setDisplayMediator(this.displayMediator);
					tabletop.setInputMediator(this.inputMediator);
					Thread matchThread = new Thread(tabletop);
					matchThread.start();
					break;
				case 2:
					options();
					break;
				case 3:
					exit();
					break;
		}
		
	}
	

	
	public void exit() {
		displayMediator.displayExitingGameScreen();
		System.exit(0);
	}

	private void options() {
		String[] options = {((sleepingAllowed)?"Disable":"Enable")+" delay between actions",
			"Main menu"};

		Menu optionsMenu = new Menu("game options",options);
		Application.displayController.displayMenu(optionsMenu);
		int input = Application.inputController.makeChoice(optionsMenu);
		switch (input) {
			case 1:
				displayMediator.passLog("Delay set to : "+((sleepingAllowed)?"OFF":"ON"));
				sleepingAllowed=!sleepingAllowed;
				options();
				break;
			case 2:
				gotoMainMenu();
				
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
