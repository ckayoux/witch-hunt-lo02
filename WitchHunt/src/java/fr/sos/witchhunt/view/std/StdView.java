package fr.sos.witchhunt.view.std;

import java.util.List;

import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;


public final class StdView {
	
	int tabulation = 0;
	private String offset="";
	
	//CONSTRUCTORS
	public StdView() {
		logStarsLine();
		increaseTabulation();
		increaseTabulation();
		logSplash();
		crlf();
		logStarsLine();
		crlf();
		tabbedLog("Welcome to Witch Hunt !");
		decreaseTabulation();
		decreaseTabulation();
	}
	
	public void tabbedLog(String msg) {
		tabbedPrint(msg);
		crlf();
	}
	
	public void log(String msg) {
		System.out.println(msg.replace("/c/",""));
	}
	
	public void tabbedPrint(String msg) {
		StringBuffer sb = new StringBuffer();
		StringBuffer megaTabSb= new StringBuffer();
		if(tabulation != 0) {
			for(int i=0; i<this.tabulation; i++) {
				megaTabSb.append('\t');
			}
			sb.append(megaTabSb.toString());
			
		}
		sb.append(this.offset);
		megaTabSb.append(" ".repeat(this.offset.length()));
		sb.append(msg.replace("/+/", megaTabSb.toString()));
		System.out.print(sb.toString());
	}
	public void print(String msg) {
		System.out.print(msg);
	}
	
	public void logStarsLine() {
		log("*********************************************************");
	}
	
	public void logDashedLine() {
		log("---------------------------------------------------------");
	}
	
	public void logWeakDashedLine() {
		log("     - - - - - - - - - - - - - - - - - - - - - - - - - - -");
	}
	
