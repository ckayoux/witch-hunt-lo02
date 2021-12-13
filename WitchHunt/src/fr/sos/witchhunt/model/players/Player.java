package fr.sos.witchhunt.model.players;

import java.util.List;

import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.Visitable;
import fr.sos.witchhunt.Visitor;
import fr.sos.witchhunt.DisplayMediator;
import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.controller.Turn;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Resettable;
import fr.sos.witchhunt.model.cards.IdentityCard;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;

/**
 * <p><b>This <i>abstract</i> class defines common behavior and specifies required abstract methods for both {@link HumanPlayer human} and {@link CPUPlayer CPU} players.</b><p>
 * <p>A player has a unique name (chosen at the start of a match human players), and a unique id, which is auto-incremented.</p>
 * <p>Players are added {@link fr.sos.witchhunt.controller.Tabletop#startMatch() at the start of the match} by an instance of {@link fr.sos.witchhunt.controller.Tabletop}.</p>
 * <p>They have an {@link fr.sos.witchhunt.model.Identity identity}, 
 * which is determined by their {@link fr.sos.witchhunt.model.cards.IdentityCard card} (which can be revealed or not).</p>
 * <p>They also possess a {@link #hand hand} containing plural {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards}, which can also be revealed or not.</p>
 * <p>They can be {@link #active active}, or not if they were eliminated or if they are not part of a {@link fr.sos.witchhunt.controller.Tabletop#gameIsTied() tied game}'s last round.</p>
 * 
 * <p>All abstract methods within this class must be defined with different bodies by the classes representing {@link HumanPlayer human} and {@link CPUPlayer CPU} players</p>
 * 
 * <p>They implement {@link fr.sos.witchhunt.model.Resettable Resettable} and are {@link #reset reset} at the end of each round.</p>
 * <p>They also implement {@link fr.sos.witchhunt.Visitable Visitable}, which makes them a possible target for classes implementing 
 * {@link fr.sos.witchhunt.Visitor Visitor}. The score they obtained at each round is saved by an instance of {@link fr.sos.witchhunt.controller.ScoreCounter ScoreCounter},
 * using the {@link https://refactoringguru.cn/design-patterns/visitor Visitor design pattern}.</p>
 * <p>Their display requests are passed to an instance of a class implementing {@link fr.sos.witchhunt.DisplayMediator DisplayMediator}.
 * They themselves implement {@link PlayerDisplayObservable}, which specifies all types of display requests they can need.</p> 
 *
 * @see HumanPlayer
 * @see CPUPlayer
 * 
 * @see fr.sos.witchhunt.model.Identity identity
 * @see fr.sos.witchhunt.Visitable Visitable
 * @see fr.sos.witchhunt.model.Resettable Resettable
 * 
 * @see PlayerDisplayObservable
 * @see fr.sos.witchhunt.DisplayMediator DisplayMediator
 *
 */
public abstract class Player implements PlayerDisplayObservable, Resettable, Visitable {
	
	//ATTRIBUTES
	
	/**
	 * A player's name, unique in the game.
	 */
	protected String name;
	/**
	 * The player's id, unique in the game and auto-incremented.
	 */
	protected int id;
	
	/**
	 * <p><b>The player's current score.</b></p>
	 * <p>A player can score points by accusing a player and making them reveal they are a witch, 
	 * {@link #winRound()} by remaining the last unrevealed one. They can also earn or loose points thanks to the 
	 * {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effects} of certain {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards}.</p>
	 * <p>A player can win the round if they are leading with score >= 5.</p>
	 * <p>Updated exclusively using method {@link #addScore(int)}, which also accepts a {@link fr.sos.witchhunt.controller.ScoreCounter#visit(Player) visit from the score counter}
	 * to update the latter one.</p>
	 * <p>The player's score is not reset between two rounds ! Thus, it isn't affected by the {@link #reset()} method.</p>
	 * 
	 * @see #addScore(int) 
	 * @see #accuse(Player)
	 * @see #winRound()
	 * 
	 * @see fr.sos.witchhunt.controller.ScoreCounter ScoreCounter
	 * @see fr.sos.witchhunt.controller.ScoreCounter#visit(Player) ScoreCounter::visit(Player)
	 * @see fr.sos.witchhunt.Visitable Visitable
	 * @see fr.sos.witchhunt.controller.ScoreCounter#getPotentialWinners() ScoreCounter::getPotentialWinners()
	 * @see fr.sos.witchhunt.controller.ScoreCounter#getRanking() ScoreCounter::getRanking()
	 */
	protected int score=0;
	
