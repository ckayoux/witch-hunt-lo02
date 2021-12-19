package fr.sos.witchhunt.model.players;

import java.util.List;

import fr.sos.witchhunt.DisplayMediator;
import fr.sos.witchhunt.Visitable;
import fr.sos.witchhunt.Visitor;
import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;
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

	/**
	 * <p><b>Adds a little delay before sending certain display notifications to the view.</b></p>
	 * <p>Expressed in milliseconds.</p>
	 */
	private int shortDelay = 500;
	
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
	 * <p><b>Chooses an {@link TurnAction action} to perform during the player's turn.</b></p>
	 * <p>Based on user input for {@link HumanPlayer human players}, done by artificial intelligence for {@link CPUPlayer CPUPlayers}.</p> 
	 * @return The selected action : {@link TurnAction can be either <i>{@link #accuse(Player) ACCUSE}</i> or <i>{@link #hunt() HUNT}}</i>.
	 * @see TurnAction
	 * @see #accuse(Player)
	 * @see #hunt()
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
	 * <b>Elects this player to take the next turn again, and requests for the display of a suitable notification.</b>
	 * @see #playTurn()
	 * @see #takeNextTurn()
	 */
	public void playTurnAgain() {
		takeNextTurn();
		requestPlayTurnAgainScreen();
	}
	
	/**
	 * <p><b>Elects the player who is going to take the next turn.</b></p>
	 * <p>Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.</p>
	 * <p>Called by certain {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards}' {@link fr.sos.witchhunt.model.cards.Effect effects}, 
	 * like {@link fr.sos.witchhunt.model.cards.DuckingStool the Ducking Stool's} Witch? effect for example.</p>
	 * @see #takeNextTurn()
	 * @see HumanPlayer#chooseNextPlayer()
	 * @see CPUPlayer#chooseNextPlayer()
	 * @see fr.sos.witchhunt.controller.Round#setNextPlayer(Player)
	 */
	public abstract Player chooseNextPlayer();
	
	/**
	 * <b>Elect this player to play the next turn.</b>
	 * Requests for the display of a suitable notification.
	 * @see fr.sos.witchhunt.controller.Round#setNextPlayer(Player) Round::setNextPlayer(Player)
	 */
	public void takeNextTurn() {
		Tabletop.getInstance().getCurrentRound().setNextPlayer(this);
		requestTakeNextTurnScreen();
	}

	/**
	 * <p><b>The player wins the {@link fr.sos.witchhunt.controller.Round current round} as they are the last whose {@link fr.sos.witchhunt.model.Identity identity} wasn't revealed.</b></p>
	 * <p>Lets them {@link #addScore(int) score} 2 points if they were playing as a <i>Witch</i>, or 1 point if they were playing as a <i>Villager</i>.</p>
	 * <p>If another round is to be played, this player will start.</p>
	 * @see #addScore(int)
	 * @see fr.sos.witchhunt.controller.Tabletop#setLastUnrevealedPlayer(Player) Tabletop::setLastUnrevealedPlayer
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see #isRevealed()
	 * @see fr.sos.witchhunt.controller.Round Round
	 */
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
	 * they can choose between {@link #revealOrDiscard() revealing their identity and discarding one of their Rumour cards}.</p>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.
	 * @return A {@link DefenseAction response among <i>ACCUSE</i>, <i>REVEAL</i> and <i>DISCARD</i>}.
	 * @see DefenseAction
	 * @see #revealIdentity()
	 * @see #witch()
	 * @see #discard(RumourCard)
	 * @see #revealOrDiscard()
	 * @see HumanPlayer#chooseDefenseAction()
	 * @see CPUPlayer#chooseDefenseAction()
	 */
	public abstract DefenseAction chooseDefenseAction();
	
	/**
	 * <b>Respond to an {@link #accuse(Player) accusation}, {@link #revealIdentity() revealing the player's identity} or playing a {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect.</b>
	 * If the player has no cards with playable witch effects left, they are {@link #forcedReveal()} forced to reveal their identity.
	 * @return The accused player's disclosed identity, if they revealed it. Otherwise, <i>null</i>.
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see #accuse(Player)
	 * @see #chooseDefenseAction()
	 * @see #canWitch()
	 * @see #witch()
	 * @see #revealIdentity()
	 * @see #forcedReveal()
	 */
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
	
	/**
	 * <b>{@link #selectWitchCard() Selects from the player's hand a Rumour Card with a playable Witch? effect} and triggers it.</b>
	 * @see #selectWitchCard()
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * fr.sos.witchhunt.model.cards.WitchEffect WitchEffect
	 */
	protected void witch () {
		RumourCard chosen = selectWitchCard();
		requestPlayerPlaysWitchEffectScreen(chosen);
		chosen.witch();
	}
	
	/**
	 * <b>{@link #selectHuntCard() Selects from the player's hand a Rumour Card with a playable Hunt! effect} and triggers it.</b>
	 * @see #selectHuntCard()
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * fr.sos.witchhunt.model.cards.HuntEffect HuntEffect
	 */
	protected void hunt() {
		Tabletop.getInstance().setHunter(this);
		RumourCard chosen = selectHuntCard();
		requestPlayerPlaysHuntEffectScreen(chosen);
		chosen.hunt();
	};
	
	/**
	 * <b>Chooses a target among a list of eligible players.</b>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.	
	 * @param eligiblePlayers A list of eligible players.
	 * @return The chosen player.
	 * @see #chooseHuntedTarget(List)
	 * @see HumanPlayer#chooseTarget(List)
	 * @see CPUPlayer#chooseTarget(List)
	 */
	public abstract Player chooseTarget(List<Player> eligiblePlayers);
	
	/**
	 * <b>{@link #chooseTarget(List) chooses a target among a list of eligible players} and {@link #beHunted() marks them as "hunted"} at the game's scale.</b>
	 * @param eligiblePlayersList A list of eligible players.
	 * @return The chosen player, who was beforehand marked as the 'current hunted player'.
	 * @see #chooseTarget(List)
	 * @see #hunt()
	 * @see #canHunt()
	 * @see #beHunted()
	 */
	public Player chooseHuntedTarget(List<Player> eligiblePlayersList) {
		Player chosenTarget = chooseTarget(eligiblePlayersList.stream().filter(p->p!=this).toList());
		chosenTarget.beHunted();
		return chosenTarget;
	}
	
	/**
	 * <b>Marks the player as the "currently hunted player" at the game's scale.</b>
	 * @see fr.sos.witchhunt.controller.Tabletop#setHuntedPlayer(Player) 
	 * @see #canHunt()
	 */
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
	 * <p><b>Adds a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} belonging to a given adversary to the player's {@link #hand}.</b></p>
	 * <p>Requests for a display of this event.</p>
	 * @param rc The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} added to the player's {@link #hand}.
	 * @param from The player from which a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} is subtilized.
	 */
	public void takeRumourCard(RumourCard rc,Player stolenPlayer) {
		requestStealCardFromScreen(stolenPlayer);
		takeRumourCard(rc,stolenPlayer.getHand());
	}
	
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} to {@link #discard(RumourCard) discard from the given {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}.</b>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.
	 * @param in A {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}.
	 * @return The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} that was chosen to be {@link #discard(RumourCard) discarded}.
	 * @see #discard(RumourCard)
	 * @see fr.sos.witchhunt.model.cards.RumourCard Rumour card
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile
	 * @see HumanPlayer#selectCardToDiscard(RumourCardsPile)
	 * @see CPUPlayer#selectCardToDiscard(RumourCardsPile)
	 */
	public abstract RumourCard selectCardToDiscard(RumourCardsPile in) ;
	
	/**
	 * <b>{@link #selectCardToDiscard(RumourCardsPile) Select a card to discard} from the player's {@link #hand}.</b>
	 * @return The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} that was chosen to be {@link #discard(RumourCard) discarded}.
	 * @see #selectCardToDiscard(RumourCardsPile)
	 * @see #hand
	 * @see #discard(RumourCard)
	 * @see fr.sos.witchhunt.model.cards.RumourCard Rumour card
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile
	 */
	public  RumourCard selectCardToDiscard() {
		return selectCardToDiscard(this.getHand());
	};
	
	/**
	 * <p><b>Sends a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} from the player's {@link #hand} to the {@link fr.sos.witchhunt.controller.Tabletop#getPile() pile}.</b></p>
	 * <p>Requests for a display of this event.</p>
	 * <p>Caused by certain {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards'} effects, like the {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} of 
	 * {@link fr.sos.witchhunt.model.cards.DuckingStool the Ducking Stool}.</p>
	 * @param rc A {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} coming from the player's {@link #hand}.
	 * @see fr.sos.witchhunt.model.cards.RumourCard Rumour card
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile#giveCard(RumourCard, RumourCardsPile) RumourCardsPile::giveCard(RumourCard, RumourCardsPile)
	 * @see fr.sos.witchhunt.controller.Tabletop#getPile() Tabletop::getPile()
	 * @see fr.sos.witchhunt.model.cards.DuckingStool DuckingStool
	 */
	public void discard(RumourCard rc) {
		requestDiscardCardScreen(rc);
		this.hand.giveCard(rc, Tabletop.getInstance().getPile());
	}
	
	/**
	 * <b>{@link #selectCardToDiscard() Selects a Rumour card to discard from the player's hand} and {@link #discard(RumourCard) discards} it.</b>
	 * @see #selectCardToDiscard()
	 * @see #discard(RumourCard)
	 */
	public void discard() {
		discard(selectCardToDiscard());
	}
	
	/**
	 * <b>{@link #selectCardToDiscard(RumourCardsPile) Selects a Rumour card to discard from the given RumourCardsPile} and {@link #discard(RumourCard) discards} it.</b>
	 * @see #selectCardToDiscard(RumourCardsPile)
	 * @see #discard(RumourCard)
	 */
	public void discard(RumourCardsPile in) {
		if(in!=null) discard(selectCardToDiscard(in));
		else selectCardToDiscard();
	}
	
	/**
	 * <p><b>{@link #discard(RumourCard) Discards} a random card from the player's hand.</b></p>
	 * <p>Caused by certain {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards'} effects, like the {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect} of 
	 * {@link fr.sos.witchhunt.model.cards.Cauldron Cauldron}.</p>
	 * <p>Requests for the display of a suitable message if the player has no cards left.</p>
	 * @see #discard(RumourCard)
	 * @see fr.sos.witchhunt.model.cards.Cauldron Cauldron
	 */
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
	
	/**
	 * <b>Chooses a card (revealed or not) within a pile of cards.</b>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.
	 * @param from The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards} within which a card must be chosen.
	 * @param seeUnrevealedCards If this boolean is <i>true</i>, unrevealed cards will be shown as if they were not.
	 * @return The chosen {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}.
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile RumourCardsPile
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see HumanPlayer#chooseAnyCard(RumourCardsPile, boolean)
	 * @see CPUPlayer#chooseAnyCard(RumourCardsPile, boolean)
	 */
	public abstract RumourCard chooseAnyCard(RumourCardsPile from,boolean seeUnrevealedCards);
	
	/**
	 * <b>Chooses a revealed card within a pile of cards.</b>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.
	 * @param from The {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards} within which a revealed card must be chosen.
	 * @return The chosen revealed {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}.
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile RumourCardsPile
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see fr.sos.witchhunt.model.cards.RumourCard#isRevealed() RumourCard::isRevealed()
	 * @see HumanPlayer#chooseRevealedCard(RumourCardsPile)
	 * @see CPUPlayer#chooseRevealedCard(RumourCardsPile)
	 */
	public abstract RumourCard chooseRevealedCard(RumourCardsPile from);

	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} valued for its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect}.</b>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.	
	 * @return A {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} chosen for its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect} in particular.
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see fr.sos.witchhunt.model.cards.WitchEffect WitchEffect
	 * @see HumanPlayer#selectWitchCard()
	 * @see CPUPlayer#selectWitchCard()
	 */
	public abstract RumourCard selectWitchCard();
	/**
	 * <b>Selects a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} valued for its {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect}.</b>
	 * Based on user-input for {@link HumanPlayer human players}, chosen by artificial intelligence for {@link CPUPlayer CPUplayers}.	
	 * @return A {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} chosen for its {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} in particular.
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see fr.sos.witchhunt.model.cards.HuntEffect HuntEffect
	 * @see HumanPlayer#selectHuntCard()
	 * @see CPUPlayer#selectHuntCard()
	 */
	public abstract RumourCard selectHuntCard();
	
	/**
	 * <b>Sneakily looks at a given player's {@link #identity}.</b>
	 * Called when using card {@link fr.sos.witchhunt.model.cards.TheInquisition The Inquisiton}.
	 * @param target The player of which the identity is going to be looked at.
	 * @return The targetted player's identity
	 * @see fr.sos.witchhunt.model.Identity Identity
	 * @see fr.sos.witchhunt.model.cards.TheInquisition TheInquisiton
	 * @see CPUPlayer CPUPlayers will remember their target is a witch it is the case.
	 */
	public Identity lookAtPlayersIdentity(Player target) {
		requestLookAtPlayersIdentityScreen(target);
		return target.getIdentity();
	}
	
	/**
	 * <b>Chooses an {@link DefenseAction action} between {@link #revealIdentity() revealing your identity} and {@link #discard(RumourCard) discarding a Rumour card from your hand}.</b>
	 * Called when targetted by the {@link fr.sos.witchhunt.model.cards.DuckingStool Ducking Stool} card's Hunt! effect.
	 * @see DefenseAction
	 * @see #revealIdentity()
	 * @see #discard(RumourCard)
	 * @see fr.sos.witchhunt.model.cards.DuckingStool DuckingStool
	 * @return Either {@link DefenseAction#REVEAL} or {@link DefenseAction#DISCARD}
	 */
	public abstract DefenseAction revealOrDiscard();
	
	/**
	 * <b>Force another player to accuse another opponent than you on their turn, if possible.</b>
	 * Called when using the {@link fr.sos.witchhunt.model.cards.EvilEye Evil Eye} card.
	 * @see #forcedToAccuseBy
	 * @see #playTurn()
	 * @see #accuse(Player)
	 * @param target The player who is going to be forced to accuse another player than this one on their turn.
	 */
	public void forceToAccuseNextTurn(Player target) {
		target.setForcedToAccuseNextTurnBy(this);
	}
		
	/**
	 * <p><b>Resets some of the player's attributes to their initial states.</b></p>
	 * <p>Called after the end of each {@link fr.sos.witchhunt.controller.Round round} by class {@link fr.sos.witchhunt.controller.Tabletop Tabletop}.</p>
	 * <p>The player's {@link #identity identity} is set back to <i>null</i> and their {@link fr.sos.witchhunt.model.cards.IdentityCard#reset() Identity card is reset}.
	 * Their {@link #hand} is also {@link fr.sos.witchhunt.model.cards.RumourCardsPile#reset() reset}.
	 * The player becomes {@link #active} again.</p>
	 * @see fr.sos.witchhunt.model.Resettable;
	 * @see #identity
	 * @see #identityCard
	 * @see fr.sos.witchhunt.model.cards.IdentityCard#reset() resetting an Identity card
	 * @see #hand
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile#reset() resetting an instance of RumourCardsPile
	 * @see #active
	 * @see fr.sos.witchhunt.controller.Tabletop
	 */
	@Override
	public void reset() {
		this.hand.reset();
		this.identity = null;
		this.identityCard.reset();
		this.active = true;
	}
	
	/**
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile#isEmpty() RumourCardsPile::isEmpty()
	 * @see #chooseAnyCard(RumourCardsPile, boolean)
	 * @see #chooseRevealedCard(RumourCardsPile)
	 */
	public boolean targetPileContainsCards(RumourCardsPile rcp) {
		if(rcp.isEmpty()) {
			requestEmptyRCPScreen(rcp);
			return false;
		}
		return true;
	}
	
	//DISPLAY METHODS
	/**
	 * @see #displayMediator
	 */
	@Override
	public void setDisplayMediator(DisplayMediator dm) {
		this.displayMediator=dm;
	}
	
	/**
	 * @see DisplayMediator#passLog(String)
	 * @deprecated Used for debugging purposes
	 */
	@Deprecated
	@Override
	public void requestLog(String msg) {
		displayMediator.passLog(msg);
	}
	
	@Override
	/**
	 * @see DisplayMediator#displayPlayTurnScreen(Player)
	 * @see #playTurn()
	 */
	public void requestPlayTurnScreen() {
		displayMediator.displayPlayTurnScreen(this);
	}
	
	/**
	 * @see DisplayMediator#displayPlayTurnAgainScreen(Player)
	 * @see #playTurnAgain()
	 */
	@Override
	public void requestPlayTurnAgainScreen() {
		if(Tabletop.getInstance().getUnrevealedPlayersList().size()>1) displayMediator.displayPlayTurnAgainScreen(this);
		//the message isn't displayed if there is only one unrevealed player remaining (as the round is going to end).
	}
	
	/**
	 * @see DisplayMediator#displayTakeNextTurnScreen(Player)
	 * @see #takeNextTurn()
	 */
	@Override
	public void requestTakeNextTurnScreen() {
		displayMediator.displayTakeNextTurnScreen(this);
	}

	/**
	 * @see DisplayMediator#displayEndOfTurnScreen()
	 * @see #playTurn()
	 */
	@Override
	public void requestEndOfTurnScreen() {
		displayMediator.displayEndOfTurnScreen();
	}
	
	/**
	 * @see DisplayMediator#displayHasChosenIdentityScreen(Player)
	 * @see CPUPlayer#chooseIdentity()
	 */
	@Override
	public void requestHasChosenIdentityScreen() {
		displayMediator.displayHasChosenIdentityScreen(this);
	}
	
	/**
	 * @see DisplayMediator#displayAccusationScreen(Player, Player)
	 * @param p The accused player.
	 * @see #accuse(Player)
	 */
	@Override
	public void requestAccusationScreen(Player p) {
		displayMediator.displayAccusationScreen(this,p);
	}	
	
	@Override
	/**
	 * <b>Requests the display (only) of a {@link fr.sos.witchhunt.model.Menu Menu}.</b>
	 * Does not collect any input (this is achieved using {@link fr.sos.witchhunt.InputMediator#makeChoice(Menu)}).
	 * @see DisplayMediator#displayPossibilities(Menu)
	 * @see fr.sos.witchhunt.model.Menu Menu
	 * @see HumanPlayer
	 */
	public void requestDisplayPossibilities(Menu m) {
		displayMediator.displayPossibilities(m);
	}
	
	public void requestDisplayNoAvailableHuntEffectsScreen() {
		displayMediator.displayNoAvailableHuntEffectsScreen();
	}
	
	/**
	 * @see DisplayMediator#displayChooseDefenseScreen()
	 * @see #defend()
	 */
	@Override
	public void requestChooseDefenseScreen() {
		displayMediator.displayChooseDefenseScreen(this);
	}
	
	/**
	 * @see DisplayMediator#displayForcedToRevealScreen()
	 * @see #forcedReveal()
	 */
	@Override
	public void requestForcedToRevealScreen() {
		this.delayGame(shortDelay);
		displayMediator.displayForcedToRevealScreen();
	}
	/**
	 * <p>Sends a notification informing the view that the player is going to display their identity.</p>
	 * <p>Then, simulates a delay depending on the player's identity (longer for a witch).</p>
	 * <p>Finally, sends the notification informing the view that it has to display the screen corresponding to the player's identity reveal.</p>
	 * @see DisplayMediator#displayIdentityRevealScreen(Player)
	 * @see #revealIdentity()
	 */
	@Override
	public void requestIdentityRevealScreen() {
		displayMediator.displayGoingToRevealIdentityScreen(this);
		this.delayGame((this.identity==Identity.WITCH)?shortDelay*3:shortDelay);
		displayMediator.displayIdentityRevealScreen(this);
	}
	
	/**
	 * Simulates a little delay before sending the score update notification to the view.
	 * @see DisplayMediator#displayScoreUpdateScreen(Player, int)
	 * @see #addScore(int)
	 */
	@Override
	public void requestScoreUpdateScreen(int scoreUpdatedBy) {
		this.delayGame(shortDelay);
		displayMediator.displayScoreUpdateScreen(this,scoreUpdatedBy);
	}
	
	/**
	 * @see DisplayMediator#displayEliminationScreen(Player, Player)
	 * @see #eliminate(Player)
	 */
	@Override
	public void requestEliminationScreen(Player victim) {
		this.delayGame(shortDelay);
		displayMediator.displayEliminationScreen(this,victim);
	}
	
	/**
	 * @see DisplayMediator#displayLastUnrevealedPlayerScreen(Player)
	 * @see #winRound()
	 */
	@Override
	public void requestLastUnrevealedPlayerScreen() {
		displayMediator.displayLastUnrevealedPlayerScreen(this);
	}
	
	/**
	 * Adds a short delay before sending a notification informing the view that this player has no cards left.
	 * @see DisplayMediator#displayNoCardsScreen(Player)
	 * @see #discardRandomCard()
	 */
	@Override
	public void requestNoCardsScreen() {
		this.delayGame(shortDelay);
		displayMediator.displayNoCardsScreen(this);
	}
	
	/**
	 * @param forcedReveal If <i>true</i>, unrevealed cards' properties will be shown as well 
	 * @param from  The pile of Rumour cards within which a card has to be chosen
	 * @see DisplayMediator#displaySelectCardScreen(RumourCardsPile,boolean)
	 */
	@Override
	public void requestSelectCardScreen(RumourCardsPile from, boolean forcedReveal) {
		displayMediator.displaySelectCardScreen(from, forcedReveal);
	}
	
	/**
	 * @param forcedReveal If <i>true</i>, unrevealed cards' properties will be shown as well 
	 * @param from  A pile of unrevealed Rumour cards
	 * @see DisplayMediator#displaySelectUnrevealedCardScreen(RumourCardsPile,boolean)
	 */
	@Override
	public void requestSelectUnrevealedCardScreen(RumourCardsPile from, boolean forcedReveal) {
		displayMediator.displaySelectUnrevealedCardScreen(from, forcedReveal);
	}
	
	/**
	 * @param forcedReveal If <i>true</i>, unrevealed cards' properties will be shown as well 
	 * @param from  A pile of revealed Rumour cards
	 * @see DisplayMediator#displaySelectRevealedCardScreen(RumourCardsPile,boolean)
	 */
	@Override
	public void requestSelectRevealedCardScreen(RumourCardsPile from, boolean forcedReveal) {
		displayMediator.displaySelectRevealedCardScreen( from, forcedReveal);
	}
	/**
	 * @param from  A pile of Rumour cards with playable Witch? effects
	 * @see DisplayMediator#displaySelectWitchCardScreen(RumourCardsPile)
	 */
	@Override
	public void requestSelectWitchCardScreen(RumourCardsPile from) {
		displayMediator.displaySelectWitchCardScreen(from);
	}
	/**
	 * @param from  A pile of Rumour cards with playable Hunt! effects
	 * @see DisplayMediator#displaySelectHuntCardScreen(RumourCardsPile)
	 */
	@Override
	public void requestSelectHuntCardScreen(RumourCardsPile from) {
		displayMediator.displaySelectHuntCardScreen(from);
	}
	
	
	/**
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile#show(DisplayMediator, boolean)
	 * @param forcedReveal If this boolean is true, unrevealed cards will be shown as if they were revealed (for example, when a player looks at their own cards, they should be allowed to see the details of their unrevealed cards).
	 */
	public void showHand(boolean forcedReveal) {
		this.hand.show(displayMediator, forcedReveal);
	}
	/**
	 * Sends a notification informing the view of the played Witch? effect, and simulates a long delay afterwise.
	 * @see DisplayMediator#displayPlayerPlaysWitchEffectScreen(Player, RumourCard)
	 * @see #witch()
	 * @param The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} of which the {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect} is played.
	 */
	@Override
	public void requestPlayerPlaysWitchEffectScreen(RumourCard rc) {
		displayMediator.displayPlayerPlaysWitchEffectScreen(this,rc);
		this.delayGame(shortDelay*3);
	}
	/**
	 * Sends a notification informing the view of the played Hunt! effect, and simulates a long delay afterwise.
	 * @see DisplayMediator#displayPlayerPlaysHuntEffectScreen(Player, RumourCard)
	 * @see #hunt()
	 * @param The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} of which the {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} is played.
	 */
	@Override
	public void requestPlayerPlaysHuntEffectScreen(RumourCard rc) {
		displayMediator.displayPlayerPlaysHuntEffectScreen(this,rc);
		this.delayGame(shortDelay*3);
	}
	/**
	 * Sends a notification informing the view of the chosen Rumour Card, then simulates a long delay to let the users
	 * read the card's information.
	 * @see DisplayMediator#displayHasChosenCardScreen(Player, RumourCard, boolean)
	 * @param chosen The chosen {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}.
	 * @param forcedReveal If this boolean is true, the chosen card will be shown as if it was revealed even if it is not.
	 */
	@Override
	public void requestHasChosenCardScreen(RumourCard chosen,boolean forceReveal) {
		displayMediator.displayHasChosenCardScreen(this,chosen,forceReveal);
		this.delayGame(shortDelay*3);
	}
	/**
	 * Adds a short delay before sending a notification informing the view there are no cards in the given pile.
	 * @see DisplayMediator#displayNoCardsInPileScreen(RumourCardsPile)
	 * @param rcp An empty {@link fr.sos.witchhunt.model.cards.RumourCardsPile pile of Rumour cards}.
	 */
	@Override
	public void requestEmptyRCPScreen(RumourCardsPile rcp) {
		this.delayGame(shortDelay);
		displayMediator.displayNoCardsInPileScreen(rcp);
	};
	/**
	 * Sends a notification informing of the discarded card to the view, then, adds a delay depending on the 
	 * reveal status of the discarded card.
	 * @see DisplayMediator#displayDiscardCardScreen(Player, RumourCard)
	 * @see #discard(RumourCard)
	 * @param rc The discarded {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}
	 */
	@Override
	public void requestDiscardCardScreen(RumourCard rc) {
		displayMediator.displayDiscardCardScreen(this,rc);
		this.delayGame((rc.isRevealed())?3*shortDelay:shortDelay);
	}
	
	/**
	 * @see DisplayMediator#displayLookAtPlayersIdentityScreen(Player, Player)
	 * @see #lookAtPlayersIdentity(Player)
	 * @param target The player whose identity is being looked at
	 */
	@Override
	public void requestLookAtPlayersIdentityScreen(Player target) {
		displayMediator.displayLookAtPlayersIdentityScreen(this,target);
	}
	/**
	 * Sends a notification informing the view a card has been taken back by the player,
	 * then simulates a long delay to let the users read this card's information.
	 * @see DisplayMediator#displayPlayerHasResetCardScreen(Player, RumourCard)
	 * @see fr.sos.witchhunt.model.cards.PointedHat PointedHat
	 * @param chosen The {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} that was chosen to be playable again.
	 */
	@Override
	public void requestHasResetCardScreen(RumourCard chosen) {
		displayMediator.displayPlayerHasResetCardScreen(this,chosen);
		this.delayGame(shortDelay*3);
	}

	/**
	 * @see DisplayMediator#displayForcedToAccuseScreen(Player, Player)
	 * @see #forceToAccuseNextTurn(Player)
	 * @param by The player who forced this one to accuse another one.
	 */
	@Override
	public void requestForcedToAccuseScreen(Player by) {
		displayMediator.displayForcedToAccuseScreen(this,by);
	}
	/**
	 * @see DisplayMediator#displayStealCardScreen(Player, Player)
	 * @param stolenPlayer The player whose {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} was stolen. 
	 */
	@Override
	public void requestStealCardFromScreen(Player stolenPlayer) {
		displayMediator.displayStealCardScreen(this,stolenPlayer);
	};
	
	public void delayGame(int ms) {
		Tabletop.getInstance().freeze(ms);
	}
	
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
	public boolean hasUnrevealedRumourCards() {
		return !this.getUnrevealedSubhand().isEmpty();
	}
	
	public boolean hasRevealedRumourCards() {
		return !this.getRevealedSubhand().isEmpty();
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
	/**
	 * @see #immunized
	 */
	public boolean isImmunized() {
		//EvilEye immunizes a player against accusation for the next time an accusation occurs.
		return this.immunized;
	}
	/**
	 * <b>A player is accusable if they are {@link #isRevealed() not revealed yet} and {@link #isImmunized() not immunized}.</b>
	 * @see #immunized
	 * @see #isRevealed()
	 * @see fr.sos.witchhunt.controller.Tabletop#getAccusablePlayersList()
	 * @see #accuse(Player)
	 */
	public boolean isAccusable() {
		if(!this.isRevealed() && !this.immunized) return true;
		else return false;
	}

	public boolean isActive() {
		return this.active;
	}
	/**
	 * <b>Certain {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards} can immunize a player against other Rumour cards.</b>
	 * Example : {@link fr.sos.witchhunt.model.cards.Wart the Wart} card {@link fr.sos.witchhunt.model.cards.Wart#grantsImmunityAgainst(RumourCard) grants immunity against}
	 * {@link fr.sos.witchhunt.model.cards.DuckingStool the Ducking Stool} card, once it has been revealed.
	 * @see fr.sos.witchhunt.model.cards.RumourCard#grantsImmunityAgainst(RumourCard)
	 * @see fr.sos.witchhunt.model.cards.Wart Wart
	 * @see fr.sos.witchhunt.model.cards.Broomstick Broomstick
	 * @param rc The Rumour card against which we test whether the player is immunized or not.
	 * @return <i>true</i> if the player is immunized against this card, <i>false</i> otherwise.
	 */
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
	
	/**
	 * <p><b>Adds the given number of points to the {@link #score player's score} and notifies the 
	 * {@link fr.sos.witchhunt.controller.ScoreCounter score counter} of a change in the player's score using the {@link #accept(Visitor)} method.</b></p>
	 * <p>@param pts The number of points by which the {@link #score player's score} is updated <b>(can be negative, as some cards can make a player loose points)</b></p>
	 * <p>Requests for the display of a suitable notification.</p>
	 * <p>A player can score by {@link #accuse() accusing} or {@link #hunt hunting} their opponents, or by being the round's {@link #winRound() last unrevealed player}.</p>
	 * @see #score
	 * @see fr.sos.witchhunt.Visitable
	 * @see #accept(Visitor)
	 * @see fr.sos.witchhunt.controller.ScoreCounter#visit(Player)
	 * 
	 * @see #accuse()
	 * @see #hunt()
	 * @see #winRound()
	 * @see fr.sos.witchhunt.controller.Tabletop#getLastUnrevealedPlayer()
	 */
	public void addScore(int pts) {
		this.score += pts;
		requestScoreUpdateScreen(pts);
		this.accept(Tabletop.getInstance().getScoreCounter());
	}
	/**
	 * @see #immunized
	 */
	public void immunize() {
		this.immunized = true;
	}
	
	/**
	 * @see #immunized
	 */
	public void looseImmunity() {
		this.immunized = false;
	}
	/**
	 * <b>Removes everyone's immunity after an accusation</b>
	 * Called at the end of the {@link #accuse()} method, as the immunity is supposed to be only one-use.
	 * @see #immunized
	 * @see #accuse()
	 * @see #looseImmunity()
	 */
	private void clearImmunities() {
		Tabletop.getInstance().getPlayersList().forEach(p -> {if(p.isImmunized()) p.looseImmunity();});

	}
	
	/**
	 * <b>This player will be out of the current {@link fr.sos.witchhunt.controller.Round round}.</b>
	 * @see #active
	 * @see fr.sos.witchhunt.controller.Tabletop#getActivePlayersList() Tabletop::getActivePlayersList()
	 */
	public void eliminate() {
		this.active = false;
	}
	
	/**
	 * <b>Eliminates a given player from the current {@link fr.sos.witchhunt.controller.Round round}.</b>
	 * Requests the display of a suitable message.
	 * @see #eliminate()
	 * @see #accuse(Player)
	 * @param victim The player to eliminate
	 */
	public void eliminate(Player victim) {
		victim.eliminate();
		requestEliminationScreen(victim);
	}
	
	/**
	 * @see #forcedToAccuseBy
	 * @param player The opponent forcing this player to accuse on their turn.
	 */
	public void setForcedToAccuseNextTurnBy(Player player) {
		this.forcedToAccuseBy=player;
	};
	
	
	//UTILS
	/**
	 * @see #isAccusable()
	 */
	public List<Player> getAccusablePlayers() {
		List <Player> l = Tabletop.getInstance().getAccusablePlayersList();
		l.remove(this);
		return l;
	}
	
	/**
	 * <b>Tests if the player has unrevealed cards with {@link fr.sos.witchhunt.model.cards.HuntEffect#isAllowed() a playable Hunt! effect} in their {@link #hand}.</b>
	 * <p>Indirectly calls the {@link fr.sos.witchhunt.model.cards.HuntEffect#isAllowed() HuntEffect::isAllowed()} method, which requires the player to be
	 * {@link fr.sos.witchhunt.controller.Tabletop#setHunter(Player) as the "currently hunting player" at the scale of the game}.</p>
	 * @return <i>true</i> if the player can play a Hunt! effect.
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile#getPlayableHuntSubpile() RumourCardsPile::getPlayableHuntSubpile() 
	 * @see fr.sos.witchhunt.model.cards.HuntEffect HuntEffect
	 * @see fr.sos.witchhunt.model.cards.HuntEffect#isAllowed() HuntEffect::isAllowed()
	 * @see #chooseTurnAction()
	 * @see fr.sos.witchhunt.controller.Tabletop#setHunter(Player) Tabletop::setHunter(Player)
	 */
	protected boolean canHunt() {
		Tabletop.getInstance().setHunter(this);
		return (!this.hand.getPlayableHuntSubpile().isEmpty());
	}
	
	/**
	 * <b>Tests if the player has unrevealed cards with {@link fr.sos.witchhunt.model.cards.WitchEffect#isAllowed() a playable Witch? effect} in their {@link #hand}.</b>
	 * <p>Calls the {@link fr.sos.witchhunt.model.cards.WitchEffect#isAllowed() HuntEffect::isAllowed()} method.</p>
	 * <p>Indirectly calls the {@link fr.sos.witchhunt.model.cards.WitchEffect#isAllowed() WitchEffect::isAllowed()} method, which requires the player to be
	 * {@link fr.sos.witchhunt.controller.Tabletop#setAccusator(Player) as the "current accusator" at the scale of the game}.</p>
	 * @return <i>true</i> if the player can play a Witch? effect.
	 * @see fr.sos.witchhunt.model.cards.RumourCardsPile#getPlayableWitchSubpile() RumourCardsPile::getPlayableWitchSubpile() 
	 * @see fr.sos.witchhunt.model.cards.WitchEffect WitchEffect
	 * @see fr.sos.witchhunt.model.cards.WitchEffect#isAllowed() WitchEffect::isAllowed()
	 * @see #defend()
	 * @see fr.sos.witchhunt.controller.Tabletop#setHunter(Player) Tabletop::setAccusator(Player)
	 */
	public boolean canWitch() {
		return (!this.hand.getPlayableWitchSubpile().isEmpty());
	}


	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);	
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public boolean equals (Player p) {
		return this.id == p.getId();
	}
	
	
}
