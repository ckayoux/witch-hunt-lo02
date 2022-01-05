package fr.sos.witchhunt.view.std;

import java.util.List;
import java.util.concurrent.Callable;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;

/**
 * <p><b>Used to create one instance of {@link fr.sos.witchhunt.model.players.Player Player} based on standard input.</b></p>
 * <p>A specific class is required as the protocol is different from the one used for the {@link fr.sos.witchhunt.controller.interactions.PlayerCreatorController the GUI} :
 * it is sequential, several answers have to be provided in a defined order.
 * - If the minimum players count is already reached, do you want to add one more ?
 * - Should this player be human-controlled ?
 * - If yes, how should we call them ?</p>
 * <p>At the same time an input Consumer and a Producer {@link fr.sos.witchhunt.view.InputSource (see InputSource)} :
 * Standard-input is collected using a {@link #slaveInputMediator slave input mediator} to control an instance of {@link InterruptibleStdInput InterruptibleStdInput}.
 * A Player is therefore instantiated based on the required information, then, {@link #post(Player)} to the {@link #masterInputMediator master input mediator}.
 * Master InputMediator :: {@link fr.sos.witchhunt.controller.ConcreteInputMediator#createPlayer(int, List, boolean) createPlayer} <code>----></code>StdPlayerCreator :: call <code>--request--></code> Slave InputMediator  <code>--request--></code>  {@link InterruptibleStdInput}.
 * Master InputMediator :: {@link fr.sos.witchhunt.controller.ConcreteInputMediator#receive(Player) receive(Player)} <code> <--Player-- </code> StdPlayerCreator :: call <code><--processed-answers--</code> Slave InputMediator  <code><--Strings--</code>  {@link InterruptibleStdInput}.</p>  
 * <p>{@link fr.sos.witchhunt.controller.ConcreteInputMediator#createPlayer(int, List, boolean) Started into a Thread which can be interrupted}.</p>
 * <p>In concurrence with {@link fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController (Controller for the graphical players creation interface)}.
 * The first one to {@link #post(Player) send a non-null instance of Player} rules and provokes its concurrent's interruption.</p>
 * @see fr.sos.witchhunt.view.InputSource InputSource
 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
 * @see fr.sos.witchhunt.controller.InputMediator#createPlayer(int, List, boolean) InputMediator::createPlayer(int, List, boolean)
 * @see fr.sos.witchhunt.model.players.Player Player
 * @see fr.sos.witchhunt.model.flow.Tabletop Tabletop is the starting point of players creation 
 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController in concurrence with PlayerCreatorController (Controller for the graphical players creation interface).
 */
public class StdPlayerCreator implements Runnable ,Callable<Player>,InputSource{
	/**
	 * The {@link fr.sos.witchhunt.controller.InputMediator slave input mediator, from which user-input is collected} in order to create a Player on its basis.
	 * @see #call()
	 */
	private InputMediator slaveInputMediator;
	/**
	 * The {@link fr.sos.witchhunt.controller.InputMediator master input mediator, to which the created Player} is {@link #post(Player) sent.} Acts as an input Consumer.
	 * @see #run()
	 */
	private InputMediator masterInputMediator;
	/**
	 * The {@link StdView console view}, used for displaying prompts and error messages related to the input requests.
	 */
	private StdView console;
	
	/**
	 * The {@link fr.sos.witchhunt.model.players.Player#getId() id} of the potential future player. 
	 */
	private int playerId;
	/**
	 * The {@link fr.sos.witchhunt.model.flow.Tabletop#getTakenNames() list of all already-taken player names.}
	 */
	private List<String> takenNames;
	/**
	 * <i>true</i> if the minimum players count has been reached, as the creation of a player is not mandatory.
	 * Induces addition of a yes-no question asking whether another player should be added or not.
	 */
	private boolean optional = false;
	
	/**
	 * @param master Value for field {@link #masterInputMediator}, the {@link fr.sos.witchhunt.controller.ConcreteInputMediator#createPlayer(int, List, boolean) calling} {@link fr.sos.witchhunt.controller.InputMediator input mediator}.
	 * @param slave Value for field {@link #slaveInputMediator}, the one used to perform several sequential input requests.
	 * @param console Value for field {@link #console}.
	 * @param playerId Value for field {@link #playerId}
	 * @param takenNames Value for field {@link #takenNames}
	 * @param optional Value for field {@link #optional}
	 */
	public StdPlayerCreator(InputMediator master,InputMediator slave,StdView console,int playerId,List<String> takenNames,boolean optionnal) {
		this.masterInputMediator=master;
		this.slaveInputMediator = slave;
		this.optional=optionnal;
		this.console=console;
		this.playerId=playerId;
		this.takenNames=takenNames;
	}
	
	/**
	 * <p>Tries creating a {@link fr.sos.witchhunt.model.players.Player player} based on a sequence of several input requests. :
	 * - If {@link #optional}, do you want to add one more one more player or do you prefere starting the game right now ?
	 * - Should this player be human-controlled ?
	 * - If yes, how should we call them ?</p>
	 * <p>Checks for the validity of provided information.</p>
	 * @return The Player created on the basis of the provided information, or <code>null</code> if it was interrupted.
	 */
	@Override
	public Player call() throws InterruptedException {
		boolean doCreate = true;
		if(optional) {
			try {
				doCreate = slaveInputMediator.answerYesNoQuestion();
			}
			catch(InterruptedException e) {
				throw e;
			}
		}
		boolean human;
		if(doCreate) {
			console.log("\tPlayer "+Integer.toString(playerId)+" :");
			console.yesNoQuestion("\tHuman-controlled ?");
			try {
				human = slaveInputMediator.answerYesNoQuestion();
			}
			catch(InterruptedException e) {
				throw e;
			}
			Player output;
			String name="";
			if(human) {
				HumanPlayer temp;
				console.log("\tName :");
				boolean correct;
				do {
					name = slaveInputMediator.getStringInput();
					if(takenNames.contains(name)) {
						console.log("\tThis name is already taken.");
						correct=false;
					}
					else if (name.contains("CPU")) {
						console.log("\tThis name is reserved.");
						correct=false;
					}
					else {
						correct=true;
					}
					if(!correct) console.log("\tPlease choose another one :");
				}
				while(!correct);
			}
			return slaveInputMediator.createPlayer(playerId,name,human);
		}
		else {
			return null;
		}
	  }
	
	/**
	 * If a all questions got valid answers, {@link #post(Player) sends} the Player {@link #call() created on the basis of the collected standard input}
	 * to the {@link #masterInputMediator master input mediator}.
	 */
	@Override
	public void run() {
		Player createdFromStdInput=null;
		try {
			createdFromStdInput=this.call();
			post(createdFromStdInput);
		}
		catch(InterruptedException e) {
		}
		finally {
			this.slaveInputMediator.interruptStdInput();
		}
	}
	/**
	 * @deprecated Used by other classes implementing {@link fr.sos.witchhunt.view.InputSource InputSource}.
	 */
	@Override
	public void post(String str) {
	}
	/**
	 * @deprecated Used by other classes implementing {@link fr.sos.witchhunt.view.InputSource InputSource}.
	 */
	@Override
	public void post() {
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void post(Player p) {
		masterInputMediator.receive(p);
	}
	
}
