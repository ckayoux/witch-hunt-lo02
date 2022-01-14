package fr.sos.witchhunt.view.gui.scenes.matchsetup;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.controller.interactions.PlayerCreatorController;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;
import fr.sos.witchhunt.view.gui.Window;

/**
 * <p>The GUI Version of the <b>match setup scene</b>, displayed when the game was started.</p>
 * <p><b>Allows the user to create the {@link fr.sos.witchhunt.model.players.Player players} from the GUI View using {@link PlayerCreatorPanel PlayerCreatorPanels} and {@link fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorControllers}.</b></p>
 * <p>Is a simple JPanel managed by a {@link java.awt.BorderLayout}. Made of a JLabel at the top, one or plural {@link PlayerCreatorPanel PlayerCreatorPanels} at the center, and a JButton at the bottom.</p>
 * <p>Created and controlled by the {@link fr.sos.witchhunt.view.gui.GUIView GUI View's central class}. See {@link fr.sos.witchhunt.view.gui.GUIView#gotoMatchSetupPanel() GUIView::gotoMatchSetupPanel()}.</p>
 * <p>Is a visual representation of the {@link fr.sos.witchhunt.model.flow.Tabletop state induced by Tabletop::addPlayers()}.</p>
 * <p>Implements {@link fr.sos.witchhunt.view.InputSource InputSource} as it is able to produce Players and {@link #post(Player) send} them to an instance of {@link fr.sos.witchhunt.controller.InputMediator InputMediator}.</p>
 * @see fr.sos.witchhunt.model.flow.Tabletop Tabletop::addPlayers()
 * 
 * @see PlayerCreatorPanel
 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController
 * @see fr.sos.witchhunt.model.players.Player Player
 * 
 * @see fr.sos.witchhunt.view.gui.GUIView#gotoMatchSetupPanel() GUIView::gotoMatchSetupPanel()
 */
public class MatchSetupPanel extends JPanel implements InputSource {
	/**
	 * <p><b>The instance of {@link fr.sos.witchhunt.controller.InputMediator InputMediator} in charge of sending input requests to this object.</b></p>
	 * <p>Collects input on the active {@link PlayerCreatorPanel PlayerCreatorPanels} using a {@link fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController} in order to create players using
	 * the GUI.</p>
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#createPlayer(int, java.util.List, boolean)
	 * @see PlayerCreatorPanel
	 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController
	 */
	 private InputMediator inputMediator;
	 
	 private boolean changed=true;
	 
	 /**
	  * The instance of {@link fr.sos.witchhunt.model.flow.Tabletop Tabletop} this view is a representation of.
	  * Used to keep the track of the players creation process.
	  * All concurrent views that are able to create players should be synchronized on its basis.
	  */
	 private Tabletop tabletop;
	 
	 /**
	  * <p><b>The JLabel displaying instructions to follow for creating players.</b></p>
	  * <p>Placed at the top.</p>
	  */
	 private JLabel instructionsLabel;
	 /**
	  * <p><b>The JButton allowing to validate the match settings and start the match.</b></p>
	  * <p>Has no effect while the minimum of players has not been reached.</p>
	  * <p>Placed at the bottom of the scene.</p>
	  */
	 private JButton playersAddedButton;
	 /**
	  * <p><b>This JPanel will contain the {@link PlayerCreatorPanel PlayerCreatorPanels} instantiated by this object.</b></p>.
	  * <p>It is placed at the center of the scene, of which it is the largest graphical component.</p>
	  */
	 private JPanel playerCreators;
	 /**
	  * <p><b>A reference to the active {@link PlayerCreatorPanel}, the one that could create a {@link fr.sos.witchhunt.model.players.Player player} who doesn't exist yet.</b></p>
	  */
	 private PlayerCreatorPanel activePCP;
	 
	 /**
	  * Inner margins for this scene's frame.
	  */
	 private int insetsSize = 20;
	 
	 /**
	  * Height of all rendered {@link PlayerCreatorPanel PlayerCreatorPanels}.
	  */
	 public static final int playerCreatorPanelHeigth = Window.HEIGHT/8;
	 /**
	  * Width of all rendered {@link PlayerCreatorPanel PlayerCreatorPanels}.
	  */
	 public static final int playerCreatorPanelWidth = Window.WIDTH;
	
	 /**
	  * <p>At instantiation, configures the layout, the margins, initilizes all three children components ({@link #instructionsLabel}, {@link #playerCreators}, {@link #playersAddedButton}) and places them.</p>
	  * @param t The instance of {@link fr.sos.witchhunt.model.flow.Tabletop Tabletop} that is being set up using this scene. See field {@link #tabletop}.
	  */
	public MatchSetupPanel(Tabletop t) {
		super();
		this.setLayout(new BorderLayout());
		this.tabletop=t;
		this.instructionsLabel = new JLabel();
		this.instructionsLabel.setBorder(new EmptyBorder(0,0,insetsSize,0));
		this.instructionsLabel.setAlignmentY(Component.CENTER_ALIGNMENT); 
		this.instructionsLabel.setFont(new Font("Arial",Font.BOLD,20));
		this.playersAddedButton=new JButton();
		this.playersAddedButton.setMargin(new Insets(10,10,10,10));
		this.playerCreators=new JPanel();
		this.playerCreators.setLayout(new BoxLayout(this.playerCreators,BoxLayout.PAGE_AXIS));
		this.add(this.playersAddedButton,BorderLayout.SOUTH);
		this.add(this.playerCreators,BorderLayout.CENTER);
		this.add(this.instructionsLabel,BorderLayout.NORTH);
	}
	
