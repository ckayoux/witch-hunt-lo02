package fr.sos.witchhunt.controller;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.CPUPlayer;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.gui.GUIView;
import fr.sos.witchhunt.view.std.InterruptibleStdInput;
import fr.sos.witchhunt.view.std.StdPlayerCreator;
import fr.sos.witchhunt.view.std.StdView;

/**
 * <p><b>Central class connecting the application's {@link fr.sos.witchhunt.model 'model'} and {@link fr.sos.witchhunt.view 'view'} levels in terms of user-input.</b></p>
 * <p><b>Allows keeping the model independent from the view, for a Model-View-Controller approach.</b></p>
 * <p>In charge of gathering user-input from <b>one of the concurrent views</b>.</p>
 * <p>Works with semaphore-inspired latches. This class can <b>send input requests to all known views</b> and <b>sleep itself until it receives</b> valid input from a view.
 * When a stranger class sends an input request through an instance of this class, it will also put itself on hold until valid input is received.
 * <p>The following classes use this class to send their input requests through a class implementing <p>{@link InputMediator}</b>, specifying each type
 * of input to be collected : {@link fr.sos.witchhunt.controller.core classes of the controller.core package}, {@link fr.sos.witchhunt.model.players.HumanPlayer HumanPlayer},
 * {@link fr.sos.witchhunt.model.flow.Tabletop Tabletop} and {@link fr.sos.witchhunt.view.std.StdPlayerCreator StdPlayerCreator}.</p>
 * <p>The following classes are sources of input for this class : {@link fr.sos.witchhunt.controller.core most classes of the controller.interactions package} and {@link fr.sos.witchhunt.view.std.InterruptibleStdInput InterruptibleStdInput}.
 * See {@link fr.sos.witchhunt.view.InputSource InputSource}, the interface implemented by user-input-gathering view classes.</p>
 * <p>This class is also responsible for <b>interrupting input collect</b> on all concurrent views once a first response is received.</p>
 * <p>Knows the central class of each concurrent view ({@link fr.sos.witchhunt.view.std.StdView StdView} and {@link fr.sos.witchhunt.view.std.GUIView GUIView}).
 * Also knows {@link fr.sos.witchhunt.view.std.InterruptibleStdInput InterruptibleStdInput}.</p>
 * <p>The main input mediator is instantiated by {@link fr.sos.witchhunt.controller.core.Application Application}.</p>
 *
 * @see fr.sos.witchhunt.model Classes of the model package can put themselves on hold, awaiting for user-input, by calling a public method on an {@link InputMediator}.
 * @see fr.sos.witchhunt.model.players.HumanPlayer HumanPlayer
 * @see fr.sos.witchhunt.model.players.PlayerInputRequester PlayerInputRequester
 * @see fr.sos.witchhunt.model.flow.Tabletop Tabletop
 * 
 * @see fr.sos.witchhunt.controller.core.Game User input in the main menu is also collected this way.
 * 
 * @see fr.sos.witchhunt.view.std.StdPlayerCreator StdPlayerCreator uses a slave input mediator to collect input
 * 
 * 
 * @see fr.sos.witchhunt.view.InputSource InputSource is implemented by each class able to send collected input to an InputMediator.
 * @see fr.sos.witchhunt.view.std.InterruptibleStdInput InterruptibleStdInput is a non-blocking console input collector
 * @see fr.sos.witchhunt.view.std.StdPlayerCreator StdPlayerCreator sends to a master input mediator its created players
 * @see fr.sos.witchhunt.controller.interactions Classes of the controller.interactions package that implement {@link fr.sos.witchhunt.view.InputSource InputSource} post collected input to an input mediator.
 * 
 * @see fr.sos.witchhunt.view The view is updated when asking for input and when receiving invalid or valid input.
 * @see fr.sos.witchhunt.view.std.StdView StdView, the central class of the Console view. Used for displaying error messages and prompts.
 * @see fr.sos.witchhunt.view.std.GUIView GUIView, the central class of the Graphical User Interface. Display-update directives are sent once input is received.
 *
 *
 * @see DisplayMediator Stranger classes send their display requests through an instance of a class implementing DisplayMediator
 * @see InputMediator User-input is gathered from the view by another component, the InputMediator
 */
