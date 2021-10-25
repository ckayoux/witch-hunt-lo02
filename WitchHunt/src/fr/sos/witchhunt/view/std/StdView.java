package fr.sos.witchhunt.view.std;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.sos.witchhunt.Menu;
import fr.sos.witchhunt.controller.Application;


public final class StdView {
	
	//CONSTRUCTORS
	public StdView() {
		log("Welcome to Witch Hunt !");
		logStarsLine();
	}
	
	public void log(String msg) {
		System.out.println(msg);
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

}
