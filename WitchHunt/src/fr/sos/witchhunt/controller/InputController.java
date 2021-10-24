package fr.sos.witchhunt.controller;

import java.util.Scanner;

import java.util.concurrent.CountDownLatch;

import fr.sos.witchhunt.InputObserver;
import fr.sos.witchhunt.view.Menu;
import fr.sos.witchhunt.view.std.InterruptibleStdInput;
import fr.sos.witchhunt.view.std.StdView;

public final class InputController implements InputObserver {

	//ATTRIBUTES
	private CountDownLatch latch = new CountDownLatch(1) ;
	private StdView console;
	private Thread stdInputThread;
	private String receivedString;
	
	//CONSTRUCTORS
	public InputController() {
		this.console=Application.console;
	}
	
	public int makeChoice(Menu m) {
		int choice;
		boolean correct;
		int n = m.getOptionsNumber();
		choice = getIntInput();
		if(!(1 <= choice && choice <= n)) {
				correct = false;
				Application.console.log("Invalid choice. Please enter an integer in the range 1.."+Integer.toString(n)+" :");
				return makeChoice(m);
		}else {
			Application.console.crlf();
			return choice;
		}
		
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
			Application.console.log("Invalid answer. Please type in whether 'y' or 'n' :"); 
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