public final class ConcreteInputMediator implements InputMediator {

	//Fields
	/**
	 * Calling <code>latch.await()</code> will sleep the input mediator until input is {@link #receive()} received.
	 */
	private CountDownLatch latch = new CountDownLatch(1) ;
	/**
	 * Knows the central class of the Console view.
	 * It is used when asking for input, displaying prompts and sending error messages.
	 * @see fr.sos.witchhunt.view.std.StdView StdView
	 * @see fr.sos.witchhunt.view.std
	 */
	private StdView console;
	/**
	 * Knows the central class of the GUI view.
	 * The GUI can be updated once input is received.
	 * @see fr.sos.witchhunt.view.gui.GUIView GUIView
	 * @see fr.sos.witchhunt.view.gui
	 */
	private GUIView gui;
	/**
	 * {@link fr.sos.witchhunt.view.std.InterruptibleStdInput InterruptibleStdInput} implements Runnable and can be interrupted when input is collected by another view.
	 * @see fr.sos.witchhunt.view.std.InterruptibleStdInput InterruptibleStdInput
	 * @see #getStringInput() 
	 */
	private Thread stdInputThread;
	/**
	 * {@link fr.sos.witchhunt.view.std.StdPlayerCreator StdPlayerCreator} implements Runnable and can be interrupted when a player is created by another view.
	 * @see fr.sos.witchhunt.view.std.StdPlayerCreator
	 * @see #createPlayer(int, List, boolean)
	 */
	private Thread activeStdPlayerCreatorThread;
	/**
	 * Stranger classes {@link fr.sos.witchhunt.view.InputSource#post(String) posting} Strings or integers to an input mediator will drop their message here.
	 * @see fr.sos.witchhunt.view.InputSource
	 * @see fr.sos.witchhunt.view.InputSource#post(String)
	 * @see #getStringInput()
	 * @see #getIntInput()
	 * @see #receive(String)
	 * @see #receive(int)
	 */
	private String receivedString;
	/**
	 * Stranger classes posting instances of {@link fr.sos.witchhunt.model.players.Player Player} to an input mediator will drop them here.
	 * @see #receive(Player)
	 * @see #getPlayerInput()
	 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController
	 * @see fr.sos.witchhunt.view.std.StdPlayerCreator StdPlayerCreator
	 */
	private Player receivedPlayer;

	/**
	 * Custom error messages are displayed depending on how many successive invalid responses where received.
	 * @see fr.sos.witchhunt.view.std.StdView#logWrongMenuChoiceMessage(int, String, int) StdView::logWrongMenuChoiceMessage(int, String, int)
	 */
	private int timesWrong=0;
	