	/**
	 * <p><b>The player's identity, either <i>Witch</i> or <i>Villager</i>. It is always equal to that associated to the player's {@link fr.sos.witchhunt.model.cards.IdentityCard Identity card}.</b></p>
	 * <p>Chosen at the start of each round.</p>
	 * <p>If a player is revealed as a <i>Witch</i>, they are {@link #eliminate() eliminated}.</p>
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see #identityCard
	 * @see #revealIdentity()
	 * @see #chooseIdentity()
	 */
	protected Identity identity;
	/**
	 * <p><b>The player's {@link fr.sos.witchhunt.model.cards.IdentityCard Identity card}. Associated to the player when they are instantiated, for the whole duration of the match.</b></p>
	 * <p>Can be {@link fr.sos.witchhunt.model.cards.IdentityCard#isRevealed() revealed or unrevealed}.</p>
	 * <p>Linked to the player's {@link #identity} attribute.</p>
	 * <p>Reset when the player is {@link #reset()} themselves.</p>
	 * <p>Chosen at the start of each round.</p>
	 * @link fr.sos.witchhunt.model.cards.IdentityCard IdentityCard
	 * @see #identity
	 * @see #revealIdentity()
	 * @see #chooseIdentity()
	 * @see #reset()
	 * @see fr.sos.witchhunt.model.Resettable Resettable
	 */	
	protected IdentityCard identityCard;
	
	/**
	 * <p><b>The player's hand, containing {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards}, is an instance of {@link fr.sos.witchhunt.model.cards.RumourCardsPile RumourCardsPile}.</b></p>
	 * <p>These cards can be revealed or not.</p>
	 * <p>The player can use a card on their turn, by triggering its {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect},
	 * of when {@link #defend() defending} against an {@link #accuse(Player) accusation}, by triggering its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect}.</p>
	 * <p>Some of these {@link fr.sos.witchhunt.model.cards.Effect effects} can lead the player to discarding, stealing, or taking back cards, thus modifying their hand.</p>
	 * <p>The player's hand is {@link fr.sos.witchhunt.model.cards.RumourCardsPile#reset() reset} when they get <i>{@link #reset()}</i> themselves, 
	 * {@link fr.sos.witchhunt.model.cards.RumourCard#reset() resetting all Rumour cards} it contains and returning them 
	 * to {@link fr.sos.witchhunt.controller.Tabletop the game's list of existing cards}.</p>
	 * @see #getRevealedSubhand()
	 * @see #getUnrevealedSubhand()
	 * @see #showHand(boolean)
	 * @see #discard(RumourCard)
	 * @see #takeRumourCard(RumourCard, Player)
	 *
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile#reset() RumourCardsPile::reset()
	 * @see #reset()
	 * @see fr.sos.witchhunt.model.Resettable Resettable
	 * 
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile RumourCardsPile
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 */
	protected RumourCardsPile hand;
	
	/**
	 * <p><b>A reference to an adversary forcing the player to accuse someone else on their turn.</b><p>
	 * <p>Indeed, the {@link fr.sos.witchhunt.model.cards.EvilEye Evil Eye} {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} can be used by a player to enforce an adversary to accuse
	 * any player but them the next turn.</p>
	 * @see #forceToAccuseNextTurn(Player)
	 * @see fr.sos.witchhunt.model.cards.EvilEye EvilEye
	 * @see #immunized
	 * @see #accuse(Player)
	 */
	private Player forcedToAccuseBy=null;
	
	/**
	 * <p><b>When this boolean is true, this player can not be targetted by an accusation.</b></p>
	 * <p>Indeed, the {@link fr.sos.witchhunt.model.cards.EvilEye Evil Eye} {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} can be used by a player to enforce an adversary to accuse
	 * any player but them the next turn.</p>
	 * @see fr.sos.witchhunt.model.cards.EvilEye EvilEye
	 * @see #forcedToAccuseBy
	 * @see #immunize()
	 */
	private boolean immunized; 
	
