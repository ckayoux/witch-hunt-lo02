package fr.sos.witchhunt.controller;

import java.util.List;

import fr.sos.witchhunt.DisplayMediator;
import fr.sos.witchhunt.controller.ScoreCounter.ScoreBoard;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy;
import fr.sos.witchhunt.view.gui.GUIView;
import fr.sos.witchhunt.view.std.StdView;

public final class DisplayController implements DisplayMediator {
	
	private StdView console;
	private GUIView gui;
	
	@Override
	public void displayMenu(Menu m) {
		console.makeMenu(m);
		gui.gotoMainMenuPanel();
		gui.displayMenu(m);
	}
	
	@Override
	public void displayAddedPlayerScreen(Player p) {
		console.logAddedPlayerMessage(p);
		gui.displayPlayerAddedScreen(p);
	}
	
	@Override
	public void displayAddedPlayersScreen(int n) {
		console.logAddedPlayersScreen(n);
	}
	
	@Override
	public void displayPossibilities(Menu possibilities) {
		console.logPossibilities(possibilities);
		gui.displayMenu(possibilities);
		//T0D0 : makeMenu for GUI view
	}
	
	@Override
	public void logYesNoQuestion(String q) {
		console.yesNoQuestion(q);
	}
/*	
	public void drawStarsLine() {
		console.logStarsLine();
	}
	
	public void drawHardLine() {
		console.logHardLine();
	}
	
	public void drawDashedLine() {
		console.logDashedLine();
	}
	
	public void drawWeakDashedLine() {
		console.logWeakDashedLine();
	}*/
	
	@Override
	public void passLog(String msg) {
		console.log(msg);
	}
	
	public void crlf() {
		console.crlf();
	}
	
	@Override
	public void displayAddPlayersScreen(Tabletop tabletop) {
		console.logAddPlayersScreen(tabletop.getMinPlayersNumber(), tabletop.getMaxPlayersNumber());
		gui.setTabletop(tabletop);
		gui.gotoMatchSetupPanel();
	}
	
	public void displayContinueMessage() {
		console.logContinueMessage();
	}
	
	@Override
	public void displayMatchStartScreen() {
		console.logMatchStartMessage();
		gui.gotoGamePanel();
	}


	@Override
	public void displayMatchEndScreen() {
		console.logMatchEndMessage();
		//TODO : equivalent for gui view
	}
	
	@Override
	public void displayRoundStartScreen(int roundNumber) {
		console.logRoundStartMessage(roundNumber);
		gui.displayRoundStartScreen(roundNumber);
	}

	@Override
	public void displayRoundEndScreen(int roundNumber) {
		console.logRoundEndMessage(roundNumber);
		gui.displayRoundEndScreen(roundNumber);
	}
	
	
	@Override
	public void displayWinnerScreen(Player winner) {
		console.logWinnerMessage(winner.getName(), winner.getScore());
		gui.displayWinnerScreen(winner);
	}
	

	
	/*public void displayRanking(List <Player> ranking) {
		console.increaseTabulation();
		if(ranking.stream().filter(p->p.getScore()==0).count()==ranking.size()) {
			ranking.forEach(p->console.logPlayerAndTheirScore(-1, false , p.getName(), 0));
		}
		else {
			int rank=1;
			int lastScore = ranking.get(0).getScore();
			for(Player p : ranking) {
				boolean exAequo = (ranking.stream().filter(p2->p.getScore()==p2.getScore()).count()>1);
				if(p.getScore()!=lastScore) {
					rank++;
					lastScore=p.getScore();
				}
				console.logPlayerAndTheirScore(rank, exAequo , p.getName(), p.getScore());
				
			};
		}
		console.crlf();
		console.decreaseTabulation();
	}*/
	@Override
	public void displayRanking(Player P,List<Player> ranking) {
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
	}

	@Override
	public void displayChooseIdentityScreen() {
		console.logChooseIdentityMessage();
		gui.displayChooseIdentityScreen();
	}

	@Override
	public void distributeHandScreen(int distributedCardsCount,int discardedCardsCount) {
		console.logHandDistributionMessage(distributedCardsCount,discardedCardsCount);
		gui.displayHandDistributionScreen(distributedCardsCount,discardedCardsCount);
		//todo : start displaying their cards in the cards panel
	}

	@Override
	public void displayPlayTurnScreen(Player p) {
		console.logPlayTurnMessage(p.getName());
		gui.displayPlayTurnScreen(p);
	}


	@Override
	public void displayEndOfTurnScreen() {
		console.logEndOfTurnMessage();
		gui.displayEndOfTurnScreen();
	}
	
	@Override
	public void displayAccusationScreen(Player accusator, Player accused) {
		console.logAccusationMessage(accusator.getName(),accused.getName());
		gui.displayAccusationScreen(accusator,accused);
	}
	
