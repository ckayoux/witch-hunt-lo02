package fr.sos.witchhunt.view.std;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.sos.witchhunt.controller.Application;
import fr.sos.witchhunt.view.Menu;


public final class StdView {
	
	//CONSTRUCTORS
	public StdView() {
		log("Welcome to Witch Hunt !");
		logStarsLine();
		crlf();
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
		log(m.getName());
		logHardLine();
		int n=0;
		for (String str : m.getOptions()) {
			n++;
			log("\t "+Integer.toString(n)+" - "+ str);
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
		log("Round "+roundNumber+" :");
		crlf();
	}
	
	public void logRoundEndMessage(int roundNumber) {
		crlf();
		log("Round "+roundNumber+" is over.");
		crlf();
	}
	
	public void logWinner(String name, int score) {
		//TEMPORARY
		crlf();
		log("Winner :");
		log("\tSorry, but my developpers are very lazy and \n\tI have yet no clue who the winner might possibly be.");
		crlf();
	}
	
	public void logScoreTable(String [][] table ) {
		//TEMPORARY
		log("Scores :");
		log("\tSorry, but this feature is not avaliable yet"); //TEMPORARY
		crlf();
	}

	public void yesNoQuestion(String q) {
		log(q+" (y/n) :");
	}

	public void logClassment(List <String> names, List <Integer> scores) {
		log("Classment :"); 
		log("\tSorry, but this feature is not avaliable yet"); //TEMPORARY
		crlf();
	}

}
