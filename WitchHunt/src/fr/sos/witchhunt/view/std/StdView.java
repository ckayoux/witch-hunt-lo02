package fr.sos.witchhunt.view.std;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.sos.witchhunt.controller.Application;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.Menu;


public final class StdView {
	
	int tabulation = 0;
	private String offset="";
	
	//CONSTRUCTORS
	public StdView() {
		log("Welcome to Witch Hunt !");
		logStarsLine();
	}
	
	public void tabbedLog(String msg) {
		tabbedPrint(msg);
		crlf();
	}
	
	public void log(String msg) {
		System.out.println(msg);
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
	
	public void makeMenu(Menu m) {
		crlf();
		logHardLine();
		log(m.getName().toUpperCase());
		logHardLine();
		int n=0;
		for (String str : m.getOptions()) {
			n++;
			log("\t "+Integer.toString(n)+" - "+ str);
		}
		crlf();
	}
	
	public void logPossibilities(Menu possibilities) {
		log("\t"+possibilities.getName());
		int n=0;
		for (String str : possibilities.getOptions()) {
			n++;
			log("\t\t "+Integer.toString(n)+" - "+ str);
		}
		crlf();
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
	
	public void logWinner(String name, int score) {
		//TEMPORARY
		crlf();
		log("\tWINNER :");
		log("\t\t!--Sorry, but my developpers are very lazy and \n\tI have yet no clue who the winner might possibly be.--!");
		crlf();
	}
	
	public void logScoreTable(String [][] table ) {
		//TEMPORARY
		logWeakDashedLine();
		crlf();
		log("\tSCOREBOARD :");
		log("\t\t!--This feature is not avaliable yet--!"); //TEMPORARY
		crlf();
	}

	public void yesNoQuestion(String q) {
		log(q+" (y/n) :");
	}

	public void logClassment(List <String> names, List <Integer> scores) {
		log("\tCLASSMENT :"); 
		log("\t\t!--This feature is not avaliable yet--!"); //TEMPORARY
		crlf();
	}

	public void logChooseIdentityMessage() {
		logWeakDashedLine();
		crlf();
		log("\t~ Please choose your identity for this round ~");
		crlf();
	}

	public void logHandDistributionMessage(int cardsNumber,int discardedCardsNumber) {
		crlf();
		log("\t~ Each player has received " + cardsNumber + " Rumour cards ~");
		if(discardedCardsNumber > 0) log("\t~ "+ discardedCardsNumber +" were put into the pile. ~");
		crlf();
		logWeakDashedLine();
	}

	public void logPlayTurnMessage(String playerName) {
		log("\tIt is " + playerName + (playerName.charAt(playerName.length()-1)!='s'?"'s":"'") + " turn.");
	}

	public void logAccusationMessage(String accusatorName, String accusedName) {
		log("\n\t"+accusedName + ", " + accusatorName + " accused you of practicing witchcraft !");
	}
	
	public void logChooseDefenseMessage() {
		log("\tWhat do you have to say in your defense ?\n");
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
		else if (points<0) print("\t"+playerName+" lost "+Integer.toString(points));
		else;
		print(" " + ( (Math.abs(points) == 1)?"point":"points" ) + ".");
		print(" (Total : "+ totalPlayerScore+")\n");
	}

	public void logEliminationMessage(String eliminatorName, String victimName) {
		if(eliminatorName!=victimName)
			log("\t"+eliminatorName + " has eliminated " + victimName + ".");
		else 
			log("\t"+eliminatorName+" has eliminated themselve.");
	}

	public void logLastUnrevealedMessage(String playerName) {
		log("\n\t" + playerName + " is the last unrevealed player remaining. It turns out...");
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
		String off=" ".repeat(offset.length())+"  ";

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
		String off=" ".repeat(offset.length())+"  ";
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

	public void logSelectCardMessage() {
		log("\tSelect a card among these ones :");
	}

	public void logSelectUnrevealedCardMessage() {
		log("\tSelect an unrevealed card among these ones :");
	}

	public void logSelectRevealedCardMessage() {
		log("\tSelect a revealed card among these ones :");
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
		setOffset("\t"+playerName + " has chosen ");
		logRumourCard(cardName,revealed,additionnalEffectDescription,witchEffectDescription,huntEffectDescription);
		resetOffset();
		crlf();
	}
	public void logHasChosenCardMessage(String playerName,String cardName,boolean revealed,String witchEffectDescription,String huntEffectDescription) {
		setOffset("\t"+playerName + " has chosen ");
		logRumourCard(cardName,revealed,witchEffectDescription,huntEffectDescription);
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

}
