package fr.sos.witchhunt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.sos.witchhunt.DisplayMediator;
import fr.sos.witchhunt.controller.ScoreCounter.ScoreBoard;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy;
import fr.sos.witchhunt.view.std.StdView;

public final class DisplayController implements DisplayMediator {
	
	private StdView console;
	
	
	public void displayMenu(Menu m) {
		console.makeMenu(m);
		//T0D0 : makeMenu for GUI view
	}
	
	public void displayPossibilities(Menu possibilities) {
		console.logPossibilities(possibilities);
		//T0D0 : makeMenu for GUI view
	}
	
	public void displayYesNoQuestion(String q) {
		console.yesNoQuestion(q);
		//T0D0 : makeMenu for GUI view
	}
	
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
	}
	
	@Override
	public void passLog(String msg) {
		console.log(msg);
	}
	
	public void crlf() {
		console.crlf();
	}
	
	public void displayContinueMessage() {
		console.logContinueMessage();
	}
	
	public void displayMatchStartScreen() {
		console.logMatchStartMessage();
		//TODO : equivalent for gui view
	}

	public void displayMatchEndScreen() {
		console.logMatchEndMessage();
		//TODO : equivalent for gui view
	}
	
	public void displayRoundStartScreen(int roundNumber) {
		console.logRoundStartMessage(roundNumber);
		//TODO : equivalent for gui view
	}

	public void displayRoundEndScreen(int roundNumber) {
		console.logRoundEndMessage(roundNumber);
		//TODO : equivalent for gui view
	}
	
	
	public void displayWinnerScreen(Player winner) {
		console.logWinnerMessage(winner.getName(), winner.getScore());
	}
	
	public void setConsole(StdView console) {
		this.console=console;
	}
	
	public void displayRanking(List <Player> ranking) {
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
	}
	public void displayRanking(Player P) {
		int rank=1;
		int pSRank=-1;
		boolean pIsExAequo=false;
		List<Player> ranking = Tabletop.getInstance().getScoreCounter().getRanking();
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

	public void chooseIdentityScreen() {
		console.logChooseIdentityMessage();
		//TODO : equivalent for gui
	}

	public void distributeHandScreen() {
		int totalRumourCardsCount = Tabletop.getInstance().getTotalRumourCardsCount();
		int playersCount = Tabletop.getInstance().getActivePlayersList().size();
		int distributedCardsCount = (int)Math.floor( totalRumourCardsCount / (float)playersCount );
		int discardedCardsCount = totalRumourCardsCount % playersCount;
		console.logHandDistributionMessage(distributedCardsCount,discardedCardsCount);
		console.crlf();
		//TODO : equivalent for gui
	}

	@Override
	public void displayPlayTurnScreen(String playerName) {
		console.logPlayTurnMessage(playerName);
		
	}

	@Override
	public void displayAccusationScreen(Player accusator, Player accused) {
		console.logAccusationMessage(accusator.getName(),accused.getName());
		// TODO equivalent for gui
	}
	
	@Override
	public void displayChooseDefenseScreen() {
		console.logChooseDefenseMessage();
		//ToutDoux : équivalent for gui
	}

	@Override
	public void displayForcedToRevealScreen() {
		console.logForcedToRevealMessage();
		//ToutDoux : equivalent for gui
	}

	@Override
	public void displayIdentityRevealScreen(Player p) {
		if(!p.isRevealed()){
			console.logGoingToRevealMessage(p.getName());
			switch(p.getIdentity()) {
				case VILLAGER:
					console.logVillagerRevealMessage(p.getName());
					break;
				case WITCH:
					if(Tabletop.getInstance().getLastUnrevealedPlayer()==p)
						console.logWitchRevealMessage(p.getName());
					else
						console.logWitchEliminatedMessage(p.getName());
					break;
			}
		}
		else console.logWasAlreadyRevealedAs(p.getName(),p.getIdentity().toString());
		// Tout doux : equivalent for gui
	}

	@Override
	public void displayScoreUpdateScreen(Player p, int points) {
		console.logUpdateScreenMessage(p.getName(),points,p.getScore());
	}

	@Override
	public void displayEliminationScreen(Player eliminator, Player victim) {
		console.logEliminationMessage(eliminator.getName(),victim.getName());
		// Tout doux : equivalent for gui
	}

	public void displayLastUnrevealedPlayerScreen(Player lastUnrevealedPlayer) {
		console.logLastUnrevealedMessage(lastUnrevealedPlayer.getName());
		switch(lastUnrevealedPlayer.getIdentity()) {
			case WITCH:
				console.logWitchRevealMessage(lastUnrevealedPlayer.getName());
				break;
			case VILLAGER:
				console.logVillagerRevealMessage(lastUnrevealedPlayer.getName());
				break;
		}
		// Tout doux : equivalent for gui
	}
	
	public void displayNoCardsScreen(Player p) {
		console.logNoCardsMessage(p.getName());
		//ToutDoux : equivalent for gui
	}

	public void displayOnlyTwoUnrevealedRemainingScreen() {
		console.logOnlyTwoUnrevealedRemainingMessage();
		//ToutDoux : equivalent for gui
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
	public void displaySelectCardScreen() {
		console.logSelectCardMessage();
	}

	@Override
	public void displaySelectUnrevealedCardScreen() {
		console.logSelectUnrevealedCardMessage();
		
	}
	
	@Override
	public void displaySelectRevealedCardScreen() {
		console.logSelectRevealedCardMessage();
		
	}


	@Override
	public void displayPlayerPlaysWitchEffectScreen(Player p,RumourCard rc) {
		if(rc.getAdditionnalEffectDescription()=="")
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getWitchEffectDescription());
		else
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getWitchEffectDescription(),rc.getAdditionnalEffectDescription());
	}
	
	@Override
	public void displayPlayerPlaysHuntEffectScreen(Player p,RumourCard rc) {
		if(rc.getAdditionnalEffectDescription()=="")
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getHuntEffectDescription());
		else
			console.logPlayerPlaysEffectMessage(p.getName(),rc.getName(),rc.getHuntEffectDescription(),rc.getAdditionnalEffectDescription());
	}

	@Override
	public void displayHasChosenCardScreen(Player p, RumourCard chosen,boolean forceReveal) {
		if(chosen.getAdditionnalEffectDescription()=="") 
			console.logHasChosenCardMessage(p.getName(),chosen.getName(),(chosen.isRevealed()||forceReveal),
					chosen.getWitchEffectDescription(),chosen.getHuntEffectDescription());
		else 
			console.logHasChosenCardMessage(p.getName(),chosen.getName(),(chosen.isRevealed()||forceReveal),chosen.getAdditionnalEffectDescription(),
					chosen.getWitchEffectDescription(),chosen.getHuntEffectDescription());
	}
	
	@Override
	public void displayNoCardsInPileScreen(RumourCardsPile rcp) {
		if(rcp.getOwner()!=null) {
			console.logEmptyHandMessage(rcp.getOwner().getName());
		}
		else if(rcp.isThePile()) {
			console.logEmptyPileMessage();
		}
	}

	@Override
	public void displayDiscardCardScreen(Player owner,RumourCard rc) {
		console.printPlayerDiscardedCardMessage(owner.getName());
		displayCard(rc, false);
		console.resetOffset();
	}

	@Override
	public void displayLookAtPlayersIdentityScreen(Player me, Player target) {
		console.logLookAtPlayersIdentityMessage(me.getName(),target.getName());
	}

	@Override
	public void secretlyDisplayIdentity(Player target) {
		console.logSecretIdentityRevealMessage(target.getName(), target.getIdentity().toString());
	}

	@Override
	public void displayPlayerHasResetCardScreen(Player player, RumourCard chosenCard) {
		console.logPlayerHasResetCardMessage(player.getName());
		displayCard(chosenCard, true);
		console.resetOffset();
	}
	
	@Override
	public void displayTakeNextTurnScreen(Player p) {
		console.logTakeNextTurnMessage(p.getName());;
	}
	@Override
	public void displayPlayTurnAgainScreen(Player p) {
		console.logPlayTurnAgainMessage(p.getName());
	}
	public void freezeDisplay(int duration) {
		if(Game.getInstance().sleepingIsAllowed()) {
			/*try{
				Application.inputController.wait(duration);
				this.wait(duration);
				Application.inputController.notify();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		
	}
	
	@Override
	public void displayForcedToAccuseScreen(Player theOneWhoMustAccuse, Player theOneWhoForcedThem) {
		console.logForcedToAccuseMessage(theOneWhoMustAccuse.getName(),theOneWhoForcedThem.getName(),theOneWhoForcedThem.isImmunized());
	}

	@Override
	public void displayStealCardScreen(Player thief, Player stolenPlayer) {
		console.logStealCardMessage(thief.getName(),stolenPlayer.getName());
		
	}
	
	public void displayScoreBoard(ScoreBoard sb) {
		console.increaseTabulation();
		console.logScoreBoard(sb.toString());
		console.decreaseTabulation();
	}

	public void displayGameIsTiedScreen(List<Player> potentialWinners) {
		console.logGameIsTiedScreen(potentialWinners.get(0).getScore(),potentialWinners.stream().map(p->p.getName()).toList());
	}

	@Override
	public void displayStrategyChange(Player p, PlayStrategy strat) {
		/*if(Game.getInstance().cpuPlayersDisplayChangesOfStrategy())*/ console.logStrategyChange(p.getName(),strat.toString());
	}

	
}
