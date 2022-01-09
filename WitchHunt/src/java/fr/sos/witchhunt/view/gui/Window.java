package fr.sos.witchhunt.view.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.view.gui.scenes.game.GamePanel;
import fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel;
import fr.sos.witchhunt.view.gui.scenes.matchsetup.MatchSetupPanel;

/**
 * <p><b>The window containing the extension of JPanel corresponding to the current {@link fr.sos.witchhunt.view.gui.scenes scene}.</b></p>
 * <p>Will be used as a frame for all Graphical User Interface displays.</p>
 * <p>Only one window is manipulated by the Graphical User Interface. Its content pane, title and size can change during the execution.</p>
 * @see GUIView The GUIView class instantiates and updates the window on scene change. 
 */
public class Window extends JFrame /*implements InputSource*/ {
	/**
	 * <b>Initial width of the window.</b>
	 */
	public static final int WIDTH = 1600;
	/**
	 * <b>Initial heigth of the window.</b>
	 */
	public static final int HEIGHT= 900;
	/**
	 * <b>Initial title of the window. The title can change depending on the scene.</b>
	 */
	public static final String defaultTitle="Witch Hunt";

	//CONSTRUCTOR
	public Window () {
		this.setTitle(defaultTitle);
		this.setSize(new Dimension(WIDTH,HEIGHT));
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);	
		this.setVisible(true);

		
	}
	
	/**
	 * <b>Changes to the {@link fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel Main Menu scene}.</b>
	 * @see GUIView#gotoMainMenuPanel()
	 * @see fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel MainMenuPanel
	 */
	public MainMenuPanel renderMainMenuPanel() {
		this.setVisible(false);
		MainMenuPanel mmp = new MainMenuPanel();
		this.setContentPane(mmp);
		this.setTitle(defaultTitle);
		mmp.init();
		this.pack();
		this.setVisible(true);
		return mmp;
	}
	
	/**
	 * <b>Changes to the {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel Game scene}.</b>
	 * @see GUIView#gotoGamePanel()
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel
	 */
	public GamePanel renderGamePanel() {
		this.setVisible(false);
		GamePanel gp = new GamePanel();
		this.setContentPane(gp);
		gp.init();
		this.pack();
		this.setVisible(true);
		return gp;
	}

	/**
	 * <b>Changes to the {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.MatchSetupPanel Match setup (players creation) scene}.</b>
	 * @see GUIView#gotoMatchSetupPanel()
	 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.MatchSetupPanel MatchSetupPanel
	 * @param t The {@link fr.sos.witchhunt.model.flow.Tabletop instance of Tabletop} which is going to be set up.
	 */
	public MatchSetupPanel renderMatchSetupPanel(Tabletop t) {
		this.setVisible(false);
		MatchSetupPanel msp = new MatchSetupPanel(t);
		this.setContentPane(msp);
		msp.init();
		this.pack();
		this.setVisible(true);
		return msp;
	}
}