	/**
	 * <p><b>When this boolean is false, the player is out of the current {@link fr.sos.witchhunt.controller.Round round}.</b></p>
	 * <p>This can happen either when the player gets {@link #eliminate()} eliminated, or when the player is not part of the last round 
	 * of a {@link fr.sos.witchhunt.controller.Tabletop#gameIsTied() tied game}.</p>
	 * <p>This boolean is set back to <i>true</i> at the end of each round, when the player gets {@link #reset()}.</p>
	 * @see #eliminate()
	 * @see #eliminate(Player)
	 * @see #reset()
	 * @see fr.sos.witchhunt.controller.Tabletop#getActivePlayersList() Tabletop::getActivePlayersList()
	 */
	private boolean active = true;

	/**
	 * <p><b>The players are associated with an instance of a class implementing {@link fr.sos.witchhunt.DisplayMediator DisplayMediator}</b></p>
	 * <p>The players passes every display request they have to this object, which is responsible of organizing their display.</p>
	 * <p>This field is binded at the player's creation.</p>
	 * @see fr.sos.witchhunt.DisplayMediator DisplayMediator
	 * @see PlayerDisplayObservable
	 */
	protected DisplayMediator displayMediator;

	
	//CONSTRUCTORS
	/**
	 * <p><b>Instanciates a player with a given name and a given id.</b></p>
	 * <p>Also initializes their {@link fr.sos.witchhunt.model.cards.IdentityCard IdentityCard}, as well as their {@link #hand}.</p>
	 * @param name The pseudonym given to this player. Will be used in the display.
	 * @param id An integer identifying the player. Unicity must be ensured.
	 * @see HumanPlayer#HumanPlayer(String, int)
	 */
	public Player(String name, int id) {
		if(name=="") {
			this.name="Player "+Integer.toString(id);
		}
		else {
			this.name=name;
			this.id=id;
		}
		this.identityCard = new IdentityCard();
		this.hand = new RumourCardsPile(this);
	}
	/**
	 * <p><b>Instanciates a player with a given id.</b></p>
	 * <p>Their name has to be generated automatically.</p>
	 * <p>Also initializes their {@link fr.sos.witchhunt.model.cards.IdentityCard IdentityCard}, as well as their {@link #hand}.</p>
	 * @param id An integer identifying the player. Unicity must be ensured.
	 * @see CPUPlayer#CPUPlayer(int, int)
	 */
	public Player(int id) {
		this.id=id;
		this.identityCard = new IdentityCard();
		this.hand = new RumourCardsPile();
	}
	
	//GAME ACTIONS METHODS
	/**
	 * <b>Chooses and sets the player's {@link #identity} and that of their {@link #identityCard}.</b>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.
	 * @see HumanPlayer#chooseIdentity()
	 * @see CPUPlayer#chooseIdentity()
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see fr.sos.witchhunt.model.IdentityCard Identity card
	 */
	public abstract void chooseIdentity();;
	
	/**
	 * <b>Reveals the player's {@link #identityCard} and returns their {@link #identity}.</b>
	 * Requests display of this event.
	 * @return The player's {@link #identity}
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see fr.sos.witchhunt.model.IdentityCard Identity card
	 */
	public Identity revealIdentity() {
		requestIdentityRevealScreen();
		this.identityCard.reveal();	
		return this.identity;
	}
	
	/**
	 * <p><b>Reveals the player's {@link #identityCard} and returns their {@link #identity}.</b></p>
	 * <p><b>Requests the display of a specific message implying the player has been forced to do so.</b>
	 * Different from {@link #revealIdentity()} only by the display.
	 * Can happen when {@link #canWitch() there are no playable witch effects} left, or when 
	 * a {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} forces the player to reveal their identity.</p>
	 * @return The player's {@link #identity}
	 * @see #revealIdentity()
	 * @see #canWitch()
	 */
	public Identity forcedReveal() {
		requestForcedToRevealScreen();
		return this.revealIdentity();
		
	}
	
	/**
	 * <p><b>Chooses an action to perform during the player's turn.</b></p>
	 * <p>Based on user input for {@link HumanPlayer human players}, done by artificial intelligence for {@link CPUPlayer CPUPlayers}.</p> 
	 * @return The selected action : {@link TurnAction can be either <i>{@link #accuse(Player) ACCUSE}</i> or <i>{@link #hunt() HUNT}</i>.
	 * @see TurnAction
	 * @see HumanPlayer#chooseTurnAction()
	 * @see CPUPlayer#chooseTurnAction()
	 */
	public abstract TurnAction chooseTurnAction();
	
