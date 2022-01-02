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
import fr.sos.witchhunt.controller.interactions.PlayerCreatorController;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;
import fr.sos.witchhunt.view.gui.Window;

public class MatchSetupPanel extends JPanel implements InputSource {
	 private InputMediator inputMediator;
	 
	 private boolean changed=true;
	 private Tabletop tabletop;
	 private JButton playersAddedButton;
	 private JPanel playerCreators;
	 
	 private PlayerCreatorPanel activePCP;
	 
	 private int insetsSize = 20;
	 
	 public static final int playerCreatorPanelHeigth = Window.HEIGHT/8;
	 public static final int playerCreatorPanelWidth = Window.WIDTH;
	 
	 private JLabel instructionsLabel;
	 
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
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Window.WIDTH,Window.HEIGHT);
	}
	
	@Override public Insets getInsets() {
		return new Insets(insetsSize,insetsSize,insetsSize,insetsSize);
	}
	
	public void setInputMediator(InputMediator inputMediator) {
		this.inputMediator=inputMediator;
	}
	
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
	
	public void addedOne(Player p) {
		if(this.activePCP!=null) this.activePCP.addedOne(p);
		this.activePCP=null;
		this.changed=true;
		this.update();
	}

	public void init() {
		
	}
	
	public PlayerCreatorPanel getActivePCP() {
		return this.activePCP;
	}

	@Override
	public void post(String str) {
		
	}

	@Override
	public void post() {

	}

	@Override
	public void post(Player p) {
		inputMediator.receive(p);
	}
	
	
}
