package fr.sos.witchhunt.view.std;

import java.util.List;
import java.util.concurrent.Callable;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;

public class StdPlayerCreator implements Runnable ,Callable<Player>,InputSource{
	
	private InputMediator slaveInputMediator;
	private InputMediator masterInputMediator;
	private StdView console;
	private int playerId;
	private List<String> takenNames;
	private boolean optionnal = false;
	
	public StdPlayerCreator(InputMediator master,InputMediator slave,StdView console,int playerId,List<String> takenNames,boolean optionnal) {
		this.masterInputMediator=master;
		this.slaveInputMediator = slave;
		this.optionnal=optionnal;
		this.console=console;
		this.playerId=playerId;
		this.takenNames=takenNames;
	}
	
	@Override
	public Player call() throws InterruptedException {
		boolean doCreate = true;
		if(optionnal) {
			try {
				doCreate = slaveInputMediator.answerYesNoQuestion();
			}
			catch(InterruptedException e) {
				throw e;
			}
		}
		boolean human;
		if(doCreate) {
			console.log("\tPlayer "+Integer.toString(playerId)+" :");
			console.yesNoQuestion("\tHuman-controlled ?");
			try {
				human = slaveInputMediator.answerYesNoQuestion();
			}
			catch(InterruptedException e) {
				throw e;
			}
			Player output;
			String name="";
			if(human) {
				HumanPlayer temp;
				console.log("\tName :");
				boolean correct;
				do {
					name = slaveInputMediator.getStringInput();
					if(takenNames.contains(name)) {
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
			}
			return slaveInputMediator.createPlayer(playerId,name,human);
		}
		else {
			return null;
		}
	  }
	

	@Override
	public void run() {
		Player createdFromStdInput=null;
		try {
			createdFromStdInput=this.call();
			post(createdFromStdInput);
		}
		catch(InterruptedException e) {
		}
		finally {
			this.slaveInputMediator.interruptStdInput();
		}
	}

	@Override
	public void post(String str) {
	}

	@Override
	public void post() {
	}

	@Override
	public void post(Player p) {
		masterInputMediator.receive(p);
	}
	
}
