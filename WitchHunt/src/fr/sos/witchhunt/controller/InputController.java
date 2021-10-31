package fr.sos.witchhunt.controller;

import java.util.Scanner;

import java.util.concurrent.CountDownLatch;

import fr.sos.witchhunt.PlayerInputObserver;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.players.CPUPlayer;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.std.InterruptibleStdInput;
import fr.sos.witchhunt.view.std.StdView;

public final class InputController implements PlayerInputObserver {

	//ATTRIBUTES
	private CountDownLatch latch = new CountDownLatch(1) ;
	private Thread stdInputThread;
	private String receivedString;
	
	public int makeChoice(Menu m) {
		int choice;
		boolean correct;
		int n = m.getOptionsNumber();
		int timesWrong=0;
		choice = getIntInput();
		if(!(1 <= choice && choice <= n)) {
				correct = false;
				timesWrong++;
				String helperMsg = "Please enter an integer in the range 1.."+Integer.toString(n)+" :" ;
				if(timesWrong==2) {
					Application.displayController.passLog("Are you doing it on purpose ?");
					if(n!=1)Application.displayController.passLog("helperMsg");
				}
				else if (timesWrong==3) {
					Application.displayController.passLog("Come on ! I believe in you ! You can do it !");
					if(n!=1)Application.displayController.passLog("helperMsg");
				}
				else {
					if(n==1) {
						Application.displayController.passLog("You can only choose option #1 here !");
					}
					else Application.displayController.passLog("Invalid choice. "+helperMsg);
				}
				return makeChoice(m);
		}else {
			Application.displayController.crlf();
			return choice;
		}
		
	}
	
	public Player createPlayer(int id) {
		Application.displayController.passLog("\tPlayer "+Integer.toString(id)+" : ");
		Application.displayController.displayYesNoQuestion("\tHuman-controlled ?");
		boolean human = answerYesNoQuestion();
		Player output;
		if(human) {
			HumanPlayer temp;
			Application.displayController.passLog("\tName :");
			String name = getStringInput();
			Application.displayController.crlf();
			temp = new HumanPlayer(name,id);
			temp.setInputObserver(Application.inputController);
			output=(Player) temp;
		}
		else {
			Application.displayController.crlf();
			output = new CPUPlayer(id);
		}
		output.setDisplayObserver(Application.displayController);
		return output;
	}
	
	public void getInput() {
		stdInputThread= new Thread( new InterruptibleStdInput());
		stdInputThread.start();
		try{latch.await();}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String getStringInput() {
		getInput();
		if (receivedString.equals("")) {
			Application.displayController.passLog("\tPlease make your choice.");
			return getStringInput();
		}
		else return receivedString;
	}
	public int getIntInput() {
		return Integer.parseInt(getStringInput());
	}
	public boolean answerYesNoQuestion() {
		char input = getStringInput().toLowerCase().charAt(0);
		if(input=='y') {
			return true;
		}
		else if (input=='n') {
			return false;
		}
		else {
			Application.displayController.passLog("\tInvalid answer. Please type in whether 'y' or 'n' :"); 
			return answerYesNoQuestion();
		}
	}
	

	public void receive(String str) {
		latch = new CountDownLatch(1);
		this.receivedString=str;
		interruptStdInput();
	}
	public void receive() {
		latch = new CountDownLatch(1);
		interruptStdInput();
	}
	
	public void wannaContinue() {
		Application.displayController.displayContinueMessage();
		getInput();
	}
	
	public void interruptStdInput() {
		if(stdInputThread != null) {
			if(stdInputThread.isAlive()) {
				stdInputThread.interrupt();
				stdInputThread = null;
			}
		}	
	}
	
	public void wake() {
		this.latch.countDown();
	}
}
