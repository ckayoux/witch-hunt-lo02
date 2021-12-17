package fr.sos.witchhunt.controller;

import java.util.List;
import java.util.Scanner;

import java.util.concurrent.CountDownLatch;

import fr.sos.witchhunt.DisplayMediator;
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
	private StdView console;
	private Thread stdInputThread;
	private String receivedString;
	private int timesWrong=0;
	
	public int makeChoice(Menu m) {
		int choice;
		boolean correct;
		int n = m.getOptionsCount();
		choice = getIntInput();
		if(!(1 <= choice && choice <= n)) {
				correct = false;
				timesWrong++;
				String helperMsg = "Please enter an integer in the range 1.."+Integer.toString(n)+" :" ;
				console.logWrongMenuChoiceMessage(timesWrong,helperMsg,n);
				return makeChoice(m);
		}else {
			timesWrong=0;
			console.crlf();;
			return choice;
		}
		
	}
	
	public Player createPlayer(int id,List<String> chosenNames) {
		console.log("\tPlayer "+Integer.toString(id)+" : ");
		console.log("\tHuman-controlled ?");
		boolean human = answerYesNoQuestion();
		Player output;
		if(human) {
			HumanPlayer temp;
			console.log("\tName :");
			String name = "";
			boolean correct;
			do {
				name = getStringInput();
				if(chosenNames.contains(name)) {
					console.log("\tThis name is already taken.");
					correct=false;
				}
				else if (name.contains("CPU")) {
					console.log("\tThis name is reserved.");
					correct=false;
				}
				else {
					correct=true;
				}
				if(!correct) console.log("\tPlease choose another one :");
			}
			while(!correct);
			chosenNames.add(name);
			console.crlf();
			temp = new HumanPlayer(name,id);
			temp.setInputMediator(this);
			output=(Player) temp;
		}
		else {
			Tabletop.getInstance().incrementCPUPlayersNumber();
			console.crlf();
			output = new CPUPlayer(id,Tabletop.getInstance().getCPUPlayersNumber());
		}
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
			console.log("\tPlease make your choice.");
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
			console.log("\tInvalid answer. Please type in whether 'y' or 'n' :"); 
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
		console.logContinueMessage();
		getInput();
		console.crlf();
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

	public void setConsole(StdView console) {
		this.console=console;
	}
}