	/**
	 * <p><b>Called when it is the player's {@link fr.sos.witchhunt.controller.Turn turn}.</b></p>
	 * <p>Causes the player to choose an {@link TurnAction action to perform} and executes it.</p>
	 * <p>Requests display of a message indicating it is the player's turn.</p>
	 * <p>In the special situation where the player has been forced to accuse an adversary by {@link fr.sos.witchhunt.model.cards.EvilEye the Evil Eye Rumour card},
	 * the player is directly asked to accuse an opponent.</p>
	 * @see TurnAction
	 * @see #accuse(Player)
	 * @see #hunt()
	 * @see #forcedToAccuseBy
	 */
	public void playTurn() {
		requestPlayTurnScreen();
		if(forcedToAccuseBy==null) {
			switch(chooseTurnAction()) {
				case ACCUSE:
					accuse();
					break;
				case HUNT:
					hunt();
					break;
			}
		}
		else { //the EvilEye RumourCard can force you to accuse on your turn
			List<Player> l = getAccusablePlayers();
			l.remove(forcedToAccuseBy);
			if(!l.isEmpty()) {
				forcedToAccuseBy.immunize();
			}
			requestForcedToAccuseScreen(forcedToAccuseBy);
			accuse();
			forcedToAccuseBy=null;
			clearImmunities();
		}
		requestEndOfTurnScreen();
		
	}
	/**
	 * <p><b>Accuses another player of practicing witchcraft.</b></p>
	 * <p>Requests a display for this event.</p>
	 * <p>Calls the {@link #defend()} method on the target, allowing them to choose between {@link DefenseAction revealing their identity and playing the witch effect of a Rumour card in their hand}.</p>
	 * <p>If the target could keep their identity hidden, their {@link #defend()} method returns <i>null</i> and nothing happens.
	 * Else, it returns the value of their {@link fr.sos.witchhunt.model.Identity identity}.
	 * If the target was revealed as a <i>Villager</i>, the target {@link #takeNextTurn() takes the next turn}.
	 * Otherwise, if the target was revealed as a <i>Witch</i>, the accusing player {@link #addScore(int) earns 1 point} and {@link #eliminate(Player) eliminates their target}.</p>  
	 * @param p The player to accuse.
	 * @see fr.sos.witchhunt.controller.Tabletop#setAccusator(Player) Tabletop::setAccusator(Player)
	 * @see fr.sos.witchhunt.controller.Tabletop#setAccusedPlayer(Player) Tabletop::setAccusedPlayer(Player)
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see #defend()
	 * @see #chooseDefenseAction()
	 * @see DefenseAction
	 * @see #takeNextTurn()
	 * @see #addScore(int)
	 * @see #eliminate(Player)
	 */
	public void accuse(Player p) {
		Tabletop.getInstance().setAccusator(this);
		Tabletop.getInstance().setAccusedPlayer(p);
		requestAccusationScreen(p);
		Identity returnedIdentity = p.defend();
		if(returnedIdentity != null) {
			switch(returnedIdentity) {
			case VILLAGER: 
				p.takeNextTurn();
				break;
			case WITCH:
				eliminate(p);
				this.addScore(1);
				this.playTurnAgain();
				break;
			}
		}
	}
	
	/**
	 * <b>Chooses a player to accuse and accuse them.</b>
	 * @see #accuse(Player)
	 * @see #choosePlayerToAccuse()
	 */
	protected void accuse() {
		accuse(choosePlayerToAccuse());
	}
	
	/**
	 * <p><b>Chooses a player to accuse</b></p>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.
	 * @return A reference pointing towards the selected target.
	 * @see fr.sos.witchhunt.controller.Tabletop#getAccusablePlayersList() Tabletop::getAccusablePlayersList()
	 * @see #accuse(Player)
	 * @see HumanPlayer#choosePlayerToAccuse()
	 * @see CPUPlayer#choosePlayerToAccuse()
	 */
	protected abstract Player choosePlayerToAccuse();	
	
	/**
	 * <p><b>Chooses an action to respond to an {@link #accuse(Player) accusation} or a {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}.</b></p>
	 * <p>When a player is accused, they can choose between {@link #witch() playing a Witch? effect}, {@link #canWitch() if they can}, and {@link #revealIdentity() revealing their identity}.</p>
	 * <p>When a player is targetted by certain {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} like that of {@link fr.sos.witchhunt.model.cards.DuckingStool}, 
	 * they can choose between {@link #revealIdentity() revealing their identity} and {@link #discard(RumourCard) discarding one of their Rumour cards}.</p>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.
	 * @return A {@link DefenseAction response among <i>ACCUSE</i>, <i>REVEAL</i> and <i>DISCARD</i>}.
	 * @see DefenseAction
	 * @see #revealIdentity()
	 * @see #witch()
	 * @see #discard(RumourCard)
	 * @see HumanPlayer#chooseDefenseAction()
	 * @see CPUPlayer#chooseDefenseAction()
	 */
	public abstract DefenseAction chooseDefenseAction();
	
