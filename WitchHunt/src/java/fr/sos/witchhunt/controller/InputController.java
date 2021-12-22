package fr.sos.witchhunt.controller;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.players.CPUPlayer;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.gui.GUIView;
import fr.sos.witchhunt.view.std.InterruptibleStdInput;
import fr.sos.witchhunt.view.std.StdView;

public final class InputController implements InputMediator {

	//ATTRIBUTES
	private CountDownLatch latch = new CountDownLatch(1) ;
	private StdView console;
	private GUIView gui;
	private Thread stdInputThread;
	private String receivedString;
	private Menu currentMenu=null;
	private int timesWrong=0;
	
	@Override
	public int makeChoice(Menu m) {
		int choice;
		boolean correct;
		currentMenu=m;
		gui.makeChoice(m);
		int n = m.getOptionsCount();
		choice = getIntInput();
		if(!(1 <= choice && choice <= n)) {
			correct = false;
			timesWrong++;
			String helperMsg = "Please enter an integer in the range 1.."+Integer.toString(n)+" :" ;
			console.logWrongMenuChoiceMessage(timesWrong,helperMsg,n);
			return makeChoice(m);
		}else {
			this.gui.choiceHasBeenMade(choice);
			timesWrong=0;
			console.crlf();;
			currentMenu=null;
			return choice;
		}
		
	}
	
	@Override
	public Player createPlayer(int id,List<String> chosenNames) {
		console.log("\tPlayer "+Integer.toString(id)+" : ");
		console.yesNoQuestion("\tHuman-controlled ?");
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
			output=temp;
		}
		else {
			Tabletop.getInstance().incrementCPUPlayersNumber();
			console.crlf();
			output = new CPUPlayer(id,Tabletop.getInstance().getCPUPlayersNumber());
		}
		return output;
	}
	
	public void getInput() {
		stdInputThread= new Thread( new InterruptibleStdInput(this));
		stdInputThread.start();
		try{latch.await();}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String getStringInput() {
		getInput();
		if (receivedString==null||receivedString.equals("")) {
			console.logInputWasExpectedMessage();
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
	@Override
	public boolean answerYesNoQuestion() {
		char input = getStringInput().toLowerCase().charAt(0);
		if(input=='y') {
			return true;
		}
		else if (input=='n') {
			return false;
		}
		else {
			console.logInvalidYesNoQuestionAnswerMessage(); 
			return answerYesNoQuestion();
		}
	}
	
	@Override
	public void receive(String str) {
		this.wake();
		latch = new CountDownLatch(1);
		this.receivedString=str;
	}
	@Override
	public void receive(int i) {
		this.receive(Integer.toString(i));
	}
	@Override
	public void receive() {
		this.wake();
		latch = new CountDownLatch(1);
	}
	
	@Override
	public void wannaContinue() {
		console.logContinueMessage();
		gui.wannaContinue(this);
		getInput();
		this.gui.choiceHasBeenMade(1);
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
		this.interruptStdInput();
	}

	public void setConsole(StdView console) {
		this.console=console;
	}
	public void setGui(GUIView gui) {
		this.gui= gui;
	}
	
	public Menu getCurrentMenu() {
		return currentMenu;
	}
}
