package fr.sos.witchhunt.view.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame /*implements InputSource*/ {
	
	public static final int WIDTH = 1600;
	public static final int HEIGHT= 900;

	
	//CONSTRUCTOR
	public Window () {
		this.setTitle("Witch Hunt");
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

	public MatchSetupPanel renderMatchSetupPanel() {
		this.setVisible(false);
		MatchSetupPanel msp = new MatchSetupPanel();
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

