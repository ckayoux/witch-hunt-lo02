package fr.sos.witchhunt.view.gui.scenes.matchsetup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.sos.witchhunt.controller.interactions.PlayerCreatorController;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>A set of regrouped graphical components used to create a {@link fr.sos.witchhunt.model.players.Player player} or represent an already created one on the GUI view.</b></p>
 * <p>{@link MatchSetupPanel} {@code <#>--} this</p>
 * <p>Only responsible for display, a specialized controller ({@link fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController}) is in charge of collecting input from
 * a PlayerCreatorPanel.</p>
 * @see MatchSetupPanel
 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController
 * @see fr.sos.witchhunt.model.players.Player player
 */
public class PlayerCreatorPanel extends JPanel {
	
	/**
	 * A JLabel indicating the associated Player's {@link fr.sos.witchhunt.model.players.Player#getId() id}.
	 */
	private JLabel nthPlayerLabel;
	/**
	 * A JCheckBox that is checked if the associated Player is {@link fr.sos.witchhunt.model.players.HumanPlayer human-controlled}.
	 */
	private JCheckBox isHumanCB;
	/**
	 * A JTextField containing the associated Player's {@link fr.sos.witchhunt.model.players.Player#getName()}.
	 */
	private JTextField nameField;
	/**
	 * A JButton used to validate the information of a new Player and add it to the match.
	 */
	private JButton createButton;
	
	private final static String defaultCreateButtonText="+";
	private final static Color defaultCreateButtonFg=null;
	private final static Color warningCreateButtonFg=Color.red;
	
	/**
	 * <b>The instance of {@link fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController} collecting input from and updating this component.</b>
	 * <p>Instantiated and set by the master {@link MatchSetupPanel}.</p>
	 */
	private PlayerCreatorController controller = null;
	
	private int nameFieldWidth = MatchSetupPanel.playerCreatorPanelWidth/7;
	private int nameFieldHeigth = MatchSetupPanel.playerCreatorPanelHeigth/2;
	private int nameFieldColumns = 80;
	
	/**
	 * <p>At instanciation, sets up the content pane's layout and size, instantiates each component ({@link #nthPlayerLabel player id label}, {@link #isHumanCB human-controlled switch}, 
	 * {@link #nameField name field} and {@link #createButton}) and configures them based on the count of already existing players.</p>
	 * @param nthPlayer The existing or future player represented by this PlayerCreatorPanel is the n<i>th</i> one
	 * @param nthCPUPlayer The existing or future player represented by this PlayerCreatorPanel is the n<i>th</i> {@link fr.sos.witchhunt.model.players.CPUPlayer CPU Player}.
	 */
	public PlayerCreatorPanel (int nthPlayer,int nthCPUPlayer) {
		super();
		this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		this.setPreferredSize(new Dimension(MatchSetupPanel.playerCreatorPanelWidth,MatchSetupPanel.playerCreatorPanelHeigth));
		this.setMaximumSize(new Dimension(MatchSetupPanel.playerCreatorPanelWidth,MatchSetupPanel.playerCreatorPanelHeigth));
		
		this.nthPlayerLabel=new JLabel("Player "+nthPlayer);
		this.nthPlayerLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.add(this.nthPlayerLabel);
		this.add(Box.createHorizontalGlue());
		
		this.nameField=new JTextField(nameFieldColumns);
		this.nameField.setMaximumSize(new Dimension(nameFieldWidth,nameFieldHeigth));
		this.nameField.setMaximumSize(new Dimension(nameFieldWidth,nameFieldHeigth));
		this.nameField.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.nameField.setEnabled(false);
		this.nameField.setText("CPU "+nthCPUPlayer);
		this.add(this.nameField);
		this.add(Box.createHorizontalGlue());
		
		this.isHumanCB=new JCheckBox("Human-controlled ?");
		this.isHumanCB.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.add(this.isHumanCB);
		this.add(Box.createHorizontalGlue());
		
		this.createButton=new JButton(defaultCreateButtonText);
		this.createButton.setMargin(new Insets(10,13,10,13));
		this.createButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.add(this.createButton);
	}
	
	/**
	 * <b>Called when the master {@link MatchSetupPanel} is notified that a Player has been added (from the GUI View or any other concurrent one. See {@link MatchSetupPanel#addedOne(Player)}.</b>
	 * <p>- {@link fr.sos.witchhunt.controller.interactions.PlayerCreatorController#destroy() Destroys the controller} listening on this component.</p>
	 * <p>- Configures each child graphical component with the newly added Player's information.</p>
	 * <p>- Disables and locks these components so their state become permanent and intangible.</p> 
	 * @param p The instance of {@link fr.sos.witchhunt.model.players.Player Player} that was added by the GUI View or another concurrent one.
	 * @see MatchSetupPanel#addedOne(Player)
	 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController#destroy() PlayerCreatorController::destroy()
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#createPlayer(int, java.util.List, boolean) ConcreteInputMediator::createPlayer(int, java.util.List, boolean)
	 * @param p The {@link fr.sos.witchhunt.model.players.Player Player} who has been added to the match.
	 */
	public void addedOne(Player p) {
		if(this.controller != null) this.controller.destroy();
		this.controller=null;
		this.nameField.setText(p.getName());
		this.isHumanCB.setSelected(p instanceof HumanPlayer);
		this.nthPlayerLabel.setEnabled(false);
		this.nameField.setEnabled(false);
		this.isHumanCB.setEnabled(false);
		this.remove(createButton);
	}
	
	/**
	 * <p><b>Changes the {@link #createButton "create the player"} button, setting its text to the given error message and changing its foreground color to {@link #warningCreateButtonFg}.</b></p>
	 * <p>Used to stop displaying an alert when the user changes an invalid player's parameters.</p>
	 * @param warningMsg The warning message to display into the button.
	 * @see #createButton
	 * @see #warningCreateButtonFg
	 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController
	 */
	public void reject(String warningMsg) {
		this.createButton.setForeground(warningCreateButtonFg);
		this.createButton.setText(warningMsg);
	}
	
	/**
	 * <p><b>Resets the {@link #createButton "create the player"} button with its {@link #defaultCreateButtonText default text} and its {@link #defaultCreateButtonFg default foreground color}.</b></p>
	 * <p>Used to stop displaying an alert when the user changes an invalid player's parameters.</p>
	 * @see #createButton
	 * @see #defaultCreateButtonFg
	 * @see #defaultCreateButtonText
	 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController
	 */
	public void resetCreateButton() {
		this.createButton.setForeground(defaultCreateButtonFg);
		this.createButton.setText(defaultCreateButtonText);
	}

	
	public JCheckBox getIsHumanCB() {
		return isHumanCB;
	}
	
	/**
	 * @param c Value for field {@link #controller}.
	 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController
	 */
	public void setController(PlayerCreatorController c) {
		this.controller=c;
	}

	public JTextField getNameField() {
		return nameField;
	}

	public JButton getCreateButton() {
		return createButton;
	}
}