	public Identity defend() {
		if(this.canWitch()) {
			requestChooseDefenseScreen();
			switch(chooseDefenseAction()) {
				case WITCH:
					this.witch();
					return null; //every witch effect protects the accused player's identity. null is always returned
				case REVEAL:
					return this.revealIdentity(); //accused players reveal their identity and return it 
			}
		}
		else return forcedReveal();
		return null;
	}
	

	protected void witch () {
		RumourCard chosen = selectWitchCard();
		requestPlayerPlaysWitchEffectScreen(chosen);
		chosen.witch();
	}
	
	protected void hunt() {
		Tabletop.getInstance().setHunter(this);
		RumourCard chosen = selectHuntCard();
		requestPlayerPlaysHuntEffectScreen(chosen);
		chosen.hunt();
	};
	
	public abstract Player chooseTarget(List<Player> eligiblePlayers);
	
	public Player chooseHuntedTarget(List<Player> eligiblePlayersList) {
		Player chosenTarget = chooseTarget(eligiblePlayersList.stream().filter(p->p!=this).toList());
		chosenTarget.beHunted();
		return chosenTarget;
	}
	
	public void beHunted() {
		Tabletop.getInstance().setHuntedPlayer(this);
	}
	
	/**
	 * <b>Adds a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} from the given {@link fr.sos.witchhunt.model.cards.RumourCardsPile Rumour cards pile} to the 
	 * player's {@link #hand}.</b>
	 * @param rc The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} added to the player's {@link #hand}.
	 * @param from The {@link fr.sos.witchhunt.model.cards.RumourCardsPile Rumour cards pile} (another {@link #getHand() player's hand} or the {@link fr.sos.witchhunt.controller.Tabletop#getPile() common pile}.
	 */
	public void takeRumourCard(RumourCard rc,RumourCardsPile from) {
		from.giveCard(rc, this.hand);
	}
	/**
	 * <b>Adds a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} belonging to a given adversary to the player's {@link #hand}.</b>
	 * @param rc The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} added to the player's {@link #hand}.
	 * @param from The player from which a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} is subtilized.
	 */
	public void takeRumourCard(RumourCard rc,Player stolenPlayer) {
		requestStealCardFromScreen(stolenPlayer);
		stolenPlayer.getHand().giveCard(rc, this.hand);
	}
	
	/**
	 * 
	 * @param in
	 * @return
	 */
	public abstract RumourCard selectCardToDiscard(RumourCardsPile in) ;
	public  RumourCard selectCardToDiscard() {
		return selectCardToDiscard(this.getHand());
	};
	public void discard(RumourCard rc) {
		requestDiscardCardScreen(rc);
		this.hand.giveCard(rc, Tabletop.getInstance().getPile());
	}
	public void discard() {
		discard(selectCardToDiscard());
	}
	public void discard(RumourCardsPile in) {
		if(in!=null) discard(selectCardToDiscard(in));
		else selectCardToDiscard();
	}
	
	public void discardRandomCard() {
		//let's assume we can only discard unrevealed cards, unless all the cards are revealed
		RumourCard chosenCard;
		if(this.hasRumourCards()) {
			if(this.hasUnrevealedRumourCards()) {
				chosenCard = this.getUnrevealedSubhand().getRandomCard();
			}
			else {
				chosenCard = this.hand.getRandomCard();
			}
			
			if (chosenCard != null) discard(chosenCard);
		}
		else requestNoCardsScreen();
	}
	
	public boolean hasUnrevealedRumourCards() {
		return !this.getRevealedSubhand().equals(this.hand);
	}
	
	public boolean hasRevealedRumourCards() {
		return !this.getRevealedSubhand().isEmpty();
	}
	
	
	public abstract RumourCard selectWitchCard();
	public abstract RumourCard selectHuntCard();
	
	
	public abstract Player chooseNextPlayer();
	
