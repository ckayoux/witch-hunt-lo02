package fr.sos.witchhunt.view.gui;

import java.util.List;
import java.util.function.Supplier;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.players.Player;

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


	
	public void wannaContinue(InputMediator im) {
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
			public void run() {
		        gamePanel.wannaContinue(im);
		    }
		});
	}
	
	public void resetActionsPanel() {
		if(this.gamePanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    gamePanel.resetActionPanel();
				}
			});
		}
		else if (this.mainMenuPanel!=null){
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    mainMenuPanel.resetActionPanel();
				}
			});
		}
	}

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
	
	public void makeChoice(Menu m) {
		if(this.gamePanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    gamePanel.makeChoice(m);
				}
			});
		}
		else if (this.mainMenuPanel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				 @Override
				public void run() {
				    mainMenuPanel.makeChoice(m);
				}
			});
		}
	}


	public void displayRoundStartScreen(int roundNumber) {
		if(gamePanel!=null) {
			gamePanel.resetNotificationsBoxes();
			gamePanel.displayMainNotification(new Notification("ROUND "+roundNumber+"\n",NotificationType.NORMAL));
			gamePanel.displaySecondaryNotification(new Notification(NotificationType.SEPARATOR));
		}
		
	}

	public void displayRoundEndScreen(int roundNumber) {
		if (gamePanel!=null) {
			gamePanel.displayMainNotification(new Notification("ROUND "+roundNumber+" IS OVER.\n",NotificationType.NORMAL));
			gamePanel.displaySecondaryNotification(new Notification(NotificationType.SEPARATOR));
		}
		
	}


	public void displayWinnerScreen(Player winner) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
				new Notification(
						winner.getName() + " has won the game with a score of "+winner.getScore()+"\n",
						NotificationType.SCORE
				)
			);
		}
	}


	public void displayChooseIdentityScreen() {
		if (gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification(
							"Please choose your identity for this round.\n",
							NotificationType.NORMAL
					)
			);
		}
	}


	public void displayHandDistributionScreen(int distributedCardsCount, int discardedCardsCount) {
		if (gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification(
							"Everyone has received "+distributedCardsCount+" Rumour cards."
							+((discardedCardsCount>0)?"\n"+ discardedCardsCount+ " were put into the pile.":"")+"\n",
							NotificationType.NORMAL
					)
			);
			//COMMENCER A AFFICHER LES CARTES
		}
	}


	public void displayPlayTurnScreen(Player p) {
		if(gamePanel!=null) {
			String name = p.getName();
			gamePanel.displayMainNotification(
					new Notification(
							"It is " + name + (name.charAt(name.length()-1)!='s'?"'s":"'") + " turn.\n",
							NotificationType.TURN
					)
			);
		
		}
	}


	public void setCurrentPlayer(Player p) {
		//ENCADRER EN VERT LE JOUEUR
	}


	public void unsetCurrentPlayer() {
		//NE PLUS L'ENCADRER EN VERT
	}


	public void displayAccusationScreen(Player accusator, Player accused) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							accused.getName()+", you've been accused by "+accusator.getName()+" !\n",
							NotificationType.OFFENSIVE
					)
			);
		
	}


	public void setAccusedPlayer(Player accused) {
		// ENCADRER EN ROUGE L'ACCUSE
		
	}


	public void displayChooseDefenseMessage(Player p) {
		if(gamePanel!=null) 
			gamePanel.displaySecondaryNotification(
				new Notification(
						"The village's pyre has been lit up.\n",
						NotificationType.NORMAL
				)
			);

	}


	public void displayForcedToRevealScreen() {
		if(gamePanel!=null)
			gamePanel.displaySecondaryNotification(
					new Notification(
							"You have no other choice but to reveal who you really are...\n",
							NotificationType.NORMAL
					)
			);
	}
	
	public void displayGoingToRevealScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" is going to reveal their identity !\n",
							NotificationType.NORMAL
					)
			);
	}


	public void displayVillagerRevealScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" is only a Villager.\n",
							NotificationType.HUNT
					)
			);
	}


	public void displayWitchRevealScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							"You fools, "+p.getName()+" was a Witch !\n",
							NotificationType.WITCH
					)
			);
	}


	public void displayWitchEliminatedScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" is a Witch !\n",
							NotificationType.WITCH
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
			
			gamePanel.displayMainNotification(new Notification(sb.toString(),NotificationType.SCORE));
		}
	}


	public void displayEliminationScreen(Player eliminator, Player victim) {
		if(gamePanel!=null) {
			Notification eliminationNotification;
			if(eliminator!=victim)
				eliminationNotification=new Notification(eliminator.getName()+ " has sentenced " + victim.getName() + " to the stake !\n",NotificationType.OFFENSIVE);
			else 
				eliminationNotification=new Notification(eliminator.getName()+" has eliminated themselve !\n",NotificationType.OFFENSIVE);
			gamePanel.displayMainNotification(eliminationNotification);
		}
		
	}


	public void displayLastUnrevealedScreen(Player lastUnrevealedPlayer) {
		if(gamePanel!=null)
			gamePanel.displaySecondaryNotification(
					new Notification(
							lastUnrevealedPlayer.getName()+" is the last unrevealed player remaining. It turns out ...\n",
							NotificationType.NORMAL
					)
			);
	}


	public void displayNoCardsScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displaySecondaryNotification(
					new Notification(
							p.getName() + " has got no cards.",
							NotificationType.NORMAL
					)
			);
	}


	public void displayOnlyTwoUnrevealedRemainingScreen() {
		//AFFICHER QUELQUE CHOSE DE PARTICULIER SUR LE PLAYERS PANEL
	}
	

	

	public void displayPlayerPlaysWitchEffectScreen(Player p, RumourCard rc) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" uses "+stringifyRumourCard(rc, true,rc::getWitchEffectDescription),
							NotificationType.WITCH
					)
			);
		//UPDATE REVEAL STATUS
	}
	
	public void displayPlayerPlaysHuntEffectScreen(Player p, RumourCard rc) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
						p.getName()+" uses "+stringifyRumourCard(rc, true,rc::getHuntEffectDescription),
						NotificationType.HUNT
					)
			);
		//UPDATE REVEAL STATUS
	}
	
	
	public void displayPlayerHasChosenCardScreen(Player p, RumourCard chosen, boolean forceReveal) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName()+" has taken "+stringifyRumourCard(chosen, forceReveal, chosen::getWitchEffectDescription ,chosen::getHuntEffectDescription),
							NotificationType.NORMAL
					)
			);
		//UPDATE CARDS PANEL
	}


	public void displayPlayerHasDiscardedCardScreen(Player owner, RumourCard rc) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
						owner.getName()+" has discarded "+stringifyRumourCard(rc, false, rc::getWitchEffectDescription ,rc::getHuntEffectDescription),
						NotificationType.NORMAL
					)
			);
		//UPDATE PILE AND OWNER'S HAND
	}


	public void displayEmptyHandMessage(Player owner) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
						owner.getName()+"'"+((owner.getName().charAt(owner.getName().length()-1)!='s')?"s":"")+ "hand is empty.\n",
						NotificationType.NORMAL
					)
			);
	}


	public void displayNoCardsInPileScreen() {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
						"The pile is empty.\n",
						NotificationType.NORMAL
					)
			);
	}


	public void displayLookAtPlayersIdentityScreen(Player me, Player target) {
		if(gamePanel!=null)
			gamePanel.displaySecondaryNotification(
					new Notification(
							me.getName() + " is looking at "+target.getName()+"'"+((target.getName().charAt(target.getName().length()-1)!='s')?"s":"") + " identity.\n",
							NotificationType.HUNT
					)
			);
	}


	public void displaySecretlyDisplayIdentity(Player target) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							"Others, close your eyes !\n"
							+ "\t"+target.getName() + " is a "+target.getIdentity().toString().toLowerCase()+".\n",
							NotificationType.NORMAL
					)
			);
	}


	public void displayPlayerHasResetCardScreen(Player player, RumourCard rc) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							player.getName() +" took back "+stringifyRumourCard(rc, false, rc::getWitchEffectDescription ,rc::getHuntEffectDescription),
							NotificationType.NORMAL
					)
			);
		//MAKE RC UNREVEALED ON CARDS PANEL
	}


	public void displayTakeNextTurnScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName() + " takes the next turn.",
							NotificationType.TURN
					)
			);
	}
	
	
	public void displayPlayTurnAgainScreen(Player p) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							p.getName() + " takes another turn.",
							NotificationType.TURN
					)
			);
	}



	public void displayForcedToAccuseScreen(Player theOneWhoMustAccuse, Player theOneWhoForcedThem) {
		if(gamePanel!=null) {
			String accuserName = theOneWhoMustAccuse.getName();
			String forcedByName = theOneWhoForcedThem.getName();
			gamePanel.displayMainNotification(
				new Notification(
						accuserName+", "+forcedByName+" forced you to accuse "+((theOneWhoForcedThem.isImmunized())?"someone else ":"them ")+ "!",
						NotificationType.NORMAL
				)
			);
		}
	}


	public void displayStealCardScreen(Player thief, Player stolenPlayer) {
		if(gamePanel!=null)
			gamePanel.displayMainNotification(
					new Notification(
							thief.getName()+" has taken a card from "+stolenPlayer.getName()+((stolenPlayer.getName().charAt(stolenPlayer.getName().length()-1)!='s')?"s":"")+" hands !",
							NotificationType.NORMAL
					)
			);
		//UPDATE CARDS FOR BOTH PLAYERS
	}


	public void displayGameIsTiedScreen(int score, List<Player> potentialWinners) {
		if(gamePanel!=null) {
			gamePanel.displayMainNotification(
					new Notification("~ The game is tied ! ~",
					NotificationType.SCORE
				)
			);
			
			gamePanel.displaySecondaryNotification(
					new Notification(NotificationType.SEPARATOR)
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
						NotificationType.TURN
					)
			);
		}
		
	}
	
	private String stringifyRumourCard(RumourCard rc,boolean forceReveal,Supplier<String> ...effectGetters) {
		StringBuffer sb = new StringBuffer();
		if(forceReveal||rc.isRevealed()) {
			sb.append(rc.getName());
			sb.append(" :");
			sb.append('\n');
			if(!rc.getAdditionnalEffectDescription().equals("")) {
				sb.append("\t*"); 
				sb.append(rc.getAdditionnalEffectDescription().replace("/+/", "\t ")); 
				sb.append("*\n");
			}
			for(Supplier<String> getter : effectGetters) {
				String off;
				if(getter.equals((Supplier<String>)rc::getWitchEffectDescription)) {
					sb.append("\tWitch : ");
					off=" ".repeat("Witch : ".length());
				}
				else if (getter.equals((Supplier<String>)rc::getHuntEffectDescription)) {
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
			return "*Unrevealed*";
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
