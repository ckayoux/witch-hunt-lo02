package fr.sos.witchhunt.model.players;

import java.util.List;

import fr.sos.witchhunt.PlayerInputObservable;
import fr.sos.witchhunt.PlayerInputObserver;
import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;

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
		requestDisplayPossibilities(possibilities);
		switch(makeChoice(possibilities)) {
			case 1:
				this.identity=Identity.VILLAGER;
				break;
			case 2:
				this.identity=Identity.WITCH;
				break;
		}
		this.identityCard.setChosenIdentity(this.identity);
	}
	
	private <T> T choose(List<T> from,String prompt){
		/*this method is made for the human players to select an element from a list.
		 * it will display the $prompt, then ask to select an option among the toString of each element of $from
		 */
		String [] options = new String [from.size()];
		for(int i=0; i<from.size(); i++) {
			options[i] = from.get(i).toString();
		}
		Menu m = new Menu(prompt,options);
		requestDisplayPossibilities(m);
		return from.get(makeChoice(m)-1);
	}
	protected Player choosePlayerToAccuse() {
		return choose(getAccusablePlayers(),"Select the player you want to accuse :");
	}
	public Player chooseTarget(List<Player> eligiblePlayersList) {
		return choose(eligiblePlayersList,"Select the player you want to target :");
	}
	public Player chooseNextPlayer() {
		return choose(Tabletop.getInstance().getActivePlayersList(),"Select the next player to play :");
	}
	public RumourCard selectCardToDiscard() {
		if(this.hasUnrevealedRumourCards()) {
			return choose(this.getUnrevealedSubhand().getCards(),"Select the unrevealed card that you want to discard :");
		}
		else {
			return choose(this.hand.getCards(),"Select the card that you want to discard :");
		}
	}
	
	
	/*@Override
	protected Player choosePlayerToAccuse() {
		List<Player> accusablePlayersList = getAccusablePlayers();
		String [] accusablePlayersNames = new String [accusablePlayersList.size()];
		for (int i=0; i<accusablePlayersList.size(); i++) {
			accusablePlayersNames[i] = accusablePlayersList.get(i).getName(); 
		}
		Menu m = new Menu("Select the player you want to accuse :", accusablePlayersNames);
		displayObserver.displayPossibilities(m);
		return accusablePlayersList.get(inputObserver.makeChoice(m)-1);
	}
	
	@Override
	public Player chooseTarget(List<Player> eligiblePlayersList) {
		eligiblePlayersList.remove(this); //so you can't target yourself
		String [] eligiblePlayersNames = new String [eligiblePlayersList.size()];
		for (int i=0; i<eligiblePlayersList.size(); i++) {
			eligiblePlayersNames[i] = eligiblePlayersList.get(i).getName(); 
		}
		Menu m = new Menu("Select the player you want to target :", eligiblePlayersNames);
		displayObserver.displayPossibilities(m);
		return eligiblePlayersList.get(inputObserver.makeChoice(m)-1);
	}
	
	@Override
	public Player chooseNextPlayer() {
		List<Player> activePlayersList = Tabletop.getInstance().getActivePlayersList();
		activePlayersList.remove(this);
		String [] activePlayersNames = new String [activePlayersList.size()];
		for (int i=0; i<activePlayersList.size(); i++) {
			activePlayersNames[i] = activePlayersList.get(i).getName(); 
		}
		Menu m = new Menu("Select the next player to play :", activePlayersNames);
		displayObserver.displayPossibilities(m);
		return activePlayersList.get(inputObserver.makeChoice(m)-1);
	}*/
	
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
		requestDisplayPossibilities(possibilities);
		switch(makeChoice(possibilities)) {
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
	@Override
	public int makeChoice(Menu m) {
		return inputObserver.makeChoice(m);
	}
	
	
	@Override
	public DefenseAction chooseDefenseAction() {
		Menu m = new Menu("Select your answer to this utterly slanderous and unfounded accusation",
							"Play the Witch? effect of a Rumour card from your hand","Reveal your identity");
		requestDisplayPossibilities(m);
		switch(makeChoice(m)) {
			case 1:
				return DefenseAction.WITCH;
			case 2:
				return DefenseAction.REVEAL;
		}
		return null;
	}
	
	@Override
	public RumourCard selectWitchCard(RumourCardsPile rcp) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