	public void winRound() {
		Tabletop.getInstance().setLastUnrevealedPlayer(this);
		requestLastUnrevealedPlayerScreen();
		switch (this.identity) {
			case WITCH:
				this.addScore(2);
				break;
				
			case VILLAGER:
				this.addScore(1);
				break;
		}
	}
	
	public void reset() {
		this.hand.reset();
		this.identity = null;
		this.identityCard.reset();
		this.active = true;
	}
	
	//DISPLAY METHODS
	@Override
	public void setDisplayMediator(DisplayMediator dm) {
		this.displayMediator=dm;
	}
	@Override
	public void requestLog(String msg) {
		displayMediator.passLog(msg);
	}
	
	@Override
	public void requestPlayTurnScreen() {
		displayMediator.displayPlayTurnScreen(name);
	}
	
	@Override
	public void requestEndOfTurnScreen() {
		displayMediator.displayEndOfTurnScreen();
	}
	
	@Override
	public void requestAccusationScreen(Player p) {
		displayMediator.displayAccusationScreen(this,p);
	}	
	
	@Override
	public void requestDisplayPossibilities(Menu m) {
		displayMediator.displayPossibilities(m);
	}
	

	@Override
	public void requestChooseDefenseScreen() {
		displayMediator.displayChooseDefenseScreen();
	}
	
	@Override
	public void requestForcedToRevealScreen() {
		displayMediator.displayForcedToRevealScreen();
	}
	
	@Override
	public void requestIdentityRevealScreen() {
		displayMediator.displayIdentityRevealScreen(this);
	}
	
	@Override
	public void requestScoreUpdateScreen(int scoreUpdatedBy) {
		displayMediator.displayScoreUpdateScreen(this,scoreUpdatedBy);
	}
	
	@Override
	public void requestEliminationScreen(Player victim) {
		displayMediator.displayEliminationScreen(this,victim);
	}
	
	@Override
	public void requestLastUnrevealedPlayerScreen() {
		displayMediator.displayLastUnrevealedPlayerScreen(this);
	}
	
	@Override
	public void requestNoCardsScreen() {
		displayMediator.displayNoCardsScreen(this);
	}
	
	@Override
	public void requestSelectCardScreen() {
		displayMediator.displaySelectCardScreen();
	}

	@Override
	public void requestSelectUnrevealedCardScreen() {
		displayMediator.displaySelectUnrevealedCardScreen();
	}
	
	@Override
	public void requestSelectRevealedCardScreen() {
		displayMediator.displaySelectRevealedCardScreen();
	}
	
	public void showHand(boolean forcedReveal) {
		this.hand.show(displayMediator, forcedReveal);
	}
	
	@Override
	public void requestPlayerPlaysWitchEffectScreen(RumourCard rc) {
		displayMediator.displayPlayerPlaysWitchEffectScreen(this,rc);
	}
	@Override
	public void requestPlayerPlaysHuntEffectScreen(RumourCard rc) {
		displayMediator.displayPlayerPlaysHuntEffectScreen(this,rc);
	}
	@Override
	public void requestHasChosenCardScreen(RumourCard chosen,boolean forceReveal) {
		displayMediator.displayHasChosenCardScreen(this,chosen,forceReveal);
	}
	@Override
	public void requestEmptyRCPScreen(RumourCardsPile rcp) {
		displayMediator.displayNoCardsInPileScreen(rcp);
	};
	@Override
	public void requestDiscardCardScreen(RumourCard rc) {
		displayMediator.displayDiscardCardScreen(this,rc);
	}
	@Override
	public void requestLookAtPlayersIdentityScreen(Player target) {
		displayMediator.displayLookAtPlayersIdentityScreen(this,target);
	}
	@Override
	public void requestHasResetCardScreen(RumourCard chosen) {
		displayMediator.displayPlayerHasResetCardScreen(this,chosen);
	}
	@Override
	public void requestTakeNextTurnScreen() {
		displayMediator.displayTakeNextTurnScreen(this);
	}
	@Override
	public void requestPlayTurnAgainScreen() {
		if(Tabletop.getInstance().getUnrevealedPlayersList().size()>1) displayMediator.displayPlayTurnAgainScreen(this);
		//the message isn't displayed if there is only one unrevealed player remaining (as the round is going to end).
	}
	@Override
	public void sleep(int ms) {
		displayMediator.freezeDisplay(ms);
	}
	@Override
	public void requestForcedToAccuseScreen(Player by) {
		displayMediator.displayForcedToAccuseScreen(this,by);
	}
	@Override
	public void requestStealCardFromScreen(Player stolenPlayer) {
		displayMediator.displayStealCardScreen(this,stolenPlayer);
	};
	//GETTERS
	public String getName() {
		return this.name;
	}
	public RumourCardsPile getHand() {
		return this.hand;
	}
	public RumourCardsPile getUnrevealedSubhand() {
		return this.hand.getUnrevealedSubpile();
	}
	public RumourCardsPile getRevealedSubhand() {
		return this.hand.getRevealedSubpile();
	}
	public Identity getIdentity() {
		return this.identity;
	}
	public IdentityCard getIdentityCard() {
		return this.identityCard;
	}
	public boolean isRevealed() {
		return this.identityCard.isRevealed();
	}
	public int getScore() {
		return this.score;
	}
	public int getId() {
		return this.id;
	}
	public boolean isImmunized() {
		//EvilEye immunizes a player against accusation for the next time an accusation occurs.
		return this.immunized;
	}
	public boolean isAccusable() {
		if(!this.isRevealed() && !this.immunized) return true;
		else return false;
	}
	public boolean isActive() {
		return this.active;
	}
	public boolean isImmunizedAgainstRumourCard(RumourCard rc) {
		//some cards grant immunity against the huntEffect of others. Example : Broomstick prevents from being targeted by the huntEffect of AngryMob
		for(RumourCard card : this.hand.getCards()) {
			if(card.grantsImmunityAgainst(rc)) return true;
		}
		return false;
	}
	public boolean hasRumourCards() {
		return (!this.hand.isEmpty());
	}
	