	@Override
	public void displayHuntedPlayerScreen(Player huntedPlayer) {
		gui.showHuntedPlayer(huntedPlayer);
	}
	
	@Override
	public void displayChooseDefenseScreen(Player p) {
		console.logChooseDefenseMessage();
		gui.displayChooseDefenseMessage(p);
	}

	@Override
	public void displayForcedToRevealScreen() {
		console.logForcedToRevealMessage();
		gui.displayForcedToRevealScreen();
	}

	@Override
	public void displayGoingToRevealIdentityScreen(Player p) {
		console.logGoingToRevealMessage(p.getName());
		gui.displayGoingToRevealScreen(p);
	}
	
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
		//else console.logWasAlreadyRevealedAs(p.getName(),p.getIdentity().toString());
		// Tout doux : equivalent for gui
	}

	@Override
	public void displayScoreUpdateScreen(Player p, int points) {
		console.logUpdateScreenMessage(p.getName(),points,p.getScore());
		gui.displayScoreUpdateScreen(p,points);
	}

	@Override
	public void displayEliminationScreen(Player eliminator, Player victim) {
		console.logEliminationMessage(eliminator.getName(),victim.getName());
		gui.displayEliminationScreen(eliminator,victim);
	}

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
	
	@Override
	public void displayNoCardsScreen(Player p) {
		console.logNoCardsMessage(p.getName());
		gui.displayNoCardsScreen(p);
	}

	@Override
	public void displayOnlyTwoUnrevealedRemainingScreen() {
		console.logOnlyTwoUnrevealedRemainingMessage();
		gui.displayOnlyTwoUnrevealedRemainingScreen();
	}

	@Override
	public void displayCard(RumourCard rc, boolean forcedReveal) {
		if(rc.isRevealed() || forcedReveal) {
			if(rc.getAdditionnalEffectDescription().equals("")) {
				console.logRumourCard(rc.getName(),rc.isRevealed(),rc.getWitchEffectDescription(),rc.getHuntEffectDescription());
			}
			else {
				console.logRumourCard(rc.getName(),rc.isRevealed(),rc.getAdditionnalEffectDescription(),rc.getWitchEffectDescription(),rc.getHuntEffectDescription());
			}
		}
		else {
			console.logUnrevealedCard();
		}
	}

	@Override
	public void displayWitchEffect(RumourCard rc) {
		if(rc.getAdditionnalEffectDescription().equals("")) {
			console.logEffect(rc.getName(),rc.getWitchEffectDescription());
		}
		else {
			console.logEffect(rc.getName(),rc.getAdditionnalEffectDescription(),rc.getWitchEffectDescription());
		}
	}

	@Override
	public void displayHuntEffect(RumourCard rc) {
		if(rc.getAdditionnalEffectDescription().equals("")) {
			console.logEffect(rc.getName(),rc.getHuntEffectDescription());
		}
		else {
			console.logEffect(rc.getName(),rc.getAdditionnalEffectDescription(),rc.getHuntEffectDescription());
		}
	}

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
	

	@Override
	public void displaySelectCardScreen(Player p,RumourCardsPile from, boolean forcedReveal) {
		console.logSelectCardsMessage(null);
		this.displayCards(from, forcedReveal);
		
		gui.displayChooseAnyCardScreen(p,from);
	}

	@Override
	public void displaySelectUnrevealedCardScreen(Player p,RumourCardsPile from, boolean forcedReveal) {
		console.logSelectCardsMessage("unrevealed");
		this.displayCards(from, forcedReveal);
		
		gui.displayChooseUnrevealedCardScreen(p,from);
	}
	
	@Override
	public void displaySelectRevealedCardScreen(Player p,RumourCardsPile from, boolean forcedReveal) {
		console.logSelectCardsMessage("revealed");
		this.displayCards(from, forcedReveal);
		
		gui.displayChooseRevealedCardScreen(p,from);
		
	}
	
	@Override
	public void displaySelectWitchCardScreen(Player p,RumourCardsPile from) {
		console.logSelectWitchCardMessage();
		this.displayWitchEffects(from);
		
		gui.displayChooseWitchCardScreen(p,from);
	}
	

	
	@Override
	public void displaySelectHuntCardScreen(Player p,RumourCardsPile from) {
		console.logSelectHuntCardMessage();
		this.displayHuntEffects(from);

		gui.displayChooseHuntCardScreen(p, from);
	}


	@Override
	public void displayPlayerPlaysWitchEffectScreen(Player p,RumourCard rc) {
		if(rc.getAdditionnalEffectDescription()=="") 
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getWitchEffectDescription());
		else
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getWitchEffectDescription(),rc.getAdditionnalEffectDescription());
		gui.displayPlayerPlaysWitchEffectScreen(p,rc);
	}
	
	@Override
	public void displayPlayerPlaysHuntEffectScreen(Player p,RumourCard rc) {
		if(rc.getAdditionnalEffectDescription()=="")
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getHuntEffectDescription());
		else
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getHuntEffectDescription(),rc.getAdditionnalEffectDescription());
		gui.displayPlayerPlaysHuntEffectScreen(p, rc);
	}

	@Override
	public void displayHasChosenCardScreen(Player p, RumourCard chosen,RumourCardsPile from,boolean forceReveal) {
		if(chosen.getAdditionnalEffectDescription()=="") 
			console.logHasChosenCardMessage(p.getName(),chosen.getName(),(chosen.isRevealed()||forceReveal),
					chosen.getWitchEffectDescription(),chosen.getHuntEffectDescription());
		else 
			console.logHasChosenCardMessage(p.getName(),chosen.getName(),(chosen.isRevealed()||forceReveal),chosen.getAdditionnalEffectDescription(),
					chosen.getWitchEffectDescription(),chosen.getHuntEffectDescription());
		gui.displayPlayerHasChosenCardScreen(p, chosen,from,forceReveal);
	}
	
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

	@Override
	public void displayPlayerDiscardedCardScreen(Player owner,RumourCard rc) {
		console.printPlayerDiscardedCardMessage(owner.getName());
		displayCard(rc, false);
		console.resetOffset();
		
		gui.displayPlayerHasDiscardedCardScreen(owner,rc);
	}

	@Override
	public void displayLookAtPlayersIdentityScreen(Player me, Player target) {
		console.logLookAtPlayersIdentityMessage(me.getName(),target.getName());
		gui.displayLookAtPlayersIdentityScreen(me,target);
	}

	@Override
	public void secretlyDisplayIdentity(Player target) {
		console.logSecretIdentityRevealMessage(target.getName(), target.getIdentity().toString());
		gui.displaySecretlyDisplayIdentity(target);
	}

	@Override
	public void displayPlayerHasResetCardScreen(Player player, RumourCard chosenCard) {
		console.logPlayerHasResetCardMessage(player.getName());
		displayCard(chosenCard, true);
		console.resetOffset();
		
		gui.displayPlayerHasResetCardScreen(player,chosenCard);
	}
	
	@Override
	public void displayTakeNextTurnScreen(Player p) {
		console.logTakeNextTurnMessage(p.getName());
		
		gui.displayTakeNextTurnScreen(p);
	}
	@Override
	public void displayPlayTurnAgainScreen(Player p) {
		console.logPlayTurnAgainMessage(p.getName());
		
		gui.displayPlayTurnAgainScreen(p);
	}
	
	@Override
	public void displayForcedToAccuseScreen(Player theOneWhoMustAccuse, Player theOneWhoForcedThem) {
		console.logForcedToAccuseMessage(theOneWhoMustAccuse.getName(),theOneWhoForcedThem.getName(),theOneWhoForcedThem.isImmunized());
		
		gui.displayForcedToAccuseScreen(theOneWhoMustAccuse,theOneWhoForcedThem);
	}

	@Override
	public void displayStealCardScreen(Player thief, Player stolenPlayer) {
		console.logStealCardMessage(thief.getName(),stolenPlayer.getName());
		
		gui.displayStealCardScreen(thief,stolenPlayer);
		
	}
	
	@Override
	public void displayScoreBoard(ScoreBoard sb) {
		console.increaseTabulation();
		console.logScoreBoard(sb.toString());
		console.decreaseTabulation();
		
	//	gui.displayScoreBoard(sb);
	}

	@Override
	public void displayGameIsTiedScreen(List<Player> potentialWinners) {
		console.logGameIsTiedScreen(potentialWinners.get(0).getScore(),potentialWinners.stream().map(p->p.getName()).toList());
		
		gui.displayGameIsTiedScreen(potentialWinners.get(0).getScore(),potentialWinners);
	}

	@Override
	public void displayStrategyChange(Player p, PlayStrategy strat) {
		/*if(Game.getInstance().cpuPlayersDisplayChangesOfStrategy())*/ console.logStrategyChange(p.getName(),strat.toString());
	}

	@Override
	public void displayHasChosenIdentityScreen(Player p) {
		console.logHasChosenIdentityMessage(p.getName());
		gui.displayHasChosenIdentityScreen(p);
	}
	
	@Override
	public void displayAllPlayersHaveChosenTheirIdentityScreen() {
		console.logAllPlayersHaveChosenTheirIdentityMessage();
		gui.displayAllPlayersHaveChosenTheirIdentityScreen();
	}

	

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

	public StdView getConsole() {
		return this.console;
	}

	@Override
	public void displayNoAvailableHuntEffectsScreen() {
		console.logNoAvailableHuntEffectsMessage();
	}

	@Override
	public void displayPlayerChoseToWitchScreen(Player player) {
		gui.showWitchingPlayer(player);
	}

	@Override
	public void displayChooseCardToDiscardScreen(Player p) {
		gui.displayChooseCardToDiscardScreen(p);
	}


	
}
