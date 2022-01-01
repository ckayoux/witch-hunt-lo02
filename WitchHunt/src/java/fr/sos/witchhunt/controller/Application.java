package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.view.gui.GUIView;
import fr.sos.witchhunt.view.std.StdView;

public class Application {

	//ATTRIBUTES
	private static volatile Application instance = null;
		
	public static StdView console;
	public static GUIView gui;
	public static ConcreteDisplayMediator displayMediator;
	public static ConcreteInputMediator inputMediator;
		
	public static void main(String[] args) {	//IMPLEMENTE LE DESIGN PATTERN SINGLETON
		Application.getInstance();
	}
	
	private Application() {
		
		
		displayMediator = new ConcreteDisplayMediator();
		inputMediator = new ConcreteInputMediator();
		
		console = new StdView();
		gui = new GUIView(inputMediator);
		
		displayMediator.setConsole(console);
		displayMediator.setGUI(gui);
		inputMediator.setConsole(console);
		inputMediator.setGui(gui);
		
		Game game = Game.getInstance();
		game.setDisplayMediator(displayMediator);
		game.setInputMediator(inputMediator);
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
