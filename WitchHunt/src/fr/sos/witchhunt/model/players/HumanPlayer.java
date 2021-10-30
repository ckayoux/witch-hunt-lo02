package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.InputObservable;
import fr.sos.witchhunt.PlayerInputObserver;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;

public final class HumanPlayer extends Player implements InputObservable {
	
	private PlayerInputObserver inputObserver;
	
	//CONSTRUCTORS
	public HumanPlayer(String name, int id) {
		super(name,id);
	}
	public HumanPlayer(int id) {
		super(id);
		this.name="Player "+Integer.toString(id);
	}
	
	//GAME ACTIONS METHODS
	@Override
	public void chooseIdentity() {
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
		this.identityCard.setChosenIdentity(this.identity);
	}
	
	@Override
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
	@Override
	public void setInputObserver(PlayerInputObserver io) {
		inputObserver = io;
	}
	
}
