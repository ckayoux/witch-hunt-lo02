package fr.sos.witchhunt.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.sos.witchhunt.InputMediator;

public class MatchSetupPanel extends JPanel {
	 private InputMediator inputMediator;
	
	public MatchSetupPanel() {
		super();
		this.setLayout(new BorderLayout());
		this.add(new JLabel("setting up the game"),BorderLayout.CENTER);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Window.HEIGHT,Window.WIDTH);
	}
	
	public void setInputMediator(InputMediator inputMediator) {
		this.inputMediator=inputMediator;
	}

	public void init() {
		
	}
}
