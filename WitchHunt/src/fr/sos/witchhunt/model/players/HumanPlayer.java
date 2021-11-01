package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.PlayerInputObservable;
import fr.sos.witchhunt.PlayerInputObserver;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;

public final class HumanPlayer extends Player implements PlayerInputObservable {
	
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
	protected void accuse() {
		displayObserver.passLog("\t!--This feature is not avaliable yet--!");//TEMPORARY
		super.accuse();
	}
	
	@Override
	protected Player choosePlayerToAccuse() {
		// TODO choisir dans un menu le jouer à accuser parmis Tabletop.getInstance().getAccusablePlayers();
		return null;
	}
	
	
	@Override
	public Identity defend() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void hunt() {
		displayObserver.passLog("\t!--This feature is not avaliable yet--!");//TEMPORARY
	}
	
	@Override
	public TurnAction chooseTurnAction() {
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
				return TurnAction.ACCUSE;
			case 2:
				return TurnAction.HUNT;
		}
		return null;
	}

	//INPUT METHODS
	@Override
	public void setInputObserver(PlayerInputObserver io) {
		inputObserver = io;
	}

}
