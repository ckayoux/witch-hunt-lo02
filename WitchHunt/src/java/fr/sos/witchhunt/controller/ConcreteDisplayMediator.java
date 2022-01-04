package fr.sos.witchhunt.controller;

import java.util.List;

import fr.sos.witchhunt.controller.core.Game;
import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.flow.ScoreCounter;
import fr.sos.witchhunt.model.flow.ScoreCounter.ScoreBoard;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy;
import fr.sos.witchhunt.view.gui.GUIView;
import fr.sos.witchhunt.view.std.StdView;

/**
 * <p><b>Central class linking the application's {@link fr.sos.witchhunt.model 'model'} and {@link fr.sos.witchhunt.view 'view'} levels in terms of display.</b></p>
 * <p><b>Allows keeping the model independent from the view, for a Model-View-Controller approach.</b></p>
 * <p>In charge of notifying all concurrent views of display-related events.</p>
 * <p>Defines a method per situation requiring update of the display. These methods are specified by the {@link DisplayMediator interface}.</p>
 * <p>The {@link fr.sos.witchhunt.model model}'s classes requiring to update the view must be associated with a class implementing {@link InputMediator}, through which they can send
 * their requests by calling its public methods.</p>
 * <p>Knows the central class of each concurrent view ({@link fr.sos.witchhunt.view.std.StdView StdView} and {@link fr.sos.witchhunt.view.std.GUIView GUIView}).
 * These classes come with a set of public methods corresponding to each situation.
 * Most of this class' methods will update all concurrent views, by calling the methods corresponding to the requested notification on each view-related known classes.</p>
 * <p>If new views were to be added, they should be managed by this class, the same way it manages the already existing views.</p>
 * <p>Not in charge of collecting user-input : this is {@link InputMediator the InputMediator's} role.</p>
 * <p>Instantiated once by {@link fr.sos.witchhunt.controller.core.Application Application} and shared between all components requiring display.</p>
 *
 * @see fr.sos.witchhunt.view
 * @see fr.sos.witchhunt.view.std.StdView StdView, the central class of the Console view
 * @see fr.sos.witchhunt.view.std.GUIView GUIView, the central class of the Graphical User Interface
 *
 * @see fr.sos.witchhunt.model
 * @see fr.sos.witchhunt.players.PlayerDisplayRequester Players can send many different types of display requests, specified by the PlayerDisplayRequester interface's methods
 * 
 * @see DisplayMediator Stranger classes send their display requests through an instance of a class implementing DisplayMediator
 * @see InputMediator User-input is gathered from the view by another component, the InputMediator
 */
