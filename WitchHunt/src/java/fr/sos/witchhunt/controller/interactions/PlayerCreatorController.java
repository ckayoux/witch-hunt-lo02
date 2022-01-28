package fr.sos.witchhunt.controller.interactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;
import fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel;

/**
 * <p><b>Gathers user-input from an instance of {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel PlayerCreatorPanel} active on the Graphical User Interface.</b></p>
 * <p>When a user clicks on a button, {@link #post(Player) sends} to the {@link fr.sos.witchhunt.controller.InputMediator InputMediator} a newly created {@link fr.sos.witchhunt.model.players.Player Player}
 * as long as the user has chosen a valid combination of characteristics.</p>
 * <p>User-chosen characteristics (whether they are a CPU-controlled player or not, and in this case their name) are gathered on the basis of a {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel PlayerCreatorPanel's JComponents}.</p>
 * <p>Intangible characteristics (the player's id, the name of a CPU-controlled player, and the validity of a Human player's name) are determined on the basis of 
 * values stored in the {@link fr.sos.witchhunt.model.flow.Tabletop#getInstance() match's instance}.</p>
 * <p>Can be in concurrence with {@link fr.sos.witchhunt.view.std.StdPlayerCreator the console version of the player creation interface} : <code>{@link fr.sos.witchhunt.controller.ConcreteInputMediator#createPlayer(int, List, boolean) see ConcreteInputMediator::createPlayer(int, List, boolean)}</code>.
 * The first instance of <code>{@link fr.sos.witchhunt.view.InputSource InputSource}</code> to {@link fr.sos.witchhunt.view.InputSource#post(Player) post an instance of} {@link fr.sos.witchhunt.model.players.Player Player} 
 * rules over all others, interrupting them and inviting them to create another.</p>
 * 
 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel PlayerCreatorPanel
 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getIsHumanCB() A JCheckbox indicating whether the new player is going to be Human-controlled or not
 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getNameField() A JTextField containing the new player's future name
 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getCreateButton() A JButton that is clicked when the user wants to finalize the creation of a player
 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.MatchSetupPanel MatchSetupPanel {@code <#>--} PlayerCreatorPanel
 * 
 * @see fr.sos.witchhunt.model.players.Player Player
 * 
 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#createPlayer(int, List, boolean) InputMediator::createPlayer(int, List, boolean)
 * @see #post(Player)
 * @see	fr.sos.witchhunt.view.std.StdPlayerCreator In concurrence with StdPlayerCreator
 * @see fr.sos.witchhunt.view.InputSource InputSource
 */
public class PlayerCreatorController implements InputSource {
	
	/**
	 * <p>The instance of {@link fr.sos.witchhunt.controller.InputMediator InputMediator} that is
	 * {@link fr.sos.witchhunt.controller.ConcreteInputMediator#wait() awaiting} for {@link fr.sos.witchhunt.controller.InputMediator#createPlayer(int, List, boolean) an instance of Player}
	 * and {@link fr.sos.witchhunt.controller.InputMediator#receive(Player) managing the concurrence between all views}.</p>
	 * 
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#wait() InputMediator::wait()
	 * @see fr.sos.witchhunt.controller.InputMediator#createPlayer(int, List, boolean) InputMediator::createPlayer(int, List, boolean)
	 * @see fr.sos.witchhunt.controller.InputMediator#receive(Player) InputMediator::receive(Player)
	 */
	private InputMediator inputMediator;
	/**
	 * The {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel PlayerCreatorPanel} from which user-input is gathered.
	 * @see #pcp
	 * 
	 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getIsHumanCB() A JCheckbox indicating whether the new player is going to be Human-controlled or not
	 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getNameField() A JTextField containing the new player's future name
	 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getCreateButton() A JButton that is clicked when the user wants to finalize the creation of a player
	 */
	private PlayerCreatorPanel pcp;
	
	/**
	 * This controller will propose to create player of this id.
	 * @see fr.sos.witchhunt.model.players.Player#getId() Player::getId()
	 * @see fr.sos.witchhunt.model.flow.Tabletop#getPlayersCount() Match scale, auto-incremented, variable.
	 */
	private int nthPlayer;
	/**
	 * If the new Player is going to be a CPU-controlled one, they will be the <code>nth</code> one and named after that number.
	 * @see fr.sos.witchhunt.model.flow.Tabletop#getCPUPlayersNumber() Match scale, auto-incremented, variable.
	 */
	private int nthCPUPlayer;
	/**
	 * These names are already taken and the controller will reject creating a Player with a name contained in this list.
	 * @see fr.sos.witchhunt.model.flow.Tabletop#getTakenNames() Match scale, auto-filled, list.
	 */
	private List<String> takenNames;
	
	/**
	 * Becomes <i>true</i> each time the user tries to create a Player with invalid characteristics. 
	 */
	private boolean couldNotCreate=false;
	
