package fr.sos.witchhunt.model.players;

import java.util.List;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;

/**
 * <p><b>This class represents a human-controlled player.</b></p>
 * <p>It overrides all of {@link Player}'s abstract methods, so that all choices a HumanPlayer can be led to make are based on user-input.</p>
 * <p>All required input methods are specified in the {@link PlayerInputObservable} interface.</p>
 * @see Player
 * @see PlayerInputObservable
 */
public final class HumanPlayer extends Player implements PlayerInputObservable {
	
	/**
	 * An instance of a class implementing {@link fr.sos.witchhunt.InputMediator InputMediator} is responsible for requesting the view to collect user-input.
	 */
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
	/**
	 * <b>Chooses and sets the player's {@link #identity} and that of their {@link #identityCard}.</b>
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see fr.sos.witchhunt.model.IdentityCard Identity card
	 * @see fr.sos.witchhunt.model.Menu Menu
	 * @see PlayerInputObservable#makeChoice(Menu)
	 */
	@Override
	public void chooseIdentity() {
		Menu possibilities;
		possibilities = new Menu(name+" , choose your identity :",
									Identity.VILLAGER,
									Identity.WITCH);
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
	
	/**
	 * <p><b>Chooses an object within a list of plural objects of a given and unique type.</b></p>
	 * <p>This method is made for the human players to select an element from a list of elements of a given type (using genericity).</p>
	 * @param {{@literal <T>} The type of the objects in the list. Could be {@link fr.sos.witchhunt.model.cards.RumourCard}, {@link Player} ...
	 * @param from The list of objects of type {@literal <T>}
	 * @param prompt The message to be displayed with the input request
	 * @return An unique object of type {@literal <T>}, belonging to the list given in parameters
	 * @see fr.sos.witchhunt.model.Menu Menu
	 * @see PlayerInputObservable#makeChoice(Menu)
	 * @see #chooseRevealedCard(RumourCardsPile)
	 * @see #chooseUnrevealedCard(RumourCardsPile, boolean)
	 * @see #chooseAnyCard(RumourCardsPile, boolean)
	 * @see #selectCardToDiscard(RumourCardsPile)
	 */
	private <T> T choose(List<T> from,String prompt){
		/*this method is made for the human players to select an element from a list.
		 * it will display the $prompt, then ask to select an option among the toString of each element of $from
		 */
		Object [] options = new Object [from.size()];
		for(int i=0; i<from.size(); i++) {
			options[i] = from.get(i);
		}
		Menu m = new Menu(prompt,options);
		requestDisplayPossibilities(m);
		return (T) m.getNthOption(makeChoice(m));
	}
	
	/**
	 * <b>Selects a player to {@link #accuse(Player) accuse} based on user-input.</b>
	 * @return The chosen Player, belonging to the {@link fr.sos.witchhunt.controller.Tabletop#getAccusablePlayersList() list of accusable players}.
	 * @see #choose(List, String)
	 * @see #accuse(Player)
	 */
	@Override
	protected Player choosePlayerToAccuse() {
		return choose(getAccusablePlayers(),"Select the player you want to accuse :");
	}
	
	/**
	 * <b>Selects a target (a player) within a list of eligible players, based on user-input.</b>
	 * @param The chosen target, belonging to the list of eligible players.
	 * @see #choose(List, String)
	 * @see #chooseHuntedTarget(List)
	 */
	@Override
	public Player chooseTarget(List<Player> eligiblePlayersList) {
		return choose(eligiblePlayersList,"Select the player you want to target :");
	}
	
	/**
	 * <p><b>Elects the player who is going to take the next turn, based on user-input.</b></p>
	 * <p>Called by certain {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards}' {@link fr.sos.witchhunt.model.cards.Effect effects}, 
	 * like {@link fr.sos.witchhunt.model.cards.DuckingStool the Ducking Stool's} Witch? effect for example.</p>
	 * @see #choose(List, String)
	 * @see #takeNextTurn()
	 * @see fr.sos.witchhunt.controller.Round#setNextPlayer(Player)
	 */
	@Override
	public Player chooseNextPlayer() {
		return choose(Tabletop.getInstance().getActivePlayersList().stream().filter(p->p!=this).toList(),"\n\tSelect the next player to play :");
	}
	
	/**
	 * <p><b>Chooses an {@link TurnAction action} to perform during the player's turn, based on user-input.</b></p>
	 * <p>Human players, like CPUPlayers, can {@link #accuse(Player) accuse} and {@link #hunt() hunt}, but they also can
	 * choose between two other special actions, {@link #showHand() show their cards} and {@link #showRanking() show players ranking}.
	 * The two latter actions do not put an end to the turn : once performed, the player is asked to choose an action again.</p>
	 * @return The selected action : {@link TurnAction can be <i>{@link #accuse(Player) ACCUSE}</i>,<i>{@link #hunt() HUNT}</i>}, {@link #showHand() show your cards} or {@link #showRanking() show players ranking}.
	 * @see #choose(List, String)
	 * @see TurnAction
	 * @see #accuse(Player)
	 * @see #hunt()
	 * @see #showHand()
	 * @see #showRanking()
	 * @see #playTurn()
	 */
	@Override
	public TurnAction chooseTurnAction() {
		Menu possibilities;
		if(this.canHunt()) {
			possibilities = new Menu("Choose one of these actions :",
										TurnAction.ACCUSE,
										TurnAction.HUNT,
										"/c/Show your cards",
										"/c/Show players ranking");
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
			requestDisplayNoAvailableHuntEffectsScreen();
			possibilities = new Menu("Choose one of these actions :",
										TurnAction.ACCUSE,
										"/c/Show your cards",
										"/c/Show players ranking");
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
	/**
	 * @see #inputMediator
	 */
	@Override
	public void setInputMediator(InputMediator im) {
		inputMediator = im;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int makeChoice(Menu m) {
		return inputMediator.makeChoice(m);
	}
	
	/**
	 * <p><b>Chooses an action to respond to an {@link #accuse(Player) accusation} or a {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}, based on user-input.</b></p>
	 * <p>When a player is accused, they can choose between {@link #witch() playing a Witch? effect}, {@link #canWitch() if they can}, and {@link #revealIdentity() revealing their identity}.</p>
	 * <p>When a player is targetted by certain {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} like that of {@link fr.sos.witchhunt.model.cards.DuckingStool}, 
	 * they can choose between {@link #revealOrDiscard() revealing their identity and discarding one of their Rumour cards}.</p>
	 * @return A {@link DefenseAction response among <i>ACCUSE</i>, <i>REVEAL</i> and <i>DISCARD</i>}.
	 * @see DefenseAction
	 * @see #revealIdentity()
	 * @see #witch()
	 * @see #discard(RumourCard)
	 * @see #revealOrDiscard()
	 * @see HumanPlayer#chooseDefenseAction()
	 * @see CPUPlayer#chooseDefenseAction()
	 */
	@Override
	public DefenseAction chooseDefenseAction() {
		Menu m = new Menu(name+", what can you say in your defence ?",
				DefenseAction.WITCH,DefenseAction.REVEAL);
		requestDisplayPossibilities(m);
		switch(makeChoice(m)) {
			case 1:
				return DefenseAction.WITCH;
			case 2:
				return DefenseAction.REVEAL;
		}
		return null;
	}

	/**
	 * <p><b>Requests the {@link fr.sos.witchhunt.DisplayMediator Display Mediator} to show all of the cards in the player's hand.</b></p>
	 * <p>Shows the unrevealed cards as if they were revealed.</p>
	 * @see fr.sos.witchhunt.DisplayMediator#showCards(Player) DisplayMediator::showCards(Player)
	 */
	public void showHand() {
		displayMediator.showCards(this);
	}
	
	/**
	 * <p><b>Requests the {@link fr.sos.witchhunt.DisplayMediator Display Mediator} to show players ranking.</b></p>
	 * <p>Specific information about this player's score and position in the ranking can be displayed.</p>
	 * @see fr.sos.witchhunt.DisplayMediator#displayRanking(Player) DisplayMediator::displayRanking(Player)
	 * @see fr.sos.witchhunt.controller.ScoreCounter#getRanking() ScoreCounter::getRanking() 
	 */
	private void showRanking() {
		displayMediator.displayRanking(this);
	}
	
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} to {@link #discard(RumourCard) discard from the given {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}, based on user-input</b>
	 * @param in A {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}.
	 * @return The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} that was chosen to be {@link #discard(RumourCard) discarded}.
	 * @see #discard(RumourCard)
	 * @see fr.sos.witchhunt.model.cards.RumourCard Rumour card
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile
	 * @see Player#selectCardToDiscard(RumourCardsPile)
	 */
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
	
	/**
	 * <b>Chooses a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} among a given {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour card}, based on user-input.</b>
	 * <p>Collects user-input but does not display anything except input error messages.</p>
	 * @param from The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour card} within which a card must be chosen
	 * @return The chosen {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}
	 */
	private RumourCard chooseCard(RumourCardsPile from){
		Menu m = new Menu ("",from.getCards().toArray());
		return (RumourCard) m.getNthOption(makeChoice(m));
	}



	/**
	 * <b>Chooses a card (revealed or not) within a pile of cards, based on user-input.</b>
	 * @see Player#chooseAnyCard(RumourCardsPile, boolean) 
	 */
	@Override
	public RumourCard chooseAnyCard(RumourCardsPile from,boolean forcedReveal){
		if(targetPileContainsCards(from)) {
			requestSelectCardScreen(from,forcedReveal);
			return chooseCard(from);
		}
		else return null;
	}
	/**
	 * <b>Chooses an unrevealed card within a pile of cards, based on user-input.</b>
	 */
	public RumourCard chooseUnrevealedCard(RumourCardsPile from,boolean forcedReveal){
		if(targetPileContainsCards(from.getUnrevealedSubpile())){
			//the player must necessarily choose an unrevealed card
			requestSelectUnrevealedCardScreen(from,forcedReveal);
			//displayMediator.displayCards(from.getUnrevealedSubpile(),forcedReveal);
			return chooseCard(from.getUnrevealedSubpile());
		}
		else return null;
	}
	/**
	 * <b>Chooses a revealed card within a pile of cards, based on user-input.</b>
	 * @see Player#chooseRevealedCard(RumourCardsPile) 
	 */
	@Override
	public RumourCard chooseRevealedCard(RumourCardsPile from){
		if(targetPileContainsCards(from.getRevealedSubpile())){
			//the player must necessarily choose a revealed card
			requestSelectRevealedCardScreen(from,true);
			//displayMediator.displayCards(from.getRevealedSubpile(),false);
			return chooseCard(from.getRevealedSubpile());
		}
		else return null;
	}
	
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} valued for its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect}, based on user-input.</b>
	 * @see Player#selectWitchCard()
	 */
	@Override
	public RumourCard selectWitchCard() {
		requestSelectWitchCardScreen(this.hand.getPlayableWitchSubpile());
		return chooseCard(this.hand.getPlayableWitchSubpile());
	}
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} valued for its {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}, based on user-input.</b>
	 * @see Player#selectHuntCard()
	 */
	@Override
	public RumourCard selectHuntCard() {
		//requestSelectCardScreen();
		requestSelectHuntCardScreen(this.hand.getPlayableHuntSubpile());
		return chooseCard(this.hand.getPlayableHuntSubpile());
	}
	
	/**
	 * {@inheritDoc}
	 * <p>When a human player looks at their target's identity, they also request it to be displayed on the screen.</p>
	 */
	@Override
	public void requestLookAtPlayersIdentityScreen(Player target) {
		super.requestLookAtPlayersIdentityScreen(target);
		displayMediator.secretlyDisplayIdentity(target);
	}
	/**
	 * <p><b>Chooses an {@link DefenseAction action} between {@link #revealIdentity() revealing your identity} and {@link #discard(RumourCard) discarding a Rumour card from your hand}.</b></p>
	 * <p>Called when targetted by the {@link fr.sos.witchhunt.model.cards.DuckingStool Ducking Stool} card's Hunt! effect.</p>
	 * <p>For human controlled players, specific messages are requested to be displayed if one of these options is not available.</p>
	 * @see Player#revealOrDiscard()
	 * @return Either {@link DefenseAction#REVEAL} or {@link DefenseAction#DISCARD}
	 */
	@Override
	public DefenseAction revealOrDiscard() {
		Menu ultimatum;
		DefenseAction choice=DefenseAction.DISCARD;
		if(this.hasRumourCards()&&!this.isRevealed()) {
			ultimatum = new Menu(this.getName()+", choose an action :",DefenseAction.DISCARD,DefenseAction.REVEAL);
			requestDisplayPossibilities(ultimatum);
			choice=(DefenseAction) ultimatum.getNthOption(makeChoice(ultimatum));
		}
		else if(!this.hasRumourCards()&&!this.isRevealed()) {
			ultimatum = new Menu(this.getName()+", you have no cards to discard. You have only one choice :",DefenseAction.REVEAL);
			requestDisplayPossibilities(ultimatum);
			makeChoice(ultimatum);
			choice=DefenseAction.REVEAL;
		}
		else if(this.hasRumourCards()) {
			ultimatum = new Menu(this.getName()+", you are already revealed. You have only one choice :",DefenseAction.DISCARD);
			requestDisplayPossibilities(ultimatum);
			makeChoice(ultimatum);
			choice=DefenseAction.DISCARD;
		}
		//cannot be chosen by ducking stool if is revealed and has no rumour cards
		return choice;
	}
}
