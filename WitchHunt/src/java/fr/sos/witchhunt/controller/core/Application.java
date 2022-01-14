package fr.sos.witchhunt.controller.core;

import fr.sos.witchhunt.controller.ConcreteDisplayMediator;
import fr.sos.witchhunt.controller.ConcreteInputMediator;
import fr.sos.witchhunt.view.gui.GUIView;
import fr.sos.witchhunt.view.std.StdView;


/**
 * <p><b>Core class in charge of initializing the application's core components.</b></p>
 * <p>Contains the <code>main</code> method. Supposed to be the <code>Main-Class</code> of the executable JAR's manifest.</p>
 * <p>Instantiated as a <i>Singleton</i> using the {@link #getInstance() static method}.
 * Can be accessed globally, exposing its public and non-static members, using the same method.</p>
 * <p>When instantiated, prepares all objects that will be responsible for display and input, 
 * such as {@link fr.sos.witchhunt.controller.DisplayMediator DisplayMediator} the display mediator,
 * then, instantiates the game.</p>
 * @author Félix Houdebert, Audrey Souppaya
 * @see #main(String[])
 * 
 * @see <a href="https://refactoringguru.cn/design-patterns/singleton">Singleton design pattern</a>
 * @see #getInstance()
 * 
 * @see fr.sos.witchhunt.controller.DisplayMediator DisplayMediator
 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
 * 
 * @see fr.sos.witchhunt.view.gui.GUIView GUI
 * @see fr.sos.witchhunt.view.std.StdView Console view
 */
public class Application {

	//ATTRIBUTES
	/**
	 * Unique instance of this class. Initialized and returned by {@link #getInstance()}.
	 */
	private static volatile Application instance = null;
	
	/**
	 * Object in charge of the console view.
	 * @see fr.sos.witchhunt.view.std.StdView StdView
	 */
	private StdView console;
	/**
	 * Object in charge of the view graphical user interface view.
	 * @see fr.sos.witchhunt.view.gui.GUIView GUIView
	 */
	private GUIView gui;
	
	/**
	 * Object in charge of making the link between the model and the concurrent views.
	 * @see fr.sos.witchhunt.controller.DisplayMediator DisplayMediator
	 * @see fr.sos.witchhunt.controller.ConcreteDisplayMediator ConcreteDisplayMediator
	 */
	private ConcreteDisplayMediator displayMediator;
	/**
	 * Object in charge of gathering user-input from the concurrent views.
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator ConcreteInputMediator
	 */
	private ConcreteInputMediator inputMediator;
		
	/**
	 * <b><code>main</code> method. Starting point of the whole application.</b>
	 * This class should be the <code>Main-Class</code> of a JAR's manifest.
	 * @see #getInstance()
	 */
	public static void main(String[] args) {
		Application.getInstance();
	}
	

	/**
	 * <b>Public and static method used for constructing an unique instance of it and accessing its non-static members</b>
	 * @return A reference to the unique instance of this class.
	 */
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
	
	/**
	 * @see #getInstance() only the <code>getInstance()</code> method is supposed to instantiate this class.
	 * @see <a href="https://refactoringguru.cn/design-patterns/singleton">Constructor is private as the class implements the Singleton design pattern.</a>
	 */
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
}
