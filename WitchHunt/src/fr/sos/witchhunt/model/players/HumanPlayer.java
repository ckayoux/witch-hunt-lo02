package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.InputObservable;
import fr.sos.witchhunt.InputObserver;
import fr.sos.witchhunt.Menu;
import fr.sos.witchhunt.model.Identity;

public final class HumanPlayer extends Player implements InputObservable {
	
	private InputObserver inputObserver;
	
	//CONSTRUCTORS
	public HumanPlayer(String name, int id) {
		super(name,id);
	}
	public HumanPlayer(int id) {
		super(id);
		this.name="Player "+Integer.toString(id);
	}
	
	//GAME ACTIONS METHODS
	public void chooseIdentity() {
		super.chooseIdentity();
		Menu possibilities;
		possibilities = new Menu(name+" , make your choice (others, don't look !) :",
									"Villager",
									"Witch");
		displayObserver.displayPossibilities(possibilities);
		switch(inputObserver.makeChoice(possibilities)) {
			case 1:
				this.identity=Identity.VILLAGER;
				break;
			case 2:
				this.identity=Identity.WITCH;
				break;
		}
	}
	
	public void playTurn() {
		super.playTurn();
		Menu possibilities;
		if(this.canHunt()) {
			possibilities = new Menu("Choose one of these two offensive actions :",
										"Accuse another player",
										"Play the Hunt! effect of a Rumour card from your hand");
		}
		else {
			possibilities = new Menu("You have no more avaliable Hunt! effects.",
										"Accuse another player");
		}
		displayObserver.displayPossibilities(possibilities);
		switch(inputObserver.makeChoice(possibilities)) {
			case 1:
				displayObserver.passLog("\t!--This feature is not avaliable yet--!");//TEMPORARY
				break;
			case 2:
				displayObserver.passLog("\t!--This feature is not avaliable yet--!");//TEMPORARY
				break;
		}
	}
	
	//INPUT METHODS
	public void setInputObserver(InputObserver io) {
		inputObserver = io;
	}
	
}
