package fr.sos.witchhunt.view.std;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.sos.witchhunt.controller.Application;
import fr.sos.witchhunt.model.Menu;


public final class StdView {
	
	//CONSTRUCTORS
	public StdView() {
		log("Welcome to Witch Hunt !");
		logStarsLine();
	}
	
	public void log(String msg) {
		System.out.println(msg);
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
		log("\t"+playerName+" was a villager.\n");
	}
	public void logWitchRevealMessage(String playerName) {
		log("\t"+playerName+" was a witch !\n");
	}

	public void logUpdateScreenMessage(String playerName, int points, int totalPlayerScore) {
		if(points>0) print("\t"+playerName+" earned "+Integer.toString(points));
		else if (points<0) print("\t"+playerName+" lost "+Integer.toString(points));
		else;
		print(" " + ( (Math.abs(points) == 1)?"point":"points" ) + ".");
		print(" (Total : "+ totalPlayerScore+")\n");
	}

	public void logEliminationMessage(String eliminatorName, String victimName) {
		log("\t"+eliminatorName + " has eliminated " + victimName + ".");
	}

	public void logLastUnrevealedMessage(String playerName) {
		log("\n\t" + playerName + " is the last unrevealed player remaining. It turns out...");
	}

}