	//SETTERS
	public void setActive(boolean active) {
		this.active=active;
	}
	
	public void addScore(int pts) {
		this.score += pts;
		requestScoreUpdateScreen(pts);
		Tabletop.getInstance().getScoreCounter().visit(this);
	}
	
	public void immunize() {
		this.immunized = true;
	}
	
	public void looseImmunity() {
		this.immunized = false;
	}
	private void clearImmunities() {
		Tabletop.getInstance().getPlayersList().forEach(p -> {if(p.isImmunized()) p.looseImmunity();});
		/*is called at the end of the accuse method.
		will remove everyone's immunity after an accusation excepted for the accused - because he could have just immunized itselfs with the witch effect of Evil Eye.*/
	}
	
	public void eliminate() {
		this.active = false;
	}
	
	public void eliminate(Player victim) {
		victim.eliminate();
		requestEliminationScreen(victim);
	}
	
	
	public void setForcedToAccuseNextTurnBy(Player player) {
		this.forcedToAccuseBy=player;
	};
	
	
	//UTILS
	public List<Player> getAccusablePlayers() {
		List <Player> l = Tabletop.getInstance().getAccusablePlayersList();
		l.remove(this);
		return l;
	}
	
	protected boolean canHunt() {
		Tabletop.getInstance().setHunter(this);
		return (!this.hand.getPlayableHuntSubpile().isEmpty());
	}
	
	public boolean canWitch() {
		return (!this.hand.getPlayableWitchSubpile().isEmpty());
	}
	
	public String toString() {
		return this.name;
	}
	
	public abstract RumourCard chooseAnyCard(RumourCardsPile from,boolean seeUnrevealedCards);
	public abstract RumourCard chooseRevealedCard(RumourCardsPile from);
	
	public boolean targetPileContainsCards(RumourCardsPile rcp) {
		if(rcp.isEmpty()) {
			requestEmptyRCPScreen(rcp);
			return false;
		}
		return true;
	}
	public Identity lookAtPlayersIdentity(Player target) {
		requestLookAtPlayersIdentityScreen(target);
		return target.getIdentity();
	}
	
	public void takeNextTurn() {
		Tabletop.getInstance().getCurrentRound().setNextPlayer(this);
		requestTakeNextTurnScreen();
	}
	
	public void playTurnAgain() {
		Tabletop.getInstance().getCurrentRound().setNextPlayer(this);
		requestPlayTurnAgainScreen();
	}
	
	public abstract DefenseAction revealOrDiscard();
	
	public void forceToAccuseNextTurn(Player target) {
		target.setForcedToAccuseNextTurnBy(this);
	}
	
	
	public void accept(Visitor visitor) {
		visitor.visit(this);	
	}
	
	public boolean equals (Player p) {
		return this.id == p.getId();
	}
	
	
}
