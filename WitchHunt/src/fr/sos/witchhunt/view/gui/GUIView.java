package fr.sos.witchhunt.view.gui;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.model.Menu;

public final class GUIView {

	private InputMediator inputMediator;
	private Window w; 
	private GamePanel gamePanel;
	private MainMenuPanel mainMenuPanel;
	private MatchSetupPanel matchSetupPanel;
	private JPanel currentPanel = null;
	
	//CONSTRUCTOR
	public GUIView (InputMediator im) {
		this.inputMediator=im;
		this.w = new Window ();

		//gotoMainMenuPanel();

	}
	
	
	public void gotoMainMenuPanel() {
		if(this.mainMenuPanel==null) {
			this.gamePanel=null;
			this.matchSetupPanel=null;
			this.mainMenuPanel=this.w.renderMainMenuPanel();
			this.mainMenuPanel.setInputMediator(inputMediator);
		}
		
	}
	public void gotoMatchSetupPanel() {
		if (this.matchSetupPanel==null) {
			this.gamePanel=null;
			this.mainMenuPanel=null;
			this.matchSetupPanel=this.w.renderMatchSetupPanel();
			this.matchSetupPanel.setInputMediator(inputMediator);
		}
		
	}
	
	public void gotoGamePanel() {
		if (this.gamePanel==null){
			this.matchSetupPanel=null;
			this.mainMenuPanel=null;
			this.gamePanel=this.w.renderGamePanel();
			this.gamePanel.setInputMediator(inputMediator);
			this.gamePanel.displayMainNotification(new Notification("Let the witch hunt begin !\n",NotificationType.NORMAL));
		}
	}


	public void displayRoundStartScreen(int roundNumber) {
		if(gamePanel!=null) {
			gamePanel.resetNotificationsBoxes();
			gamePanel.displayMainNotification(new Notification("ROUND "+roundNumber+"\n",NotificationType.NORMAL));
			gamePanel.displaySecondaryNotification(new Notification(NotificationType.SEPARATOR));
			//gamePanel.displaySecondaryNotification(new Notification(NotificationType.CRLF));
		}
		
	}
	
	public void wannaContinue(InputMediator im) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        gamePanel.wannaContinue(im);
		    }
		});
	}
	
	public void resetActionsPanel() {
		if(this.gamePanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 public void run() {
				    gamePanel.resetActionPanel();
				}
			});
		}
		else if (this.mainMenuPanel!=null){
			SwingUtilities.invokeLater(new Runnable() {
				 public void run() {
				    mainMenuPanel.resetActionPanel();
				}
			});
		}
	}

	public void displayMenu(Menu m) {
		if(this.gamePanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 public void run() {
				    gamePanel.displayMenu(m);
				}
			});
		}
		else if (this.mainMenuPanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 public void run() {
				    mainMenuPanel.displayMenu(m);
				}
			});
		}
	}
	
	public void makeChoice(Menu m) {
		if(this.gamePanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 public void run() {
				    gamePanel.makeChoice(m);
				}
			});
		}
		else if (this.mainMenuPanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 public void run() {
				    mainMenuPanel.makeChoice(m);
				}
			});
		}
	}
}
