package fr.sos.witchhunt.view.gui;

import java.util.List;
import java.util.function.Supplier;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.flow.ScoreCounter.ScoreBoard;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;
import fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy;
import fr.sos.witchhunt.view.gui.scenes.game.GamePanel;
import fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel;
import fr.sos.witchhunt.view.gui.scenes.matchsetup.MatchSetupPanel;

/**
 * <p><b>Central class of the GUI view. Possesses high-level display and input methods.</b></p>
 * <p>Instantiates a {@link Window} and updates it to display the extension of JPanel corresponding to the current {@link fr.sos.witchhunt.view.gui.scenes scene}.</p>
 * <p>Also manipulates directly the current {@link fr.sos.witchhunt.view.gui.scenes scene's main class}, calling their lower-level display or input methods.</p>
 * <p>Receives and performs display requests from a {@link fr.sos.witchhunt.controller.DisplayMediator DisplayMediator}.
 * Possesses one display method per situation when display on the GUI is required.
 * Not concerned by some display requests.</p>
 * <p>Receives and performs input requests from the {@link fr.sos.witchhunt.controller.InputMediator InputMediator} with which it is associated.
 * Possesses one input method per type of input request that can be performed by the Graphical Interface.
 * The {@link fr.sos.witchhunt.controller.ConcreteInputMediator InputMediator} keeps the GUI when it receives user input from this view or from other concurrent ones.</p>
 * 
 * @see fr.sos.witchhunt.controller.DisplayMediator DisplayMediator
 * @see fr.sos.witchhunt.controller.ConcreteDisplayMediator ConcreteDisplayMediator
 *
 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
 * @see fr.sos.witchhunt.controller.ConcreteInputMediator ConcreteInputMediator
 *
 * @see Window
 * @see fr.sos.witchhunt.view.gui.scenes
 * @see fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel is the central class of the Main Menu scene
 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.MatchSetupPanel is the central class of the game setup scene (players creation)
 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel is the central class of the ingame scene
 * 
 * 
 */
public final class GUIView {

	/**
	 * <b>The {@link fr.sos.witchhunt.controller.InputMediator InputMediator} gathering input from this Graphical User Interface.</b>
	 * <p>Sends input requests. Receives user-input and keeps all concurrent views synchronized in terms of input collection.</p>
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator ConcreteInputMediator
	 */
	private InputMediator inputMediator;
	/**
	 * <b>The {@link Window} in which all {@link fr.sos.witchhunt.view.gui.scenes scenes} are rendered.</b>
	 */
	private Window w;
	
	/**
	 * <b>The ingame scene of this Graphical User Interface.</b>
	 * @see #gotoGamePanel()
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel
	 * @see fr.sos.witchhunt.view.gui.scenes.game
	 */
	private GamePanel gamePanel;
	/**
	 * <b>The Main Menu scene of this Graphical User Interface.</b>
	 * @see #gotoMainMenuPanel()
	 * @see fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel MainMenuPanel
	 */
	private MainMenuPanel mainMenuPanel;
	/**
	 * <b>The m setup scene of this Graphical User Interface.</b>
	 * @see #gotoMatchSetupPanel()
	 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup.MatchSetupPanel MatchSetupPanel
	 * @see fr.sos.witchhunt.view.gui.scenes.matchsetup
	 */
	private MatchSetupPanel matchSetupPanel;

	/**
	 * <b>The {@link fr.sos.witchhunt.model.flow.Tabletop instance of Tabletop} which the game setup and ingame scenes represent.</b>
	 * @see fr.sos.witchhunt.model.flow.Tabletop Tabletop
	 */
	private Tabletop tabletop;
	
	
	//CONSTRUCTOR
	/**
	 * <p><b>Creates a Graphical User Interface working for the given {@link fr.sos.witchhunt.controller.InputMediator InputMediator}.</b></p>
	 * <p>Instantiates a unique {@link Window}, which will be used as a frame for all Graphical User Interface displays.</p>
	 * @param im Value for field {@link #inputMediator}. The {@link fr.sos.witchhunt.controller.InputMediator InputMediator} keeping this view synchronized with others and sending its input requests.</p>
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 * @see #inputMediator
	 * @see Window
	 */
	public GUIView (InputMediator im) {
		this.inputMediator=im;
		this.w = new Window ();
	}
	
