package fr.sos.witchhunt.controller.core;

import fr.sos.witchhunt.controller.DisplayMediator;
import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.model.flow.Tabletop;

/**
 * <p><b>Core class giving access to the game and its options.</b></p>
 * <p>Instantiated by class {@link Application}, as a <i>Singleton</i> using the {@link #getInstance() static method}.
 * Can be accessed globally, exposing its public and non-static members, using the same method.
 * Game options are supposed to be checked by the model this way.</p>
 * <p>Gives access to the {@link #gotoMainMenu() Main Menu}, which is the entry point to the {@link #options() game options menu} and lets the user start the match.</p>
 * 
 * @see <a href="https://refactoringguru.cn/design-patterns/singleton"> Singleton design pattern </a>
 * @see #getInstance()
 * 
 * @see #gotoMainMenu()
 * @see #options()
 * 
 * @see fr.sos.witchhunt.model.flow.Tabletop Tabletop
 */
public final class Game {

	/**
	 * Unique instance of this class. Initialized and returned by {@link #getInstance()}.
	 */
	private static volatile Game instance = null;
	
	/**
	 * Object in charge of making the link between the model and the concurrent views.
	 * @see fr.sos.witchhunt.controller.DisplayMediator DisplayMediator
	 * @see fr.sos.witchhunt.controller.ConcreteDisplayMediator ConcreteDisplayMediator
	 * @see Application Set by the class creating this one.
	 */
	private DisplayMediator displayMediator;
	/**
	 * Object in charge of making the link between the model and the concurrent views.
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator ConcreteInputMediator
	 * @see Application Set by the class creating this one.
	 */
	private InputMediator inputMediator;
	
	/**
	 * <p><b>Object representing the match and controlling its flow.</b></p>
	 * <p>The match is started using this object's <code>{@link fr.sos.witchhunt.model.flow.Tabletop#startMatch() startMatch()}</code> method.</p>
	 * @see fr.sos.witchhunt.model.flow.Tabletop Tabletop
	 */
	private Tabletop tabletop;
	
	
	//OPTIONS
	/**
	 * <b>Boolean option. If set to <i>true</i>, a delay is simulated between after each game action.</b> 
	 * <p>Default : <i>true</i>.</p>
	 * @see #options() User can change its value using the game options menu
	 * @see fr.sos.witchhunt.model.flow.Tabletop#freeze(int) Tabletop::freeze(int)
	 */
	private boolean sleepingAllowed=true;
	/**
	 * <b>Boolean option. If set to <i>true</i>, 
	 * the {@link DisplayMediator#displayStrategyChange(fr.sos.witchhunt.model.players.Player, fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy) view will display} any 
	 * {@link fr.sos.witchhunt.model.players.CPUPlayer#chooseStrategy() change in a CPU Player's strategy}.</b> 
	 * <p>Default : <i>false</i>.</p>
	 * @see #options() User can change its value using the game options menu
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseStrategy() CPUPlayer::chooseStrategy()
	 */
	private boolean displayCPUStrategyChange=false;
	
	/**
	 * <b>Public and static method used for constructing an unique instance of it and accessing its non-static members</b>
	 * @return A reference to the unique instance of this class.
	 */
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
	
	/**
	 * <b>Exits the game. Called from the {@link #gotoMainMenu() main menu}.</b>
	 * @see #gotoMainMenu()
	 */
	public void exit() {
		displayMediator.displayExitingGameScreen();
		System.exit(0);
	}
	
	/**
	 * <p><b>Displays the game's main menu and lets the user choose between :</b></p>
	 * <p>- {@link fr.sos.witchhunt.model.flow.Tabletop#startMatch() Starting a new match}</p>
	 * <p>- Going to the {@link #options() options} menu</p>
	 * <p>- {@link #exit() Exiting} the application</p>
	 * 
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * @see fr.sos.witchhunt.controller.DisplayMediator#displayMenu(Menu) DisplayMediator::displayMenu(Menu)
	 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) InputMediator::makeChoice(Menu)
	 * 
	 * @see fr.sos.witchhunt.model.flow.Tabletop#startMatch() The match is instantiated as a <a href="https://refactoringguru.cn/design-patterns/singleton"> Singleton</a> and started in its own thread.
	 * @see #options()
	 * @see #exit()
	 */
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

	/**
	 * <p><b>Displays the game options menu.</b></p>
	 * <p>The user can modify the following options :</p>
	 * <p>- {@link #sleepingIsAllowed() Simulated delay between game actions}</p>
	 * <p>- {@link #displayCPUStrategyChange display changes in a CPU Player's strategy}.</p>
	 * 
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * @see fr.sos.witchhunt.controller.DisplayMediator#displayMenu(Menu) DisplayMediator::displayMenu(Menu)
	 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) InputMediator::makeChoice(Menu)
	 * 
	 * @see #sleepingAllowed
	 * @see #displayCPUStrategyChange
	 */
	private void options() {
		String[] options = {((sleepingAllowed)?"Disable":"Enable")+" delay between actions",
				((displayCPUStrategyChange)?"Disable":"Enable")+" displaying CPU strategy choices",
				"Main menu"};

		Menu optionsMenu = new Menu("game options",options);
		displayMediator.displayMenu(optionsMenu);
		int input = inputMediator.makeChoice(optionsMenu);
		switch (input) {
			case 1:
				displayMediator.passLog("Delay set to : "+((sleepingAllowed)?"OFF":"ON"));
				sleepingAllowed=!sleepingAllowed;
				options();
				break;
			case 2:
				displayMediator.passLog("Display of CPU Strategy choices set to : "+((displayCPUStrategyChange)?"OFF":"ON"));
				displayCPUStrategyChange=!displayCPUStrategyChange;
				options();
				break;
			case 3:
				gotoMainMenu();
				
		}
	}
	
	/**
	 * @return {@link #sleepingIsAllowed()}
	 */
	public boolean sleepingIsAllowed() {
		return this.sleepingAllowed;
	}
	/**
	 * @return {@link #displayCPUStrategyChange}
	 */
	public boolean displayChangesOfStrategy() {
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
