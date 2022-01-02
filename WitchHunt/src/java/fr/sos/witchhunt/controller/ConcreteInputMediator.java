package fr.sos.witchhunt.controller;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.CPUPlayer;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.gui.GUIView;
import fr.sos.witchhunt.view.std.InterruptibleStdInput;
import fr.sos.witchhunt.view.std.StdPlayerCreator;
import fr.sos.witchhunt.view.std.StdView;

public final class ConcreteInputMediator implements InputMediator {

	//ATTRIBUTES
	private CountDownLatch latch = new CountDownLatch(1) ;
	private StdView console;
	private GUIView gui;
	private Thread stdInputThread;
	private Thread activePlayerCreatorThread;
	private String receivedString;
	private Player receivedPlayer;
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
			this.gui.choiceHasBeenMade(m.getNthOption(choice));
			console.log(choice + " : "+m.getNthOption(choice).toString()); //logging choice correspondance in console
			timesWrong=0;
			console.crlf();;
			currentMenu=null;
			return choice;
		}
		
	}
	
	@Override
	public Player createPlayer(int id,List<String> chosenNames,boolean optionnal) {
		ConcreteInputMediator ic = new ConcreteInputMediator();
		ic.setConsole(console);
		ic.setGui(gui);
		StdPlayerCreator spc = new StdPlayerCreator(this,ic,console,id,chosenNames,optionnal);
		activePlayerCreatorThread= new Thread(spc);
		activePlayerCreatorThread.start();
		Player output=getPlayerInput();
		return output;
	}
	
	@Override
	public Player createPlayer(int id, String name, boolean isHuman) {
		Player output;
		if(isHuman) {
			HumanPlayer houtput =new HumanPlayer(name,id);
			houtput.setInputMediator(this);
			output=houtput;
		}
		else  {
			Tabletop.getInstance().incrementCPUPlayersNumber();
			output=new CPUPlayer(id,Tabletop.getInstance().getCPUPlayersNumber());
		}
		return output;
	}
	
	public void getInput() throws InterruptedException {
		stdInputThread= new Thread( new InterruptibleStdInput(this,console));
		stdInputThread.start();
		latch.await();
	}
	
	@Override
	public String getStringInput() throws InterruptedException {
		getInput();
		if (receivedString==null||receivedString.equals("")) {
			console.logInputWasExpectedMessage();
			return getStringInput();
		}
		else return receivedString;
	}
	
	public Player getPlayerInput() {
		try{latch.await();}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		return receivedPlayer;
	}
	public int getIntInput() {
		try {
			int got = Integer.parseInt(getStringInput());
			return got;
		}
		catch (final NumberFormatException e) {
			return -1;
		}
		catch (InterruptedException e) {
			return -1;
		}
			
	}
	@Override
	public boolean answerYesNoQuestion() throws InterruptedException {
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
		this.receivedString=str;
		latch = new CountDownLatch(1);
	}
	@Override
	public void receive(Player p) {
		this.latch.countDown();
		if(this.activePlayerCreatorThread!=null && this.activePlayerCreatorThread.isAlive()) {
			this.activePlayerCreatorThread.interrupt();
			this.activePlayerCreatorThread=null;
		}
		this.receivedPlayer=p;
		latch = new CountDownLatch(1);
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
		try {
			getInput();
		} catch (InterruptedException e) {

		}
		this.gui.choiceHasBeenMade(1);
		console.crlf();
	}
	
	@Override
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