	/**
	 * <b>The default size for this scene will be {@link fr.sos.witchhunt.view.gui.Window#getWidth() the window's one}.</b>
	 * The window can be resized by the user during this scene.
	 * @see fr.sos.witchhunt.view.gui.Window Window
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Window.WIDTH,Window.HEIGHT);
	}
	
	@Override 
	public Insets getInsets() {
		return new Insets(insetsSize,insetsSize,insetsSize,insetsSize);
	}
	
	/**
	 * @param inputMediator Value for field {@link #inputMediator}
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 */
	public void setInputMediator(InputMediator inputMediator) {
		this.inputMediator=inputMediator;
	}
	
	/**
	 * <p><b>Updates the scene based on the information provided {@link #tabletop by the represented instance of Tabletop}.</b></p>
	 * <p>- The {@link #instructionsLabel instructions label} will be kept up to date to indicate the minimum number of players if it has not been reached yet ; Otherwise, it will indicate the
	 * number of players that can still be added until the maximum is reached. See {@link fr.sos.witchhunt.model.flow.Tabletop#getMaxPlayersNumber() Tabletop::getMaxPlayersNumber()} and {@link fr.sos.witchhunt.model.flow.Tabletop#getMinPlayersNumber() Tabletop::getMinPlayersNumber()}</p>
	 * <p>- The {@link #playersAddedButton validation button} will be activated only once {@link fr.sos.witchhunt.model.flow.Tabletop#getMinPlayersNumber() the minimum number of players} has been reached.
	 * While it has not been reached, its text will be colored in red and updated with instructions to follow in order to be able to start the match.</p>
	 * <p>- While the {@link fr.sos.witchhunt.model.flow.Tabletop#getMaxPlayersNumber() maximum players count} has not been reached, a new {@link PlayerCreatorPanel} will be added each time a player has been added (using this view or a concurrent one).
	 * A {@link fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController} is used to collect input from the {@link #activePCP active PlayerCreatorPanel} and will be disabled as soon as the Player is added.</p>
	 * @see fr.sos.witchhunt.model.flow.Tabletop#getMaxPlayersNumber() Tabletop::getMaxPlayersNumber()
	 * @see fr.sos.witchhunt.model.flow.Tabletop#getMinPlayersNumber() Tabletop::getMinPlayersNumber()
	 * 
	 * @see #instructionsLabel
	 * 
	 * @see fr.sos.witchhunt.controller.interactions.PlayerCreatorController PlayerCreatorController
	 * @see PlayerCreatorPanel
	 * 
	 * @see #playersAddedButton
	 */
	public void update() {
		if (this.changed) {
			if(this.tabletop.getMinPlayersNumber()>tabletop.getPlayersCount()) {
				this.instructionsLabel.setText("Add "+tabletop.getMinPlayersNumber()+" to "+tabletop.getMaxPlayersNumber()+ " players :");
				this.playersAddedButton.setForeground(Color.red);
				int missing = tabletop.getMinPlayersNumber()-tabletop.getPlayersCount();
				this.playersAddedButton.setText("Add at least "+missing+ " more player"+((missing>1)?"s":""));
			}
			else if (tabletop.getMinPlayersNumber()<=tabletop.getPlayersCount() && tabletop.getPlayersCount()<tabletop.getMaxPlayersNumber()) {
				int canAddNMore = tabletop.getMaxPlayersNumber() - tabletop.getPlayersCount();
				this.instructionsLabel.setText("You can add "+canAddNMore+" more player"+((canAddNMore>1)?"s":"")+".");
				this.playersAddedButton.setForeground(null);
				this.playersAddedButton.setText("Let the witch hunt begin !");
				this.playersAddedButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						post((Player)null);
					}
				});
			}
			
			if(tabletop.getPlayersCount()<tabletop.getMaxPlayersNumber()) {
				PlayerCreatorPanel pcp = new PlayerCreatorPanel(tabletop.getPlayersCount()+1,tabletop.getCPUPlayersNumber()+1);
				pcp.setController(new PlayerCreatorController(tabletop.getPlayersCount()+1,tabletop.getCPUPlayersNumber()+1,pcp,tabletop.getTakenNames(),this.inputMediator));
				this.activePCP=pcp;
				this.playerCreators.add(pcp);
			}
			this.updateUI();
			this.changed=false;
		}
	}
	
	/**
	 * <p><b>Notifies this object that a {@link fr.sos.witchhunt.model.players.Player Player} has been added to the match</b> - it doesn't matter whether it has been created using this view or not.</p>
	 * <p>Called by the {@link #inputMediator InputMediator} collecting input via this object to synchronize all concurrent views in charge of creating players.</p>
	 * <p>{@link #update() Updates} the scene based on this notification.
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#createPlayer(int, java.util.List, boolean) ConcreteInputMediator::createPlayer(int, java.util.List, boolean)
	 * @param p The {@link fr.sos.witchhunt.model.players.Player Player} who has been added to the match.
	 */
	public void addedOne(Player p) {
		if(this.activePCP!=null) this.activePCP.addedOne(p);
		this.activePCP=null;
		this.changed=true;
		this.update();
	}

	/**
	 * Fill this methods' body if this scenes requires a particular configuration to take effect only after being rendered.
	 */
	public void init() {
		
	}
	
	public PlayerCreatorPanel getActivePCP() {
		return this.activePCP;
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
		inputMediator.receive(p);
	}
	
	
}
