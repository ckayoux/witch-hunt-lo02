package fr.sos.witchhunt.view.std;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	
	private void logStarsLine() {
		log("*********************************************************");
	}
	
	private void logDashedLine() {
		log("---------------------------------------------------------");
	}
	
	private void crlf() {
		log("");
	}
	
	public void invite() {
		System.out.print("\t>> ");
	}
	
	public void makeMenu(Menu m) {
		log(m.getName());
		logDashedLine();
		int n=0;
		for (String str : m.getOptions()) {
			n++;
			log("\t "+Integer.toString(n)+" - "+ str);
		}
		crlf();
	}
	
	public int selectOption(Menu m) {
		Scanner sc = new Scanner(System.in); //TODO : gérer les conflits avec le gui
		boolean correct=true;
		int choice;
		int n = m.getOptionsNumber();
		do {
			invite();
			choice = Integer.parseInt(sc.nextLine());
			if(!(1 <= choice && choice <= n)) {
				correct = false;
				log("Invalid choice. Please enter an integer in the range 1.."+Integer.toString(n)+" :");
			}
		} while(!correct);
		sc.close();
		crlf();
		return choice;
	}
}