	/**
	 * <p><b>Creates the controller and starts listening on each input-gathering JComponent of the given {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel PlayerCreatorPanel} :</b></p>
	 * <p>- {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getIsHumanCB() Its JCheckbox}'s status will indicate whether the new player is going to be Human-controlled or not.</p>
	 * <p>- {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getNameField() Its JTextField} will let the user type in a future human-controlled player's name.
	 * Its content is automatically updated if the user switches to a CPU-controlled player and <i>vice versa</i>.</p>
	 * <p>- When clicked, {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getCreateButton() its JButton} will check the validity of the provided information : if they are valid, it will
	 * {@link #post(Player) create a player}, otherwise, it will display why creation failed. This error message will be hidden as soon as the user changes anything.</p>
	 * 
	 * @param nthPlayer Value for field {@link #nthPlayer}
	 * @param nthCPUPlayer Value for field {@link #nthCPUPlayer}
	 * @param p Value for field {@link #pcp}
	 * @param takenNames Value for field {@link #takenNames}
	 * @param im Value for field {@link #inputMediator}
	 * 
	 * @see #nthPlayer
	 * @see #nthCPUPlayer
	 * @see #pcp
	 * @see #takenNames
	 * @see #inputMediator
	 * @see #couldNotCreate
	 * 
	 * @see java.awt.event.ActionListener
	 * @see javax.swing.event.DocumentListener
	 */
	public PlayerCreatorController(int nthPlayer,int nthCPUPlayer,PlayerCreatorPanel p,List<String> takenNames,InputMediator im) {
		this.inputMediator=im;
		this.nthCPUPlayer=nthCPUPlayer;
		this.nthPlayer=nthPlayer;
		this.takenNames=takenNames;
		this.pcp=p;
		
		p.getIsHumanCB().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!p.getIsHumanCB().isSelected()) {
					p.getNameField().setText("CPU "+nthCPUPlayer);
					p.getNameField().setEnabled(false);
				}
				else {
					p.getNameField().setEnabled(true);
				}
			}
		});
		
		p.getNameField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			 public void changedUpdate(DocumentEvent e) {
				  if(couldNotCreate) this.resetCreateButton();
			  }
			  @Override
			public void removeUpdate(DocumentEvent e) {
				  if(couldNotCreate)  this.resetCreateButton();
			  }
			  @Override
			public void insertUpdate(DocumentEvent e) {
				  if(couldNotCreate) this.resetCreateButton();
			  }
			  
			  private void resetCreateButton() {
				  p.resetCreateButton();
				  couldNotCreate=false;
			  }
		});
		
		p.getNameField().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryCreatingPlayer();
			}
		});
		
		p.getCreateButton().addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryCreatingPlayer();
			}
		});
	}
	
	/**
	 * <p><b>Checks validity of the user-provided characteristics for the future player and performs adequate operation :</b></p>
	 * <p>- Displays a suitable error message if provided information is invalid</p>
	 * <p>- {@link fr.sos.witchhunt.controller.InputMediator#createPlayer(int, String, boolean) Creates} a player based on the provided information and {@link #post(Player) sends it to the input mediator} otherwise.</p>
	 * <p>Called when the {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.PlayerCreatorPanel#getCreateButton() creation button is clicked}.</p>
	 */
	private void tryCreatingPlayer() {
		String name = this.pcp.getNameField().getText();
		boolean isHuman = this.pcp.getIsHumanCB().isSelected();
		if(isHuman) {
			if(name!=null && name.length()>0 && !takenNames.contains(name)&& !name.contains("CPU")) {
				post(inputMediator.createPlayer(nthPlayer,name,isHuman));
			}
			else {
				if(takenNames.contains(name)) {
					this.pcp.reject("Name already taken");
					couldNotCreate=true;
				}
				else if(name.contains("CPU")) {
					this.pcp.reject("Reserved name");
					couldNotCreate=true;
				}
				else {
					this.pcp.reject("No name provided");
					couldNotCreate=true;
				}
			}
		}
		else {
			post(inputMediator.createPlayer(nthPlayer,"",isHuman));
		}
	}

	/**
	 * <b>Destroys each and every listener associated to this controller's {@link #pcp PlayerCreatorPanel's JComponents}.</b>
	 * @see java.awt.event.ActionListener
	 * @see javax.swing.event.DocumentListener
	 */
	public void destroy() {
		for(ActionListener al : this.pcp.getCreateButton().getActionListeners()) this.pcp.getCreateButton().removeActionListener(al);
		for(ActionListener al : this.pcp.getIsHumanCB().getActionListeners()) this.pcp.getIsHumanCB().removeActionListener(al);
		for(ActionListener al : this.pcp.getNameField().getActionListeners()) this.pcp.getNameField().removeActionListener(al);
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
		this.inputMediator.receive(p);
	}
}