	/**
	 * <b>Switches to the {@link fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel Main Menu scene}.</b>
	 * <p>No effect if this scene is already the current one.</p>
	 * <p>The currently active scene is destroyed.</p>
	 * <p>Resizing the {@link Window} is not allowed for this scene.</p>
	 */
	public void gotoMainMenuPanel() {
		if(this.mainMenuPanel==null) {
			this.w.setResizable(false);
			this.gamePanel=null;
			this.matchSetupPanel=null;
			this.mainMenuPanel=this.w.renderMainMenuPanel();
			this.mainMenuPanel.setInputMediator(inputMediator);
		}	
	}
	
	/**
	 * <b>Switches to the {@link fr.sos.witchhunt.view.gui.scenes.matchsetup.MatchSetupPanel match setup scene}.</b>
	 * <p>No effect if this scene is already the current one or if this class does not know the instance of {@link #tabletop Tabletop} it represents.</p>.</p>
	 * <p>The currently active scene is destroyed.</p>
	 * <p>Resizing the {@link Window} is allowed for this scene.</p>
	 * <p>The window's title is modified when switching to this scene.</p>
	 */
	public void gotoMatchSetupPanel() {
		if (this.matchSetupPanel==null&&this.tabletop!=null) {
			this.w.setResizable(true);
			this.gamePanel=null;
			this.mainMenuPanel=null;
			this.w.setTitle(Window.defaultTitle+" : match setup");
			this.matchSetupPanel=this.w.renderMatchSetupPanel(this.tabletop);
			this.matchSetupPanel.setInputMediator(inputMediator);
			this.matchSetupPanel.update();
		}
		
	}
	
	/**
	 * <b>Switches to the {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel ingame scene} and initializes it.</b>
	 * <p>No effect if this scene is already the current one or if this class does not know the instance of {@link #tabletop Tabletop} it represents.</p>
	 * <p>The currently active scene is destroyed.</p>
	 * <p>Resizing the {@link Window} is allowed for this scene.</p>
	 * <p>The window's title is modified when switching to this scene.</p>
	 */
	public void gotoGamePanel() {
		if (this.gamePanel==null&&this.tabletop!=null){
			this.matchSetupPanel=null;
			this.mainMenuPanel=null;
			this.gamePanel=this.w.renderGamePanel();
			this.gamePanel.setInputMediator(inputMediator);
			this.gamePanel.displayMainNotification(new Notification("Let the witch hunt begin !\n",Theme.NORMAL));
			this.gamePanel.renderPlayers(tabletop.getPlayersList());
			this.gamePanel.setPlayerButtonsEnabled(false);
		}
	}

