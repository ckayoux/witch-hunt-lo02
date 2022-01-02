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

public class PlayerCreatorPanel extends JPanel {
	
	private JLabel nthPlayerLabel;
	private JCheckBox isHumanCB;
	private JTextField nameField;
	private JButton createButton;
	
	private final static String defaultCreateButtonText="+";
	private final static Color defaultCreateButtonFg=null;
	private final static Color warningCreateButtonFg=Color.red;
	
	private PlayerCreatorController controller = null;
	
	private int nameFieldWidth = MatchSetupPanel.playerCreatorPanelWidth/7;
	private int nameFieldHeigth = MatchSetupPanel.playerCreatorPanelHeigth/2;
	private int nameFieldColumns = 80;
	
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
	
	public void resetCreateButton() {
		this.createButton.setForeground(defaultCreateButtonFg);
		this.createButton.setText(defaultCreateButtonText);
	}
	
	public void reject(String warningMsg) {
		this.createButton.setForeground(warningCreateButtonFg);
		this.createButton.setText(warningMsg);
	}
	
	public JCheckBox getIsHumanCB() {
		return isHumanCB;
	}
	
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