	public void logHardLine() {
		log("―――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
	}
	
	public void crlf() {
		//new line
		log("");
	}
	
	public void invite() {
		System.out.print("\t>> ");
	}
	
	public void logContinueMessage() {
		log("\tPress ENTER to continue:");
	}
	
	public String stringifyMenuOption(Object o) {
		if (o instanceof String) {
			String str = (String) o;
			return str.replaceFirst("^/c/", ""); //console only
		}
		else if (o instanceof Identity) {
			Identity i = (Identity) o;
			switch (i) {
			case VILLAGER:
				return "Villager";
			case WITCH:
				return "Witch";
			}
		}
		else if (o instanceof TurnAction) {
			TurnAction ta = (TurnAction) o;
			switch (ta) {
				case ACCUSE:
					return "Accuse another player";
				case HUNT:
					return "Play the Hunt! effect of a Rumour card from your hand";
			}
		}
		else if (o instanceof DefenseAction) {
			DefenseAction da = (DefenseAction) o;
			switch (da) {
				case REVEAL:
					return "Reveal your identity";
				case WITCH:
					return "Play the Witch? effect of a Rumour card from your hand";
				case DISCARD:
					return "Discard a card";
			}
		}
		else if (o instanceof Player) {
			Player p = (Player) o;
			return p.getName();
		}
		else if (o instanceof RumourCard) {
			RumourCard rc = (RumourCard) o;
			return rc.getName();
		}
		return o.toString();
	}
	
	public void makeMenu(Menu m) {
		//used for main menus
		crlf();
		logHardLine();
		log("  "+m.getName().toUpperCase());
		logHardLine();
		int n=1;
		for (Object o : m.getOptions()) {
			StringBuffer sb = new StringBuffer("\t ");
			sb.append(n);
			sb.append(" - ");
			sb.append(stringifyMenuOption(o));
			log(sb.toString());
			n++;
		}
		crlf();
	}
	
	public void logPossibilities(Menu possibilities) {
		//used for making choices within a match
		log("\t"+possibilities.getName());
		int n=1;
		for (Object o : possibilities.getOptions()) {
			StringBuffer sb = new StringBuffer("\t\t ");
			sb.append(n);
			sb.append(" - ");
			sb.append(stringifyMenuOption(o));
			log(sb.toString());
			n++;
		}
		crlf();
	}

	public void logSplash() {
		tabbedPrint(" _     _         	\n"
				+ "/+/' )   / _/_    /    \n" 
				+ "/+/ / / /o /  _. /_       _   ,_,   _\n"
				+ "/+/(_(_/<_<__(__/ /_     / `'=) (='` \\\\ \n");
		tabbedLog(" _    ,             /.-.-.\\\\ /.-.-.\\\\ \n"
				+ "/+/' )  /          _/_  `      \\\"      `\n"
				+ "/+/ /--/ . . ____  /   \n"
				+ "/+//  (_(_/_/ / <_<__");

	}
	
	public void logMatchStartMessage() {
		crlf();
		logHardLine();
		log("Let the witch hunt begin !");
	}
	
	public void logMatchEndMessage() {
		logDashedLine();
		log("The game is over.");
		logHardLine();
	}
	
	public void logRoundStartMessage(int roundNumber) {
		logDashedLine();
		log("ROUND "+roundNumber+" :");
		crlf();
	}
	
	public void logRoundEndMessage(int roundNumber) {
		crlf();
		log("ROUND "+roundNumber+" IS OVER.");
		crlf();
	}
	
	public void logWinnerMessage(String name, int score) {
		//TEMPORARY
		crlf();
		log("\t~ "+name+ " has won the game with a score of "+score+" ! ~");
		crlf();
	}
	

	public void yesNoQuestion(String q) {
		log(q+" (y/n) :");
	}

	

	public void logChooseIdentityMessage() {
		logWeakDashedLine();
		crlf();
		log("\t~ Please choose your identity for this round ~");
		crlf();
	}
	
	public void logHasChosenIdentityMessage(String playerName) {
		log("\t"+playerName+" has chosen their identity.");
	}
	
	public void logAllPlayersHaveChosenTheirIdentityMessage() {
		crlf();
		logWeakDashedLine();
	}


	public void logHandDistributionMessage(int cardsNumber,int discardedCardsNumber) {
		crlf();
		log("\t~ Each player has received " + cardsNumber + " Rumour cards ~");
		if(discardedCardsNumber > 0) log("\t~ "+ discardedCardsNumber +" were put into the pile. ~");
		crlf();
		logWeakDashedLine();
		crlf();
	}

	public void logPlayTurnMessage(String playerName) {
		log("\tIt is " + playerName + (playerName.charAt(playerName.length()-1)!='s'?"'s":"'") + " turn.");
	}

	public void logEndOfTurnMessage() {
		crlf();
		logWeakDashedLine();
		crlf();
	}
	
	
	public void logAccusationMessage(String accusatorName, String accusedName) {
		log("\n\t"+accusedName + ", " + accusatorName + " accused you of practicing witchcraft !");
	}
	
	public void logChooseDefenseMessage() {
		log("\tThe village's pyre has been lit up.\n");
	}

	public void logForcedToRevealMessage() {
		log("\tYou have no other choice but to reveal who you really are.\n");
	}
	
	public void logGoingToRevealMessage(String playerName) {
		log("\t"+playerName+" is going to reveal their identity...");
	}
	
	public void logVillagerRevealMessage(String playerName) {
		log("\t"+playerName+" was only a villager.\n");
	}
	public void logWitchEliminatedMessage(String playerName) {
		increaseTabulation();
		tabbedLog("           (                      )      ____ \n"
				+ "/+/ (  (      )\\ )  *   )   (     ( /(     |   / \n"
				+ "/+/ )\\))(   '(()/(` )  /(   )\\    )\\())    |  /  \n"
				+ "/+/((_)()\\ )  /(_))( )(_))(((_)  ((_)\\     | /   \n"
				+ "/+/_(())\\_)()(_)) (_(_()) )\\___   _((_)    |/    \n"
				+ "/+/\\ \\((_)/ /|_ _||_   _|((/ __| | || |   (      \n"
				+ "/+/ \\ \\/\\/ /  | |   | |   | (__  | __ |   )\\     \n"
				+ "/+/  \\_/\\_/  |___|  |_|    \\___| |_||_|  ((_)    \n");
		decreaseTabulation();
		//log("\t"+playerName+" was a witch !\n");
	}
	public void logWitchRevealMessage(String playerName) {
		log("\t"+playerName+" was a witch !\n");
	}
	
	public void logTakeNextTurnMessage(String playerName) {
		log("\t"+playerName+" takes the next turn.");
	}
	
	public void logPlayTurnAgainMessage(String playerName) {
		log("\t"+playerName+" can play again.");
	}

	public void logUpdateScreenMessage(String playerName, int points, int totalPlayerScore) {
		if(points>0) print("\t"+playerName+" earned "+Integer.toString(points));
		else if (points<0) print("\t"+playerName+" lost "+Integer.toString(Math.abs(points)));
		else;
		print(" " + ( (Math.abs(points) == 1)?"point":"points" ) + ".");
		print(" (Total : "+ totalPlayerScore+")\n");
	}

	public void logEliminationMessage(String eliminatorName, String victimName) {
		if(eliminatorName!=victimName)
			log("\t"+eliminatorName + " has sentenced " + victimName + " to the stake !");
		else 
			log("\t"+eliminatorName+" has eliminated themselve.");
	}

	public void logLastUnrevealedMessage(String playerName) {
		log("\t" + playerName + " is the last unrevealed player remaining. It turns out...");
	}

	public void logNoCardsMessage(String playerName) {
		log("\t" + playerName + " has got not cards.");
	}

	public void logOnlyTwoUnrevealedRemainingMessage() {
		log("\t~ Only two unrevealed players remaining ! ~");
	}

	public void logUnrevealedCard() {
		tabbedLog("*Unrevealed*");
	}
	
	//SETTERS
	public void setTabulation(int t) {
		this.tabulation=t;
	}
	public void increaseTabulation() {
		this.tabulation++;
	}
	public void decreaseTabulation() {
		this.tabulation--;
	}

	public void logRumourCard(String name, boolean revealed, String additionnalEffectDescription, String witchEffectDescription,
		String huntEffectDescription) {
		tabbedLog(name + ((revealed)?"\t(Revealed)":""));
		String off=" ".repeat(offset.length());//+"  ";
		
			setOffset(off);
			tabbedLog("*" + additionnalEffectDescription + "*");
			setOffset(off+"Witch : ");
			tabbedLog(witchEffectDescription);
			setOffset(off+"Hunt  : ");
			tabbedLog(huntEffectDescription);
			resetOffset();
		crlf();
	}
	public void logRumourCard(String name, boolean revealed, String witchEffectDescription, String huntEffectDescription) {
		tabbedLog(name + ((revealed)?"\t(Revealed)":""));
		String off=" ".repeat(offset.length());//+"  ";
			setOffset(off+"Witch : ");
			tabbedLog(witchEffectDescription);
			setOffset(off+"Hunt  : ");
			tabbedLog(huntEffectDescription);
			resetOffset();

		crlf();
	}

	public void logEffect(String name, String effectDescription) {
		tabbedLog(name+" :");
		setBlankOffset(this.offset+"  ");
		tabbedLog(effectDescription);
	}

	public void logEffect(String name, String additionnalEffectDescription, String effectDescription) {
		tabbedLog(name+" :");
		setBlankOffset(this.offset+"  ");
		tabbedLog("*"+additionnalEffectDescription +"*\n/+/" + effectDescription);
	}

	public void logShowPlayersCardsMessage(String playerName) {
		log("\t"+playerName + ", here are all the cards in your possession (others, don't look !) :");
	}
	
	public void logSelectCardsMessage(String cardAdjective) {
		log("\tSelect any "+((cardAdjective!=null)?cardAdjective+" ":"")+"card among these ones :");
	}

	public void logPlayerPlaysEffectMessage(String playerName,String cardName, String effectDescription) {
		setOffset("\t"+playerName+ " uses ");
		logEffect(cardName,effectDescription);
		resetOffset();
		crlf();
		
	}
	public void logPlayerPlaysEffectMessage(String playerName,String cardName, String effectDescription, String additionnalEffectDescription) {
		setOffset("\t"+playerName + " uses ");
		logEffect(cardName,additionnalEffectDescription,effectDescription);
		resetOffset();
		crlf();
	}

	public void setOffset(String str) {
		this.offset=str;
	}
	public void setBlankOffset(String str) {
		this.offset=" ".repeat(str.length());
	}
	public void addOffset(String str) {
		this.offset+=str;
	}
	public void resetOffset() {
		this.offset="";
	}

	public void logHasChosenCardMessage(String playerName,String cardName,boolean revealed,String additionnalEffectDescription,
			String witchEffectDescription,String huntEffectDescription) {
		setOffset("\t"+playerName + " has taken ");
		if(revealed) logRumourCard(cardName,revealed,additionnalEffectDescription,witchEffectDescription,huntEffectDescription);
		else logUnrevealedCard();
		resetOffset();
		crlf();
	}
	public void logHasChosenCardMessage(String playerName,String cardName,boolean revealed,String witchEffectDescription,String huntEffectDescription) {
		setOffset("\t"+playerName + " has taken ");
		if(revealed) logRumourCard(cardName,revealed,witchEffectDescription,huntEffectDescription);
		else logUnrevealedCard();
		resetOffset();
		crlf();
	}

	public void logEmptyHandMessage(String ownersName) {
		log("\t"+ownersName+"'"+((ownersName.charAt(ownersName.length()-1)!='s')?"s":"")+ "hand is empty.");
	}
	public void logEmptyPileMessage() {
		log("\tThe pile is empty.");
	}

	public void printPlayerDiscardedCardMessage(String playerName) {
		setOffset("\t"+playerName + " has discarded ");
	}

	public void logLookAtPlayersIdentityMessage(String myName, String targetsName) {
		log("\t"+myName + " is looking at "+targetsName+"'"+((targetsName.charAt(targetsName.length()-1)!='s')?"s":"") + " identity.");
	}

	public void logSecretIdentityRevealMessage(String targetsName, String identity) {
		log("\tOthers, close your eyes !");
		log("\t\t"+targetsName + " is a "+identity.toLowerCase()+ ".\n");
	}

	public void logWasAlreadyRevealedAs(String playerName, String identity) {
		log("\t"+playerName + " was already revealed as a "+identity.toLowerCase()+ ".\n");
	}

	public void logPlayerHasResetCardMessage(String playerName) {
		setOffset("\t"+playerName + " took back ");
		
	}

	public void logForcedToAccuseMessage(String accuserName, String forcedByName, boolean immunizedThemselve) {
		log("\t"+accuserName+", "+forcedByName+" forced you to accuse "+((immunizedThemselve)?"someone else ":"them ")+ "!");
	}

	public void logStealCardMessage(String thiefName,String stolenName) {
		log("\t"+thiefName+" is subtilizing a card from "+stolenName+" !");
	}

	public void logScoreBoard(String scoreBoardAsString) {
		tabbedLog("SCOREBOARD :\n");
		addOffset("   ");
		tabbedLog(scoreBoardAsString);
		resetOffset();
	}

	public void logGameIsTiedScreen(int score, List<String> duelists) {
		crlf();
		logDashedLine();
		log("\t~ The game is tied ! ~");
		StringBuffer sb = new StringBuffer("\t");
		for(int i=0; i<duelists.size(); i++) {
			sb.append(duelists.get(i));
			if(i<duelists.size()-2) sb.append(", ");
			else if(i==duelists.size()-2) sb.append(" and ");
		}
		sb.append((duelists.size()>2)?" all":" both");
		sb.append(" have a score of ");
		sb.append(score);
		sb.append(" and must compete for the first place !");
		log(sb.toString());
	}

	public void logPlayerAndTheirScore(int ranking, boolean exAequo, String name, int score) {
		StringBuffer sb = new StringBuffer();
		if(ranking!=-1) {
			sb.append(ranking);
			sb.append((exAequo)?" (ex aequo)":"");
			sb.append(" : ");
			
		}
		sb.append(name);
		sb.append(' ');
		tabbedLog(sb.toString()+"-".repeat(45-sb.toString().length())+" "+score);
	}

	public void logPlayerRankingMessage(boolean exAequo, int rank, int score) {
		tabbedLog("You are ranked n°"+rank+" "+((exAequo)?"(ex aequo) ":"")+"with a score of "+score+".");
	}

	public void logStrategyChange(String name, String playstyleDesc) {
		log("\t"+name+" opts for "+playstyleDesc+".");
	}

	public void logAddPlayersScreen(int minPlayersNumber,int maxPlayersNumber) {
		logDashedLine();
		log("~ Add "+minPlayersNumber+" to "+maxPlayersNumber+" players : ~\n");
	}

	public void logWrongMenuChoiceMessage(int timesWrong, String helperMsg, int optionsCount) {
		if(timesWrong==2) {
			log("\tAre you doing it on purpose ?");
			log("\t"+helperMsg);
		}
		else if (timesWrong==3) {
			log("\tCome on ! I believe in you ! You can do it !");
			if(optionsCount!=1)log("\t"+helperMsg);
		}
		else {
			if(optionsCount==1) {
				log("\tYou can only choose option #1 here !");
			}
			else log("\tInvalid choice. "+helperMsg);
		}
	}

	public void logAddedPlayersScreen(int n) {
		crlf();
		log("All "+Integer.toString(n)+" players have been successfully added.");
		logDashedLine();
		crlf();
	}

	public void logExitingGameScreen() {
		crlf();
		logStarsLine();
		log("See you soon !");
	}

	public void logInputWasExpectedMessage() {
		log("\tPlease make your choice.");
	}

	public void logInvalidYesNoQuestionAnswerMessage() {
		log("\tInvalid answer. Please type in whether 'y' or 'n' :");
	}

	public void logNoAvailableHuntEffectsMessage() {
		log("\tYou have no more available Hunt! effects.");
	}

	public void logSelectHuntCardMessage() {
		log("\tSelect a card with a valid Hunt! effect :");
	}

	public void logSelectWitchCardMessage() {
		log("\tSelect a card with a valid Witch? effect :");
		
	}

	public void logAddedPlayerMessage(Player p) {
		log("Added "+p.getName()+((p instanceof HumanPlayer)?" (Human)":""));
		crlf();
	}



	

}