	/**
	 * <p>Calls the {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel#wannaContinue(InputMediator) GamePanel::wannaContinue(InputMediator) method}, which
	 * <b>freezes the game and asks for user input to unpause it.</b></p>
	 * <p>Used when the master {@link fr.sos.witchhunt.controller.ConcreteInputMediator#wannaContinue() InputMediator sends a "wanna continue ?" input request}.</p>
	 * @see fr.sos.witchhunt.controller.InputMediator#wannaContinue() InputMediator::wannaContinue()
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#wannaContinue() ConcreteInputMediator::wannaContinue()
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#wannaContinue(InputMediator) GamePanel::wannaContinue(InputMediator)
	 */
	public void wannaContinue() {
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
			public void run() {
		        gamePanel.wannaContinue(inputMediator);
		    }
		});
	}
	
	/**
	 * <p>Calls the {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel#makeChoice(Menu, InputMediator) ChoicesPanel::makeChoice(Menu, InputMediator) method} for the current scene's {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel}, if it has one, activating 
	 * {@link fr.sos.witchhunt.controller.interactions.ChoicesPanelController its controller} to collect user input. Not in charge of the display of the menu, see {@link #displayMenu(Menu)}.</p>
	 * <p>Used when the master {@link fr.sos.witchhunt.controller.ConcreteInputMediator#makeChoice(Menu) InputMediator asks the users to choose between plural possibilities} to let the user make its choice using this Graphical Interface.</p>
	 * <p>{@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel} is a class dedicated to representing a {@link fr.sos.witchhunt.controller.interactions.Menu Menu} on the Graphical Interface. It is used by a {@link fr.sos.witchhunt.view.gui.scenes scene}.</p>
	 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) InputMediator::makeChoice(Menu)
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#makeChoice(Menu) ConcreteInputMediator::makeChoice(Menu)
	 * 
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#makeChoice(Menu) GamePanel::makeChoice(Menu)
	 * @see fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel#makeChoice(Menu) MainMenuPanel::makeChoice(Menu)
	 * 
	 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel
	 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel#makeChoice(Menu, InputMediator) ChoicesPanel::makeChoice(Menu, InputMediator)
	 *
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * 
	 */
	public void makeChoice() {
		if(this.gamePanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    gamePanel.makeChoice();
				}
			});
		}
		else if (this.mainMenuPanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    mainMenuPanel.makeChoice();
				}
			});
		}
	}
	
	
	/**
	 * <p>Calls the {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel#displayMenu(Menu) ChoicesPanel::displayMenu(Menu) method} for the current scene's {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel}, 
	 * displaying the entitled and options of the given {@link fr.sos.witchhunt.controller.interactions.Menu Menu}.
	 * Not in charge of collecting the user's choice, see {@link #makeChoice(Menu)}.</p>
	 * <p>Used when the master {@link fr.sos.witchhunt.controller.ConcreteDisplayMediator#displayMenu(Menu) DisplayMediator orders to display a Menu} on all concurrent views.</p>
	 * <p>{@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel} is a class dedicated to representing a {@link fr.sos.witchhunt.controller.interactions.Menu Menu} on the Graphical Interface. It is used by a {@link fr.sos.witchhunt.view.gui.scenes scene}.</p>
	 * @see fr.sos.witchhunt.controller.DisplayMediator#displayMenu(Menu) DisplayMediator::displayMenu(Menu)
	 * @see fr.sos.witchhunt.controller.ConcreteDisplayMediator#displayMenu(Menu) ConcreteDisplayMediator::displayMenu(Menu)
	 * 
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#displayMenu(Menu) GamePanel::displayMenu(Menu)
	 * @see fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel#displayMenu(Menu) MainMenuPanel::displayMenu(Menu)
	 * 
	 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel
	 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel#displayMenu(Menu) ChoicesPanel::displayMenu(Menu)
	 *
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * 
	 */
	public void displayMenu(Menu m) {
		if(this.gamePanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    gamePanel.displayMenu(m);
				}
			});
		}
		else if (this.mainMenuPanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    mainMenuPanel.displayMenu(m);
				}
			});
		}
	}
	
	/**
	 * <p>Called by the master {@link fr.sos.witchhunt.controller.InputMediator} when {@link fr.sos.witchhunt.controller.ConcreteInputMediator#makeChoice(Menu) it has asked the users to choose between plural possibilities
	 *  and received their answer} to update the active scene, telling it "okay, we got an answer, you can stop harassing our users".</b></p>
	 * <p>If the current scene is {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel the ingame scene}, calls its {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel#choiceHasBeenMade(Object) choiceHasBeenMade(Object)} method.</p> 
	 * <p>Otherwise, if it is the {@link fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel the main menu scene}, calls its {@link fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel#resetChoicesPanel() resetChoicesPanel} method.</p>
	 * @param o The {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() chosen Menu entry}.
	 * @see fr.sos.witchhunt.controller.InputMediator#makeChoice(Menu) InputMediator::makeChoice(Menu)
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#makeChoice(Menu) ConcreteInputMediator::makeChoice(Menu)
	 * 
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#choiceHasBeenMade(Object) GamePanel::choiceHasBeenMade(Object)
	 * @see fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel#resetChoicesPanel() MainMenuPanel::resetChoicesPanel()
	 * 
	 * @see fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu::getOptions()
	 */
	public void choiceHasBeenMade(Object o) {
		if(this.gamePanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
					 if(gamePanel!=null) gamePanel.choiceHasBeenMade(o);
				}
			});
		}
		else if (this.mainMenuPanel!=null){
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    mainMenuPanel.resetChoicesPanel();
				}
			});
		}
	}

	/**
	 * <b>Displays a {@link fr.sos.witchhunt.controller.interactions.Menu Menu} containing only Rumour cards</b> as entries 
	 * on the {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel ingame scene}, if it is active.
	 * 
	 * @param m A menu containing entries of type {@link fr.sos.witchhunt.model.cards.RumourCard RumourCard}.
	 * @param forceReveal If this boolean is <i>true</i>, even {@link fr.sos.witchhunt.model.cards.RumourCard#isRevealed() unrevealed Rumour cards'} information will be visible.
	 * 
	 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel#displayCards(Menu, boolean) GamePanel::displayCards(Menu, boolean)
	 * @see fr.sos.witchhunt.controller.DisplayMediator#displayCa
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 */
	private void displayCardsChoiceMenu(Menu m,boolean forceReveal) {
		if(this.gamePanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    gamePanel.displayCards(m,forceReveal);
				}
			});
		}
	}
	
	
	
	public void displayPlayerAddedScreen(Player p) {
		if(this.matchSetupPanel!=null) this.matchSetupPanel.addedOne(p);
	}

	public void displayRoundStartScreen(int roundNumber) {
		if(gamePanel!=null) {
			this.w.setTitle(Window.defaultTitle+" : Round "+roundNumber);
			gamePanel.resetNotificationsBoxes();
			gamePanel.displayMainNotification(new Notification("ROUND "+roundNumber+"\n",Theme.NORMAL));
			gamePanel.displaySecondaryNotification(new Notification(Theme.HARD_SEPARATOR));
			gamePanel.setPlayerButtonsEnabled(false);
			gamePanel.updateScoreDisplay(null, tabletop.getScoreCounter());
		}
		
	}

	public void displayRoundEndScreen(int roundNumber) {
		if (gamePanel!=null) {
			gamePanel.displayMainNotification(new Notification("ROUND "+roundNumber+" IS OVER.\n",Theme.NORMAL));
			gamePanel.displaySecondaryNotification(new Notification(Theme.HARD_SEPARATOR));
			gamePanel.resetCardsPanel();
			gamePanel.hidePile();
		}
		
	}


	public void displayWinnerScreen(Player winner) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
				new Notification(
						winner.getName() + " has won the game with a score of "+winner.getScore()+" !\n",
						Theme.SCORE
				)
			);
		}
	}


	public void displayChooseIdentityScreen() {
		if (gamePanel!=null) {
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
			gamePanel.displayMainNotification(
					new Notification(
							"Please choose your identity for this round.\n",
							Theme.NORMAL
					)
			);
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
			gamePanel.boldenPlayer(tabletop.getPlayersList().get(0));
		}
	}

	public void displayHasChosenIdentityScreen(Player p) {
		if(gamePanel!=null) {
			gamePanel.displaySecondaryNotification(
					new Notification(
							"\t"+p.getName()+" has chosen their identity.\n",
							Theme.NORMAL
					)
			);
			gamePanel.unBoldenPlayer(p);
			gamePanel.updatePlayerActivityStatus(p);
			int index = tabletop.getPlayersList().indexOf(p);
			if (index+1<tabletop.getPlayersList().size()) {
				gamePanel.boldenPlayer(tabletop.getPlayersList().get(index+1));
			}
		}
	}
	
	public void displayAllPlayersHaveChosenTheirIdentityScreen() {
		gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							"All players have chosen their identity.\n",
							Theme.NORMAL
					)
			);
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
			gamePanel.displaySecondaryNotification(new Notification(Theme.HARD_SEPARATOR));
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
	}

	public void displayHandDistributionScreen(int distributedCardsCount, int discardedCardsCount) {
		if (gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification(
							"Everyone has received "+distributedCardsCount+" Rumour cards."
							+((discardedCardsCount>0)?"\n"+ discardedCardsCount+ " were put into the pile.":"")+"\n",
							Theme.NORMAL
					)
			);
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
			gamePanel.displaySecondaryNotification(new Notification(Theme.HARD_SEPARATOR));
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
			gamePanel.renderPile(tabletop.getPile());
			tabletop.getActivePlayersList().forEach(p->gamePanel.updateDeckContent(p.getHand(),false));
			gamePanel.displayOwnerLabels();
			gamePanel.updateDeckContent(tabletop.getPile(),false);
		}
	}


	public void displayPlayTurnScreen(Player p) {
		if(gamePanel!=null) {
			String name = p.getName();
			gamePanel.displayMainNotification(
					new Notification(
							"It is " + name + (name.charAt(name.length()-1)!='s'?"'s":"'") + " turn.\n",
							Theme.TURN
					)
			);

			gamePanel.switchDeck(p.getHand(),true);
			gamePanel.showCurrentPlayer(p);
		}
	}
	
	public void displayEndOfTurnScreen() {
		if(gamePanel!=null){
			
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
			gamePanel.displaySecondaryNotification(
					new Notification(
							Theme.LIGHT_SEPARATOR
					)
			);
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
			gamePanel.resetEffects();
		}
	}
	
	

	public void displayAccusationScreen(Player accusator, Player accused) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification(
							accused.getName()+", you've been accused by "+accusator.getName()+" !\n",
							Theme.OFFENSIVE
					)
			);
			gamePanel.showAccusedPlayer(accused);
		}
		
	}
	
	public void showHuntedPlayer(Player huntedPlayer) {
		if(gamePanel!=null)  {
			gamePanel.showHuntedPlayer(huntedPlayer);
		}
	}
	

	public void showWitchingPlayer(Player witchingPlayer) {
		if(gamePanel!=null)  {
			gamePanel.showWitchingPlayer(witchingPlayer);
		}
	}



	public void displayChooseDefenseMessage(Player p) {
		if(gamePanel!=null) 
			gamePanel.switchDeck(p.getHand(),true);


	}


	public void displayForcedToRevealScreen() {
		if(gamePanel!=null)
			gamePanel.displaySecondaryNotification(
					new Notification(
							"You have no other choice but to reveal who you really are...\n",
							Theme.NORMAL
					)
			);
	}
	
	public void displayGoingToRevealScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" is going to reveal their identity !\n",
							Theme.NORMAL
					)
			);
	}


	public void displayVillagerRevealScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" is only a Villager.\n",
							Theme.HUNT
					)
			);
	}


	public void displayWitchRevealScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							"You fools, "+p.getName()+" was a Witch !\n",
							Theme.WITCH
					)
			);
	}


	public void displayWitchEliminatedScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" is a Witch !\n",
							Theme.WITCH
					)
			);
	}


	public void displayScoreUpdateScreen(Player p, int points) {
		if (gamePanel!=null) {
			StringBuffer sb = new StringBuffer(p.getName());
			sb.append(' ');
			if(points>0) sb.append("earned");
			else if (points<0) sb.append("lost");
			sb.append(' ');
			sb.append(Integer.toString(Math.abs(points)));
			sb.append(" point");sb.append((Math.abs(points) == 1)?'s':"");
			sb.append(" (Total : "+ p.getScore()+")\n");
			
			gamePanel.displayMainNotification(new Notification(sb.toString(),Theme.SCORE));
			gamePanel.updateScoreDisplay(p,tabletop.getScoreCounter());
		}
	}


	public void displayEliminationScreen(Player eliminator, Player victim) {
		if(gamePanel!=null) {
			Notification eliminationNotification;
			if(eliminator!=victim)
				eliminationNotification=new Notification(eliminator.getName()+ " has sentenced " + victim.getName() + " to the stake !\n",Theme.OFFENSIVE);
			else 
				eliminationNotification=new Notification(eliminator.getName()+" has eliminated themselve !\n",Theme.OFFENSIVE);
			gamePanel.displayMainNotification(eliminationNotification);
			gamePanel.updatePlayerActivityStatus(victim);
		}
		
	}


	public void displayLastUnrevealedScreen(Player lastUnrevealedPlayer) {
		if(gamePanel!=null)
			gamePanel.displaySecondaryNotification(
					new Notification(
							lastUnrevealedPlayer.getName()+" is the last unrevealed player remaining.\n",
							Theme.NORMAL
					)
			);
	}


	public void displayNoCardsScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displaySecondaryNotification(
					new Notification(
							p.getName() + " has got no cards.\n",
							Theme.NORMAL
					)
			);
	}	

	public void displayPlayerPlaysWitchEffectScreen(Player p, RumourCard rc) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" uses "+stringifyRumourCard(rc, true,rc::getWitchEffectDescription),
							Theme.WITCH
					)
			);
			gamePanel.updateCardRevealStatus(rc);
		}
	}
	
	public void displayPlayerPlaysHuntEffectScreen(Player p, RumourCard rc) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification(
						p.getName()+" uses "+stringifyRumourCard(rc, true,rc::getHuntEffectDescription),
						Theme.HUNT
					)
			);
			gamePanel.updateCardRevealStatus(rc);
		}
	}
	
	
	public void displayPlayerHasChosenCardScreen(Player p, RumourCard chosen, RumourCardsPile from, boolean forceReveal) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" has taken "+stringifyRumourCard(chosen, forceReveal, chosen::getWitchEffectDescription ,chosen::getHuntEffectDescription),
							Theme.NORMAL
					)
			);
			gamePanel.updateDeckContent(p.getHand(),forceReveal||playerChoosesOwnCard(p, from));
			if(from!=p.getHand()) gamePanel.updateDeckContent(from,forceReveal||playerChoosesOwnCard(p, from));
			gamePanel.resetCardsEffects();
		}
	}


	public void displayPlayerHasDiscardedCardScreen(Player owner, RumourCard rc) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification(
						owner.getName()+" has discarded "+stringifyRumourCard(rc, false, rc::getWitchEffectDescription ,rc::getHuntEffectDescription),
						Theme.NORMAL
					)
			);
			gamePanel.updateDeckContent(owner.getHand(),false);
			gamePanel.updateDeckContent(tabletop.getPile(),false);
			gamePanel.resetCardsEffects();
		}
	}


	public void displayEmptyHandMessage(Player owner) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
						owner.getName()+"'"+((owner.getName().charAt(owner.getName().length()-1)!='s')?"s":"")+ "hand is empty.\n",
						Theme.NORMAL
					)
			);
	}


	public void displayNoCardsInPileScreen() {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
						"The pile is empty.\n",
						Theme.NORMAL
					)
			);
	}


	public void displayLookAtPlayersIdentityScreen(Player me, Player target) {
		if(gamePanel!=null)
			gamePanel.displaySecondaryNotification(
					new Notification(
							me.getName() + " is looking at "+target.getName()+"'"+((target.getName().charAt(target.getName().length()-1)!='s')?"s":"") + " identity.\n",
							Theme.HUNT
					)
			);
	}


	public void displaySecretlyDisplayIdentity(Player target) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							"Others, close your eyes !\n"
							+ "\t"+target.getName() + " is a "+target.getIdentity().toString().toLowerCase()+".\n",
							Theme.NORMAL
					)
			);
	}


	public void displayPlayerHasResetCardScreen(Player player, RumourCard rc) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification(
							player.getName() +" took back "+stringifyRumourCard(rc, true, rc::getWitchEffectDescription ,rc::getHuntEffectDescription),
							Theme.NORMAL
					)
			);
		}
	}


	public void displayTakeNextTurnScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName() + " takes the next turn.\n",
							Theme.TURN
					)
			);
	}
	
	
	public void displayPlayTurnAgainScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName() + " takes another turn.\n",
							Theme.TURN
					)
			);
	}



	public void displayForcedToAccuseScreen(Player theOneWhoMustAccuse, Player theOneWhoForcedThem) {
		if(gamePanel!=null) {
			String accuserName = theOneWhoMustAccuse.getName();
			String forcedByName = theOneWhoForcedThem.getName();
			gamePanel.displayMainNotification(
				new Notification(
						accuserName+", "+forcedByName+" forced you to accuse "+((theOneWhoForcedThem.isImmunized())?"someone else ":"them ")+ "!\n",
						Theme.NORMAL
				)
			);
		}
	}


	public void displayStealCardScreen(Player thief, Player stolenPlayer) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							thief.getName()+" is subtilizing a card from "+stolenPlayer.getName()+"'"+((stolenPlayer.getName().charAt(stolenPlayer.getName().length()-1)!='s')?"s":"")+" hands !\n",
							Theme.NORMAL
					)
			);
	}


	public void displayGameIsTiedScreen(int score, List<Player> potentialWinners) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification("~ The game is tied ! ~\n",
					Theme.SCORE
				)
			);
			
			gamePanel.displaySecondaryNotification(
					new Notification(Theme.HARD_SEPARATOR)
			);
			
			List<String> duelists = potentialWinners.stream().map(p->p.getName()).toList();
			StringBuffer sb = new StringBuffer("\t");
			for(int i=0; i<duelists.size(); i++) {
				sb.append(duelists.get(i));
				if(i<duelists.size()-2) sb.append(", ");
				else if(i==duelists.size()-2) sb.append(" and ");
			}
			sb.append((duelists.size()>2)?" all":" both");
			sb.append(" have a score of ");
			sb.append(score);
			sb.append(" and must compete for the first place !\n");
			
			gamePanel.displaySecondaryNotification(new Notification(
						sb.toString(),
						Theme.TURN
					)
			);
		}
		
	}
	
	public void displayStrategyChange(Player p, PlayStrategy strat) {
		if(gamePanel!=null) {
			gamePanel.displaySecondaryNotification(
					new Notification(p.getName()+" opts for "+strat.toString()+".\n",
					Theme.SCORE
				)
			);
		}
	}


	public void displayScoreBoard(ScoreBoard sb) {
		if(gamePanel!=null) {
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
			gamePanel.displaySecondaryNotification(
				new Notification(
						"\t"+sb.toString().replace("/+/", "\t"),
						Theme.SCORE
				)
			);
			gamePanel.displaySecondaryNotification(new Notification(Theme.CRLF));
		}
	}
	
	/**
	 * <p><b>Transforms a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} into a String (GUI version).</b></p>
	 * <p>Used for displaying cards-related messages (played a Witch? effect, discarded a card ...) in the {@link #gamePanel ingame scene}.</p>
	 * <p>The /+/ "catch up indentation" special sequence is replaced with spaces in order to make a paragraph out of the card's description.</p>
	 * @param rc A {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} to stringify
	 * @param forceReveal The unrevealed cards' name and properties will be hidden unless this boolean is set to <i>true</i>. 
	 * @param effectGetters An array of String getters giving the wanted parts of the cards' description. See for example : {@link fr.sos.witchhunt.model.cards.RumourCard#getWitchEffectDescription() RumourCard::getWitchEffectDescription()}
	 * @return A String describing the given card's, which can be used for display on the GUI view.
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see fr.sos.witchhunt.model.cards.RumourCard#getAdditionalEffectDescription() RumourCard::getAdditionalEffectDescription()
	 * @see fr.sos.witchhunt.model.cards.RumourCard#getWitchEffectDescription() RumourCard::getAdditionalEffectDescription()
	 * @see fr.sos.witchhunt.model.cards.RumourCard#getHuntEffectDescription() RumourCard::getHuntEffectDescription()
	 * 
	 * @see #displayPlayerPlaysWitchEffectScreen(Player, RumourCard)
	 * @see #displayChooseHuntCardScreen(Player, RumourCardsPile)
	 * @see #displayPlayerHasDiscardedCardScreen(Player, RumourCard)
	 * @see #displayPlayerHasChosenCardScreen(Player, RumourCard, RumourCardsPile, boolean)
	 * @see #displayStealCardScreen(Player, Player)
	 * @see #displayPlayerHasResetCardScreen(Player, RumourCard)
	 * 
	 * @see java.util.function.Supplier a Supplier<T> is a method taking no entry parameter and returning an object of type T.
	 */
	private String stringifyRumourCard(RumourCard rc,boolean forceReveal,Supplier<String> ...effectGetters) {
		StringBuffer sb = new StringBuffer();
		if(forceReveal||rc.isRevealed()) {
			sb.append(rc.getName());
			sb.append(" :");
			sb.append('\n');
			if(!rc.getAdditionalEffectDescription().equals("")) {
				sb.append("\t*"); 
				sb.append(rc.getAdditionalEffectDescription().replace("/+/", "\t ")); 
				sb.append("*\n");
			}
			for(Supplier<String> getter : effectGetters) {
				String off;
				if(getter.get().equals(rc.getWitchEffectDescription())&&effectGetters.length>1) {
					sb.append("\tWitch : ");
					off=" ".repeat("Witch : ".length());
				}
				else if (getter.get().equals(rc.getHuntEffectDescription())&&effectGetters.length>1) {
					sb.append("\tHunt : ");
					off=" ".repeat("Hunt : ".length());
				}
				else {
					sb.append("\t");
					off="";
				}
				sb.append(getter.get().replace("/+/","\t"+off));
				sb.append('\n');
			}	
			return sb.toString();
		}
		else {
			return "*Unrevealed*\n";
		}
		
	}
	
	public void displayChooseAnyCardScreen(Player p,RumourCardsPile from) {
		if(gamePanel!=null) { 
			gamePanel.switchDeck(from,playerChoosesOwnCard(p,from));
			gamePanel.makeCardsChoosable(p, from, from, Theme.NORMAL);
			displayCardsChoiceMenu(new Menu("Select any card",from.getCards().toArray()),playerChoosesOwnCard(p,from));
		}
	}
	public void displayChooseRevealedCardScreen(Player p,RumourCardsPile from) {
		gamePanel.switchDeck(from,playerChoosesOwnCard(p,from));
		gamePanel.makeCardsChoosable(p, from, from.getRevealedSubpile(), Theme.NORMAL);
		if(gamePanel!=null) displayCardsChoiceMenu(new Menu("Select a revealed card",from.getCards().toArray()),playerChoosesOwnCard(p,from));
	}
	public void displayChooseUnrevealedCardScreen(Player p,RumourCardsPile from) {
		gamePanel.switchDeck(from,playerChoosesOwnCard(p,from));
		gamePanel.makeCardsChoosable(p, from, from.getUnrevealedSubpile(), Theme.NORMAL);
		if(gamePanel!=null) displayCardsChoiceMenu(new Menu("Select an unrevealed card",from.getCards().toArray()),playerChoosesOwnCard(p,from));
	}


	public void displayChooseWitchCardScreen(Player p,RumourCardsPile from) {
		if(gamePanel!=null) {
			gamePanel.switchDeck(p.getHand(),true);
			gamePanel.makeCardsChoosable(p, p.getHand() , from, Theme.WITCH);
			displayCardsChoiceMenu(new Menu("Select a card with a valid Witch? effect",from.getCards().toArray()),true);
			SwingUtilities.invokeLater(new Runnable() {
			    @Override
				public void run() {
			    	gamePanel.setActionsPanelThemeAsForActionType(DefenseAction.WITCH);
			    }
			});
			
		}
	}


	public void displayChooseHuntCardScreen(Player p,RumourCardsPile from) {
		if(gamePanel!=null)  {
			gamePanel.switchDeck(p.getHand(),true);
			gamePanel.makeCardsChoosable(p, p.getHand() , from, Theme.HUNT);
			displayCardsChoiceMenu(new Menu("Select a card with a valid Hunt! effect",from.getCards().toArray()),true);
			SwingUtilities.invokeLater(new Runnable() {
			    @Override
				public void run() {
			    	gamePanel.setActionsPanelThemeAsForActionType(TurnAction.HUNT);
			    }
			});
			
		}
	}


	public void displayChooseCardToDiscardScreen(Player p) {
		if(gamePanel!=null) gamePanel.switchDeck(p.getHand(), true);
	}


	
	public void setTabletop(Tabletop tabletop) {
		this.tabletop=tabletop;
	}
	
	/**
	 * <p><b>Asserts whether the given {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} belongs to the given player or not.</b></p>
	 * <p>Used to know whether the {@link #gamePanel ingame scene} has to show the given deck's view or not.</p>
	 * @param p Is this one player the owner of the following deck ?
	 * @param from Does this pile of cards belong to the given {@link fr.sos.witchhunt.model.players.Player Player} ?
	 * @return <i>true</i> if the deck belongs to the given player, <i>false</i> otherwise.
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile RumourCardsPile
	 * @see fr.sos.witchhunt.model.players.Player Player Player
	 * @see #displayChooseAnyCardScreen(Player, RumourCardsPile)
	 * @see #displayChooseUnrevealedCardScreen(Player, RumourCardsPile)
	 * @see #displayChooseRevealedCardScreen(Player, RumourCardsPile)
	 * @see #displayChooseCardToDiscardScreen(Player)
	 */
	private boolean playerChoosesOwnCard(Player p,RumourCardsPile from) {
		return p==from.getOwner();
	}
	
	



	
}
