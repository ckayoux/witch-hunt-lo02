package fr.sos.witchhunt.model.players;

import java.util.List;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;

public final class HumanPlayer extends Player implements PlayerInputObservable {
	
	private InputMediator inputMediator;
	
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
	@Override
	protected Player choosePlayerToAccuse() {
		return choose(getAccusablePlayers(),"Select the player you want to accuse :");
	}
	@Override
	public Player chooseTarget(List<Player> eligiblePlayersList) {
		return choose(eligiblePlayersList,"Select the player you want to target :");
	}
	
	@Override
	public Player chooseNextPlayer() {
		return choose(Tabletop.getInstance().getActivePlayersList().stream().filter(p->p!=this).toList(),"\n\tSelect the next player to play :");
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
	public TurnAction chooseTurnAction() {
		Menu possibilities;
		if(this.canHunt()) {
			possibilities = new Menu("Choose one of these actions :",
										"Accuse another player",
										"Play the Hunt! effect of a Rumour card from your hand",
										"Show your cards",
										"Show players ranking");
			requestDisplayPossibilities(possibilities);
			switch(makeChoice(possibilities)) {
				case 1:
					return TurnAction.ACCUSE;
				case 2:
					return TurnAction.HUNT;
				case 3:
					this.showHand();
					return this.chooseTurnAction();
				case 4:
					this.showRanking();
					return this.chooseTurnAction();
			}
		}
		else {
			possibilities = new Menu("You have no more avaliable Hunt! effects.",
										"Accuse another player",
										"Show your cards",
										"Show players ranking");
			requestDisplayPossibilities(possibilities);
			switch(makeChoice(possibilities)) {
				case 1:
					return TurnAction.ACCUSE;
				case 2:
					this.showHand();
					return this.chooseTurnAction();
				case 3:
					this.showRanking();
					return this.chooseTurnAction();
			}
		}
		
		return null;
	}

	//INPUT METHODS
	@Override
	public void setInputMediator(InputMediator im) {
		inputMediator = im;
	}
	@Override
	public int makeChoice(Menu m) {
		return inputMediator.makeChoice(m);
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

	public void showHand() {
		displayMediator.showCards(this);
	}
	
	private void showRanking() {
		displayMediator.displayRanking(this);
	}
	@Override
	public RumourCard selectCardToDiscard(RumourCardsPile in) {
		if(this.hasRumourCards()) {
			if(this.hasUnrevealedRumourCards()) {
				return chooseUnrevealedCard(in.getUnrevealedSubpile(),true);
			}
			else {
				return chooseAnyCard(in,true);
			}
		}
		else {
			requestNoCardsScreen();
			return null;
		}
		
	}
	
	private RumourCard chooseCard(RumourCardsPile from,boolean forcedReveal){
		//this method must not be used alone.
		String [] options = new String [from.getCardsCount()];
		for(int i=0; i<from.getCardsCount(); i++) {
			options[i] = from.getCards().get(i).getName();
		}
		Menu m = new Menu("",options);
		return from.getCards().get(makeChoice(m)-1);
	}
	
	@Override
	public RumourCard chooseAnyCard(RumourCardsPile from,boolean forcedReveal){
		if(targetPileContainsCards(from)) {
			requestSelectCardScreen();
			displayMediator.displayCards(from,forcedReveal);
			return chooseCard(from,forcedReveal);
		}
		else return null;
	}
	public RumourCard chooseUnrevealedCard(RumourCardsPile from,boolean forcedReveal){
		if(targetPileContainsCards(from.getUnrevealedSubpile())){
			//the player must necessarily choose an unrevealed card
			requestSelectUnrevealedCardScreen();
			displayMediator.displayCards(from.getUnrevealedSubpile(),forcedReveal);
			return chooseCard(from.getUnrevealedSubpile(),forcedReveal);
		}
		else return null;
	}
	@Override
	public RumourCard chooseRevealedCard(RumourCardsPile from){
		if(targetPileContainsCards(from.getRevealedSubpile())){
			//the player must necessarily choose a revealed card
			requestSelectRevealedCardScreen();
			displayMediator.displayCards(from.getRevealedSubpile(),false);
			return chooseCard(from.getRevealedSubpile(),false);
		}
		else return null;
	}
	
	@Override
	public RumourCard selectWitchCard() {
		requestSelectCardScreen();
		displayMediator.displayWitchEffects(this.hand.getPlayableWitchSubpile());
		return chooseCard(this.hand.getPlayableWitchSubpile(),true);
	}
	
	@Override
	public RumourCard selectHuntCard() {
		requestSelectCardScreen();
		displayMediator.displayHuntEffects(this.hand.getPlayableHuntSubpile());
		return chooseCard(this.hand.getPlayableHuntSubpile(),true);
	}
	
	@Override
	public void requestLookAtPlayersIdentityScreen(Player target) {
		super.requestLookAtPlayersIdentityScreen(target);
		displayMediator.secretlyDisplayIdentity(target);
	}
	@Override
	public DefenseAction revealOrDiscard() {
		Menu ultimatum;
		DefenseAction choice=DefenseAction.DISCARD;
		if(this.hasRumourCards()&&!this.isRevealed()) {
			ultimatum = new Menu(this.getName()+", choose an action :","Discard a card","Reveal your identity");
			requestDisplayPossibilities(ultimatum);
			if(makeChoice(ultimatum)==2) choice=DefenseAction.REVEAL;
		}
		else if(!this.hasRumourCards()&&!this.isRevealed()) {
			ultimatum = new Menu(this.getName()+", you have no cards to discard. You have only one choice :","Reveal your identity");
			requestDisplayPossibilities(ultimatum);
			makeChoice(ultimatum);
		}
		else if(this.hasRumourCards()) {
			ultimatum = new Menu(this.getName()+", you are already revealed. You have only one choice :","Discard a card");
			requestDisplayPossibilities(ultimatum);
			makeChoice(ultimatum);
		}
		//cannot be chosen by ducking stool if is revealed and has no rumour cards
		return choice;
	}
}
