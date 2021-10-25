package fr.sos.witchhunt.controller;

import java.util.Scanner;

import java.util.concurrent.CountDownLatch;

import fr.sos.witchhunt.InputObserver;
import fr.sos.witchhunt.model.players.CPUPlayer;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.Menu;
import fr.sos.witchhunt.view.std.InterruptibleStdInput;
import fr.sos.witchhunt.view.std.StdView;

public final class InputController implements InputObserver {

	//ATTRIBUTES
	private CountDownLatch latch = new CountDownLatch(1) ;
	private Thread stdInputThread;
	private String receivedString;
	
	public int makeChoice(Menu m) {
		int choice;
		boolean correct;
		int n = m.getOptionsNumber();
		choice = getIntInput();
		if(!(1 <= choice && choice <= n)) {
				correct = false;
				Application.displayController.passLog("Invalid choice. Please enter an integer in the range 1.."+Integer.toString(n)+" :");
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
	
	public String getStringInput() {
		stdInputThread= new Thread( new InterruptibleStdInput());
		stdInputThread.start();
		try{latch.await();}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		return receivedString;
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
			Application.displayController.passLog("Invalid answer. Please type in whether 'y' or 'n' :"); 
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
		getStringInput();
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
