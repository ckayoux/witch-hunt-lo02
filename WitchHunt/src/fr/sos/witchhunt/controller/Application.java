package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.view.gui.GUIView;
import fr.sos.witchhunt.view.std.StdView;

public class Application {

	//ATTRIBUTES
	private static volatile Application instance = null;
		
	public static StdView console;
	//public static GUIView gui;
	public static DisplayController displayController;
	public static InputController inputController;
		
	public static void main(String[] args) {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON
		Application.getInstance();
	}
	
	private Application() {
		console = new StdView();
		
		//gui = new GUIView();
		
		displayController = new DisplayController();
		displayController.setConsole(console);
		
		inputController = new InputController();
		inputController.setConsole(console);
		
		Game game = Game.getInstance();
		game.setDisplayMediator(displayController);
		game.setInputMediator(inputController);
		game.gotoMainMenu();
	}
	
	public final static Application getInstance(){
		if(Application.instance == null) {
			synchronized(Application.class) {
				if(Application.instance == null) {
					Application.instance = new Application();
				}
			}
		}
		return instance;
	}
}