public final class ConcreteDisplayMediator implements DisplayMediator {
	/**
	 * <p><b>Central class of the console view.</b></p>
	 * <p>Defines a way to display each sort of notification in the console.</p>
	 * @see fr.sos.witchhunt.view.std
	 */
	private StdView console;
	/**
	 * <p><b>Central class of the Graphical User Interface.</b></p>
	 * <p>Knows all components of the GUI view, and sends them requests through public methods in order to update them.</p>
	 * <p>Defines a way to display each sort of notification.</p>
	 * @see fr.sos.witchhunt.view.gui
	 */
	private GUIView gui;
	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayMenu(Menu m) {
		console.makeMenu(m);
		gui.gotoMainMenuPanel();
		gui.displayMenu(m);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayAddedPlayerScreen(Player p) {
		console.logAddedPlayerMessage(p);
		gui.displayPlayerAddedScreen(p);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayAddedPlayersScreen(int n) {
		console.logAddedPlayersScreen(n);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayPossibilities(Menu possibilities) {
		console.logPossibilities(possibilities);
		gui.displayMenu(possibilities);
		//T0D0 : makeMenu for GUI view
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void logYesNoQuestion(String q) {
		console.yesNoQuestion(q);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void passLog(String msg) {
		console.log(msg);
	}
	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayAddPlayersScreen(Tabletop tabletop) {
		console.logAddPlayersScreen(tabletop.getMinPlayersNumber(), tabletop.getMaxPlayersNumber());
		gui.setTabletop(tabletop);
		gui.gotoMatchSetupPanel();
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayMatchStartScreen() {
		console.logMatchStartMessage();
		gui.gotoGamePanel();
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayMatchEndScreen() {
		console.logMatchEndMessage();
		//TODO : equivalent for gui view
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayRoundStartScreen(int roundNumber) {
		console.logRoundStartMessage(roundNumber);
		gui.displayRoundStartScreen(roundNumber);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayRoundEndScreen(int roundNumber) {
		console.logRoundEndMessage(roundNumber);
		gui.displayRoundEndScreen(roundNumber);
	}
	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayWinnerScreen(Player winner) {
		console.logWinnerMessage(winner.getName(), winner.getScore());
		gui.displayWinnerScreen(winner);
	}
	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayRanking(Player P,ScoreCounter sc) {
		List<Player> ranking = sc.getRanking();
		int rank=1;
		int pSRank=-1;
		boolean pIsExAequo=false;
		int lastScore = ranking.get(0).getScore();
		
		console.increaseTabulation();
		if(ranking.stream().filter(p->p.getScore()==0).count()==ranking.size()) {
			ranking.forEach(p->console.logPlayerAndTheirScore(-1, false , p.getName(), 0));
		}
		else {
			for(Player p : ranking) {
				boolean exAequo = (ranking.stream().filter(p2->p.getScore()==p2.getScore()).count()>1);
				if(p.getScore()!=lastScore) {
					rank++;
					lastScore=p.getScore();
				}
				console.logPlayerAndTheirScore(rank, exAequo , p.getName(), p.getScore());
				if(p==P) {
					pIsExAequo=exAequo;
					pSRank=rank;
				}
				
				
			};
		}
		if(pSRank>0) {
			console.crlf();
			console.logPlayerRankingMessage(pIsExAequo,pSRank,P.getScore());
		}
		console.crlf();
		console.decreaseTabulation();
		
		gui.displayScoreBoard(sc.getScoreBoard());
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayChooseIdentityScreen() {
		console.logChooseIdentityMessage();
		gui.displayChooseIdentityScreen();
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void distributeHandScreen(int distributedCardsCount,int discardedCardsCount) {
		console.logHandDistributionMessage(distributedCardsCount,discardedCardsCount);
		gui.displayHandDistributionScreen(distributedCardsCount,discardedCardsCount);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayPlayTurnScreen(Player p) {
		console.logPlayTurnMessage(p.getName());
		gui.displayPlayTurnScreen(p);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayEndOfTurnScreen() {
		console.logEndOfTurnMessage();
		gui.displayEndOfTurnScreen();
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayAccusationScreen(Player accusator, Player accused) {
		console.logAccusationMessage(accusator.getName(),accused.getName());
		gui.displayAccusationScreen(accusator,accused);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayHuntedPlayerScreen(Player huntedPlayer) {
		gui.showHuntedPlayer(huntedPlayer);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayChooseDefenseScreen(Player p) {
		console.logChooseDefenseMessage();
		gui.displayChooseDefenseMessage(p);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayForcedToRevealScreen() {
		console.logForcedToRevealMessage();
		gui.displayForcedToRevealScreen();
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayGoingToRevealIdentityScreen(Player p) {
		console.logGoingToRevealMessage(p.getName());
		gui.displayGoingToRevealScreen(p);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayIdentityRevealScreen(Player p,Player lastUnrevealedPlayer) {
		if(!p.isRevealed()){
			
			switch(p.getIdentity()) {
				case VILLAGER:
					console.logVillagerRevealMessage(p.getName());
					gui.displayVillagerRevealScreen(p);
					break;
				case WITCH:
					if(lastUnrevealedPlayer==p) {
						console.logWitchRevealMessage(p.getName());
						gui.displayWitchRevealScreen(p);
					}
					else {
						console.logWitchEliminatedMessage(p.getName());
						gui.displayWitchEliminatedScreen(p);
					}
					break;
			}
		}
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayScoreUpdateScreen(Player p, int points) {
		console.logUpdateScreenMessage(p.getName(),points,p.getScore());
		gui.displayScoreUpdateScreen(p,points);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayEliminationScreen(Player eliminator, Player victim) {
		console.logEliminationMessage(eliminator.getName(),victim.getName());
		gui.displayEliminationScreen(eliminator,victim);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayLastUnrevealedPlayerScreen(Player lastUnrevealedPlayer) {
		console.logLastUnrevealedMessage(lastUnrevealedPlayer.getName());
		gui.displayLastUnrevealedScreen(lastUnrevealedPlayer);
		switch(lastUnrevealedPlayer.getIdentity()) {
			case WITCH:
				console.logWitchRevealMessage(lastUnrevealedPlayer.getName());
				gui.displayWitchRevealScreen(lastUnrevealedPlayer);
				break;
			case VILLAGER:
				console.logVillagerRevealMessage(lastUnrevealedPlayer.getName());
				gui.displayVillagerRevealScreen(lastUnrevealedPlayer);
				break;
		}
		console.logWeakDashedLine();
		console.crlf();
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayNoCardsScreen(Player p) {
		console.logNoCardsMessage(p.getName());
		gui.displayNoCardsScreen(p);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayOnlyTwoUnrevealedRemainingScreen() {
		console.logOnlyTwoUnrevealedRemainingMessage();
		gui.displayOnlyTwoUnrevealedRemainingScreen();
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayCard(RumourCard rc, boolean forcedReveal) {
		if(rc.isRevealed() || forcedReveal) {
			if(rc.getAdditionalEffectDescription().equals("")) {
				console.logRumourCard(rc.getName(),rc.isRevealed(),rc.getWitchEffectDescription(),rc.getHuntEffectDescription());
			}
			else {
				console.logRumourCard(rc.getName(),rc.isRevealed(),rc.getAdditionalEffectDescription(),rc.getWitchEffectDescription(),rc.getHuntEffectDescription());
			}
		}
		else {
			console.logUnrevealedCard();
		}
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayWitchEffect(RumourCard rc) {
		if(rc.getAdditionalEffectDescription().equals("")) {
			console.logEffect(rc.getName(),rc.getWitchEffectDescription());
		}
		else {
			console.logEffect(rc.getName(),rc.getAdditionalEffectDescription(),rc.getWitchEffectDescription());
		}
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayHuntEffect(RumourCard rc) {
		if(rc.getAdditionalEffectDescription().equals("")) {
			console.logEffect(rc.getName(),rc.getHuntEffectDescription());
		}
		else {
			console.logEffect(rc.getName(),rc.getAdditionalEffectDescription(),rc.getHuntEffectDescription());
		}
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayCards(RumourCardsPile rcp, boolean forcedReveal) {
		console.increaseTabulation();
		int i=1;
		for(RumourCard rc : rcp.getCards()) {
			console.setOffset(i + " - ");
			displayCard(rc,forcedReveal);
			i++;
		};
		console.resetOffset();
		console.decreaseTabulation();
		console.crlf();
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayWitchEffects(RumourCardsPile rcp) {
		console.increaseTabulation();
		int i=1;
		for(RumourCard rc : rcp.getCards()) {
			console.setOffset(i + " - ");
			displayWitchEffect(rc);
			console.crlf();
			i++;
		};
		console.resetOffset();
		console.decreaseTabulation();
		console.crlf();
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayHuntEffects(RumourCardsPile rcp) {
		console.increaseTabulation();
		int i=1;
		for(RumourCard rc : rcp.getCards()) {
			console.setOffset(i + " - ");
			displayHuntEffect(rc);
			console.crlf();
			i++;
		};
		console.resetOffset();
		console.decreaseTabulation();
		console.crlf();
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void showCards(Player p) {
		//shows all the cards of a player. Called on purpose by human players
		if(p.hasRumourCards()) {
			console.logShowPlayersCardsMessage(p.getName());
			displayCards(p.getHand(),true);
			console.crlf();

		}
		else {
			console.logNoCardsMessage(p.getName());
		}
	}
	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displaySelectCardScreen(Player p,RumourCardsPile from, boolean forcedReveal) {
		console.logSelectCardsMessage(null);
		this.displayCards(from, forcedReveal);
		
		gui.displayChooseAnyCardScreen(p,from);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displaySelectUnrevealedCardScreen(Player p,RumourCardsPile from, boolean forcedReveal) {
		console.logSelectCardsMessage("unrevealed");
		this.displayCards(from, forcedReveal);
		
		gui.displayChooseUnrevealedCardScreen(p,from);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displaySelectRevealedCardScreen(Player p,RumourCardsPile from, boolean forcedReveal) {
		console.logSelectCardsMessage("revealed");
		this.displayCards(from, forcedReveal);
		
		gui.displayChooseRevealedCardScreen(p,from);
		
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displaySelectWitchCardScreen(Player p,RumourCardsPile from) {
		console.logSelectWitchCardMessage();
		this.displayWitchEffects(from);
		
		gui.displayChooseWitchCardScreen(p,from);
	}
	

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displaySelectHuntCardScreen(Player p,RumourCardsPile from) {
		console.logSelectHuntCardMessage();
		this.displayHuntEffects(from);

		gui.displayChooseHuntCardScreen(p, from);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayPlayerPlaysWitchEffectScreen(Player p,RumourCard rc) {
		if(rc.getAdditionalEffectDescription()=="") 
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getWitchEffectDescription());
		else
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getWitchEffectDescription(),rc.getAdditionalEffectDescription());
		gui.displayPlayerPlaysWitchEffectScreen(p,rc);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayPlayerPlaysHuntEffectScreen(Player p,RumourCard rc) {
		if(rc.getAdditionalEffectDescription()=="")
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getHuntEffectDescription());
		else
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getHuntEffectDescription(),rc.getAdditionalEffectDescription());
		gui.displayPlayerPlaysHuntEffectScreen(p, rc);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayHasChosenCardScreen(Player p, RumourCard chosen,RumourCardsPile from,boolean forceReveal) {
		if(chosen.getAdditionalEffectDescription()=="") 
			console.logHasChosenCardMessage(p.getName(),chosen.getName(),(chosen.isRevealed()||forceReveal),
					chosen.getWitchEffectDescription(),chosen.getHuntEffectDescription());
		else 
			console.logHasChosenCardMessage(p.getName(),chosen.getName(),(chosen.isRevealed()||forceReveal),chosen.getAdditionalEffectDescription(),
					chosen.getWitchEffectDescription(),chosen.getHuntEffectDescription());
		gui.displayPlayerHasChosenCardScreen(p, chosen,from,forceReveal);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayNoCardsInPileScreen(RumourCardsPile rcp) {
		if(rcp.getOwner()!=null) {
			console.logEmptyHandMessage(rcp.getOwner().getName());
			gui.displayEmptyHandMessage(rcp.getOwner());
		}
		else if(rcp.isThePile()) {
			console.logEmptyPileMessage();
			gui.displayNoCardsInPileScreen();
		}
		
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayPlayerDiscardedCardScreen(Player owner,RumourCard rc) {
		console.printPlayerDiscardedCardMessage(owner.getName());
		displayCard(rc, false);
		console.resetOffset();
		
		gui.displayPlayerHasDiscardedCardScreen(owner,rc);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayLookAtPlayersIdentityScreen(Player me, Player target) {
		console.logLookAtPlayersIdentityMessage(me.getName(),target.getName());
		gui.displayLookAtPlayersIdentityScreen(me,target);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void secretlyDisplayIdentity(Player target) {
		console.logSecretIdentityRevealMessage(target.getName(), target.getIdentity().toString());
		gui.displaySecretlyDisplayIdentity(target);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayPlayerHasResetCardScreen(Player player, RumourCard chosenCard) {
		console.logPlayerHasResetCardMessage(player.getName());
		displayCard(chosenCard, true);
		console.resetOffset();
		
		gui.displayPlayerHasResetCardScreen(player,chosenCard);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayTakeNextTurnScreen(Player p) {
		console.logTakeNextTurnMessage(p.getName());
		
		gui.displayTakeNextTurnScreen(p);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayPlayTurnAgainScreen(Player p) {
		console.logPlayTurnAgainMessage(p.getName());
		
		gui.displayPlayTurnAgainScreen(p);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayForcedToAccuseScreen(Player theOneWhoMustAccuse, Player theOneWhoForcedThem) {
		console.logForcedToAccuseMessage(theOneWhoMustAccuse.getName(),theOneWhoForcedThem.getName(),theOneWhoForcedThem.isImmunized());
		
		gui.displayForcedToAccuseScreen(theOneWhoMustAccuse,theOneWhoForcedThem);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayStealCardScreen(Player thief, Player stolenPlayer) {
		console.logStealCardMessage(thief.getName(),stolenPlayer.getName());
		
		gui.displayStealCardScreen(thief,stolenPlayer);
		
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayScoreBoard(ScoreBoard sb) {
		console.increaseTabulation();
		console.logScoreBoard(sb.toString());
		console.decreaseTabulation();
		
		gui.displayScoreBoard(sb);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayGameIsTiedScreen(List<Player> potentialWinners) {
		console.logGameIsTiedScreen(potentialWinners.get(0).getScore(),potentialWinners.stream().map(p->p.getName()).toList());
		
		gui.displayGameIsTiedScreen(potentialWinners.get(0).getScore(),potentialWinners);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayStrategyChange(Player p, PlayStrategy strat) {
		if(Game.getInstance().displayChangesOfStrategy()) {
			console.logStrategyChange(p.getName(),strat.toString());
			gui.displayStrategyChange(p,strat);
		}
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayHasChosenIdentityScreen(Player p) {
		console.logHasChosenIdentityMessage(p.getName());
		gui.displayHasChosenIdentityScreen(p);
	}
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayAllPlayersHaveChosenTheirIdentityScreen() {
		console.logAllPlayersHaveChosenTheirIdentityMessage();
		gui.displayAllPlayersHaveChosenTheirIdentityScreen();
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayNoAvailableHuntEffectsScreen() {
		console.logNoAvailableHuntEffectsMessage();
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayPlayerChoseToWitchScreen(Player player) {
		gui.showWitchingPlayer(player);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayChooseCardToDiscardScreen(Player p) {
		gui.displayChooseCardToDiscardScreen(p);
	}

	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public void displayExitingGameScreen() {
		console.logExitingGameScreen();
	}
	
	
	
	

	public void setConsole(StdView console) {
		this.console=console;
	}
	
	public void setGUI(GUIView gui) {
		this.gui=gui;
	}
	
}