	/**
	 * {@inheritDoc}
	 * Calls itselfs recursively until valid input is gathered.
	 * Once valid input is received, the {@link #gui GUI} is updated and the chosen option is logged into the {@link #console}.
	 */
	@Override
	public int makeChoice(Menu m) {
		int choice;
		boolean correct;
		gui.makeChoice(m);
		int n = m.getOptionsCount();
		choice = getIntInput();
		if(!(1 <= choice && choice <= n)) {
			correct = false;
			timesWrong++;
			String prompt = "Please enter an integer in the range 1.."+Integer.toString(n)+" :" ;
			console.logWrongMenuChoiceMessage(timesWrong,prompt,n);
			return makeChoice(m);
		}else {
			this.gui.choiceHasBeenMade(m.getNthOption(choice));
			console.log(choice + " : "+m.getNthOption(choice).toString()); //logging choice correspondance in console
			timesWrong=0;
			console.crlf();
			return choice;
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 * <b>{@link fr.sos.witchhunt.view.std Console} only.</b>
	 */
	@Override
	public boolean answerYesNoQuestion() throws InterruptedException {
		char input = getStringInput().toLowerCase().charAt(0);
		if(input=='y') {
			return true;
		}
		else if (input=='n') {
			return false;
		}
		else {
			console.logInvalidYesNoQuestionAnswerMessage(); 
			return answerYesNoQuestion();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void wannaContinue() {
		console.logContinueMessage();
		gui.wannaContinue();
		try {
			getStdInput();
		} catch (InterruptedException e) {

		}
		this.gui.choiceHasBeenMade(1);
		console.crlf();
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>Input can be sent by {@link fr.sos.witchhunt.view.std.StdPlayerCreator StdCreator}, which uses another instance of this class for creating
	 * a player based on standard input, and sends its created players to this one.</p>
	 * <p>Input can also be sent by {@link fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController}, which creates players
	 * based on the input gathered by the graphical user interface.</p>
	 * <p>The first {@link fr.sos.witchhunt.view.InputSource} to {@link fr.sos.witchhunt.view.InputSource#post(Player) send a Player} rules over all others, which are interrupted.
	 * <b>Called by {@link fr.sos.witchhunt.model.flow.Tabletop Tabletop} to instantiate a Player into the game.</b>
	 * 
	 *  @see #getPlayerInput()
	 *  @see #receive(Player)
	 */
	@Override
	public Player createPlayer(int id,List<String> chosenNames,boolean optionnal) {
		ConcreteInputMediator slave = new ConcreteInputMediator();
		//players creation is a sequential process in the console, while it isn't for the graphical user interface.
		//therefore, another input mediator has to be used.
		slave.setConsole(console);
		slave.setGui(gui);
		StdPlayerCreator spc = new StdPlayerCreator(this,slave,console,id,chosenNames,optionnal);
		activeStdPlayerCreatorThread= new Thread(spc);
		activeStdPlayerCreatorThread.start();
		
		Player output=getPlayerInput();
		return output;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player createPlayer(int id, String name, boolean isHuman) {
		Player output;
		if(isHuman) {
			HumanPlayer houtput =new HumanPlayer(name,id);
			houtput.setInputMediator(this);
			output=houtput;
		}
		else  {
			Tabletop.getInstance().incrementCPUPlayersNumber();
			output=new CPUPlayer(id,Tabletop.getInstance().getCPUPlayersNumber());
		}
		return output;
	}
	
	/**
	 * Instantiates an {@link fr.sos.witchhunt.view.std.InterruptibleStdInput InterruptibleStdInput} in its own thread in order to gather user-input from the console non-blockingly.
	 * Awaits for an answer by decrementing a {@link #latch semaphore}.
	 * @see #interruptStdInput()
	 * @throws InterruptedException 
	 */
	public void getStdInput() throws InterruptedException {
		stdInputThread= new Thread(new InterruptibleStdInput(this,console));
		stdInputThread.start();
		latch.await();
	}
	
	/**
	 * <p><b>Puts this class on the hold until it {@link #receive(String) receives a String}, then returns it.</b></p>
	 * <p>Can be called by foreign classes when used as a slave input mediator for building a result step by step based on plural sequential answers. (see {@link fr.sos.witchhunt.view.std.StdPlayerCreator#StdPlayerCreator(InputMediator, InputMediator, StdView, int, List, boolean) StdPlayerCreator).</p>
	 * return #receivedString once it is nonempty.
	 */
	@Override
	public String getStringInput() throws InterruptedException {
		getStdInput();
		if (receivedString==null||receivedString.equals("")) {
			console.logInputWasExpectedMessage();
			return getStringInput();
		}
		else {
			String output=this.receivedString;
			this.receivedString=null;
			return output;
		}
	}
	
	/**
	 * <p><b>Puts this class on the hold until it {@link #receive(int)) receives an integer}, then returns it.</b></p>
	 * @return {@link #receivedString} parsed as an integer. -1 if parsing failed.
	 * @see #getStringInput() Calls getStringInput()
	 */
	private int getIntInput() {
		try {
			int got = Integer.parseInt(getStringInput());
			return got;
		}
		catch (final NumberFormatException e) {
			return -1;
		}
		catch (InterruptedException e) {
			return -1;
		}
			
	}
	
	/**
	 * <p><b>Puts this class on the hold until it {@link #receive(Player) receives an instance of Player}, then returns it.</b></p>
	 * @see #createPlayer(int, List, boolean)
	 */
	private Player getPlayerInput() {
		try{latch.await();}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		Player output = receivedPlayer;
		this.receivedPlayer=null;
		return output;
	}
	
	/**
	 * {@inheritDoc}
	 * Wakes the input mediator up and {@link #interruptStdInput() interrupts input from the console}.
	 * @see #wake()
	 * @see #interruptStdInput()
	 * @see fr.sos.witchhunt.view.InputSource InputSource
	 * @see fr.sos.witchhunt.view.InputSource#post(String) InputSource::post(String)
	 */
	@Override
	public synchronized void receive(String str) {
		this.wake();
		this.interruptStdInput();
		this.receivedString=str;
		latch = new CountDownLatch(1);
	}
	
	/**
	 * {@inheritDoc}
	 * Wakes the input mediator up and {@link #activeStdPlayerCreatorThread interrupts the creation of a player in the console.}.
	 * 
	 * @see #latch
	 * @see #activeStdPlayerCreatorThread
	 * @see fr.sos.witchhunt.view.InputSource InputSource
	 * @see fr.sos.witchhunt.view.InputSource#post(Player) InputSource::post(Player)
	 */
	@Override
	public synchronized void receive(Player p) {
		this.latch.countDown();
		if(this.activeStdPlayerCreatorThread!=null && this.activeStdPlayerCreatorThread.isAlive()) {
			this.activeStdPlayerCreatorThread.interrupt();
			this.activeStdPlayerCreatorThread=null;
		}
		this.receivedPlayer=p;
		latch = new CountDownLatch(1);
	}
	
	/**
	 * {@inheritDoc}
	 * Wakes the input mediator up and {@link #interruptStdInput() interrupts input from the console}.
	 * @see #wake()
	 * @see #interruptStdInput()
	 * @see fr.sos.witchhunt.view.InputSource InputSource
	 * @see fr.sos.witchhunt.view.InputSource#post(String) InputSource::post(String)
	 */
	@Override
	public synchronized void receive(int i) {
		this.receive(Integer.toString(i));
	}
	
	/**
	 * {@inheritDoc}
	 * Wakes the input mediator up and {@link #interruptStdInput() interrupts input from the console}.
	 * @see #wake()
	 * @see #interruptStdInput()
	 * @see fr.sos.witchhunt.view.InputSource InputSource
	 * @see fr.sos.witchhunt.view.InputSource#post() InputSource::post()
	 */
	@Override
	public synchronized void receive() {
		this.wake();
		this.interruptStdInput();
		latch = new CountDownLatch(1);
	}
	
	/**
	 * Interrupts input from the console if it is waiting for input.
	 * @see #stdInputThread
	 * @see fr.sos.witchhunt.view.std.InterruptibleStdInput
	 */
	@Override
	public void interruptStdInput() {
		if(stdInputThread != null) {
			if(stdInputThread.isAlive()) {
				stdInputThread.interrupt();
				stdInputThread = null;
			}
		}
	}

	/**
	 * <p>Wakes the input mediator up.</p>
	 * @see #latch
	 */
	private void wake() {
		this.latch.countDown();
	}

	public void setConsole(StdView console) {
		this.console=console;
	}
	public void setGui(GUIView gui) {
		this.gui= gui;
	}
	
}
