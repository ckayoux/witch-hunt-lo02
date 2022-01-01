package fr.sos.witchhunt.view.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.view.gui.scenes.game.GamePanel;
import fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel;
import fr.sos.witchhunt.view.gui.scenes.matchsetup.MatchSetupPanel;

public class Window extends JFrame /*implements InputSource*/ {
	
	public static final int WIDTH = 1600;
	public static final int HEIGHT= 900;
	public static final String defaultTitle="Witch Hunt";

	
	//CONSTRUCTOR
	public Window () {
		this.setTitle(defaultTitle);
		this.setSize(new Dimension(WIDTH,HEIGHT));
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);	
		this.setVisible(true);

		
	}
	
	public MainMenuPanel renderMainMenuPanel() {
		this.setVisible(false);
		MainMenuPanel mmp = new MainMenuPanel();
		this.setContentPane(mmp);
		mmp.init();
		this.pack();
		this.setVisible(true);
		return mmp;
	}
	
	public GamePanel renderGamePanel() {
		this.setVisible(false);
		GamePanel gp = new GamePanel();
		this.setContentPane(gp);
		gp.init();
		this.pack();
		this.setVisible(true);
		return gp;
	}

	public MatchSetupPanel renderMatchSetupPanel(Tabletop t) {
		this.setVisible(false);
		MatchSetupPanel msp = new MatchSetupPanel(t);
		this.setContentPane(msp);
		msp.init();
		this.pack();
		this.setVisible(true);
		return msp;
	}
	

	/*
	@Override
	public void post(String str) {
		inputMediator.receive(str);
	}
	public void post() {
		inputMediator.receive();
	}*/
}

