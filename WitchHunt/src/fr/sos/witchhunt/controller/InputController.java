package fr.sos.witchhunt.controller;

import java.util.List;
import java.util.Scanner;

import java.util.concurrent.CountDownLatch;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.players.CPUPlayer;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.std.InterruptibleStdInput;
import fr.sos.witchhunt.view.std.StdView;

public final class InputController implements InputMediator {

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
					Application.displayController.passLog("\tAre you doing it on purpose ?");
					if(n!=1)Application.displayController.passLog("helperMsg");
				}
				else if (timesWrong==3) {
					Application.displayController.passLog("\tCome on ! I believe in you ! You can do it !");
					if(n!=1)Application.displayController.passLog("helperMsg");
				}
				else {
					if(n==1) {
						Application.displayController.passLog("\tYou can only choose option #1 here !");
					}
					else Application.displayController.passLog("Invalid choice. "+helperMsg);
				}
				return makeChoice(m);
		}else {
			Application.displayController.crlf();
			return choice;
		}
		
	}
	
	public Player createPlayer(int id,List<String> chosenNames) {
		Application.displayController.passLog("\tPlayer "+Integer.toString(id)+" : ");
		Application.displayController.displayYesNoQuestion("\tHuman-controlled ?");
		boolean human = answerYesNoQuestion();
		Player output;
		if(human) {
			HumanPlayer temp;
			Application.displayController.passLog("\tName :");
			String name = "";
			boolean correct;
			do {
				name = getStringInput();
				if(chosenNames.contains(name)) {
					Application.displayController.passLog("\tThis name is already taken.");
					correct=false;
				}
				else if (name.contains("CPU")) {
					Application.displayController.passLog("\tThis name is reserved.");
					correct=false;
				}
				else {
					correct=true;
				}
				if(!correct) Application.displayController.passLog("\tPlease choose another one :");
			}
			while(!correct);
			chosenNames.add(name);
			Application.displayController.crlf();
			temp = new HumanPlayer(name,id);
			temp.setInputMediator(Application.inputController);
			output=(Player) temp;
		}
		else {
			Game.incrementCPUPlayersNumber();
			Application.displayController.crlf();
			output = new CPUPlayer(id,Game.getCPUPlayersNumber());
		}
		output.setDisplayMediator(Application.displayController);
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
		try {
			int got = Integer.parseInt(getStringInput());
			return got;
		}
		catch (final NumberFormatException e) {
			return -1;
		}
			
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
	
	public void sleepStdInput() {
		if(this.stdInputThread.isAlive()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public void wake() {
		this.latch.countDown();
	}

}
