package fr.sos.witchhunt;

import java.util.List;

import fr.sos.witchhunt.controller.ScoreCounter;
import fr.sos.witchhunt.controller.ScoreCounter.ScoreBoard;
import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy;

/**
 * <p><b>Interface implemented by the central display controller</b></p>
 * <p>Makes the link between the Model (<i>Player</i> and <i>RumourCardsPile</i>) and the View (<i>StdView</i> and <i>GuiView</i>).</p>
 * <p>Specifies a set of methods which must be redefined to achieve all cases where a display associated with a Model's component has to be shown.</p>
 * 
 * @see fr.sos.witchhunt.controller.DisplayController
 * @see InputMediator
 * 
 */
public interface DisplayMediator {
	
	/**
	 * <b>Requests the DM to display the "add players" screen.</b>
	 * @param tabletop The mtach (instance of {@link fr.sos.witchhunt.controller.Tabletop Tabletop}) which is being set up
	 */
	public void displayAddPlayersScreen(Tabletop tabletop) ;
	
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation when one player has been added.</b>
	 * @param p The player who has been added
	 */
	public void displayAddedPlayerScreen(Player p);
	
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation where all players have been added to the match.</b>
	 * @param n The number of added players
	 */
	public void displayAddedPlayersScreen(int n);

	/**
	 * <b>Requests the DM to display a yes-no question.</b>
	 * Not in charge of getting the answer.
	 * @param yesnoq The yes-no question
	 */
	public void logYesNoQuestion(String yesnoq);

//GAME FLOW DISPLAY METHODS
	
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation where the game is tied.</b>
	 * @param potentialWinners The list of all players all players with the highest score, that score being higher than 5.
	 */
	public void displayGameIsTiedScreen(List<Player> potentialWinners);
	
	/**
	 * <b>Requests the DM to display the screen corresponding to the match's start.</b>
	 * @see Tabletop
	 */
	public void displayMatchStartScreen();
	
	/**
	 * <b>Requests the DM to display the screen corresponding to a Round's start.</b>
	 * @see fr.sos.witchhunt.controller.Round Round
	 * @param roundNumber
	 */
	public void displayRoundStartScreen(int roundNumber);
	
	/**
	 * <b>Requests the DM to display the screen corresponding to the "choose an identity" screen.</b>
	 */
	public void displayChooseIdentityScreen();
	
	/**
	 * <b>Requests the DM to display the suited screen when a player has chosen their identity.</b>
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseIdentity()
	 * @param p The player who has chosen their identity
	 */
	public void displayHasChosenIdentityScreen(Player p);

	/**
	 * <b>Requests the DM to display the screen corresponding to the situation where all players have chosen their identity.</b>
	 */
	public void displayAllPlayersHaveChosenTheirIdentityScreen();
	

	/**
	 * <b>Requests the DM to display the screen corresponding to the situation where all cards have been distributed at the start of a Round.</b>
	 * @param distributedCardsCount The number of Rumour cards given to each player
	 * @param discardedCardsCount The number of cards directly put into the pile
	 */
	public void distributeHandScreen(int distributedCardsCount, int discardedCardsCount);

	
	/**
	 * <b>Requests the DM to display the screen corresponding to the end of a round.</b>
	 * @param roundNumber The number of the round that just ended.
	 */
	public void displayRoundEndScreen(int roundNumber);
	
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation where the game is over.</b>
	 */
	public void displayMatchEndScreen();
	
	/**
	 * <b>Requests the DM to display a screen to congratulate the winning player.</b>
	 * @param winner The player who has won the game
	 */
	public void displayWinnerScreen(Player winner);

	
/**
 * <b>Requests the DM to display the screen corresponding to the beginning of a turn.</b>
 * @see fr.sos.witchhunt.model.players.Player#playTurn() Player::playTurn
 * @param p The player taking the current turn.
 */
public void displayPlayTurnScreen(Player p);

/**
 * <b>Requests the DM to notify the view that a player has taken the next turn.</b>
 * @param p the player taking the next turn.
 * @see fr.sos.witchhunt.model.players.Player#takeNextTurn() Player::takeNextTurn
 */
	public void displayTakeNextTurnScreen(Player p);
/**
 * <b>Requests the DM to notify the view that a player is authorized to play one turn again.</b>
 * @param p the player taking another turn.
 * @see fr.sos.witchhunt.model.players.Player#playTurnAgain() Player::playTurnAgain
 */
	public void displayPlayTurnAgainScreen(Player p);
	/**
	 * <b>Requests the DM to display the screen corresponding to the end of a turn.</b>
	 * @see fr.sos.witchhunt.model.players.Player#playTurn() Player::playTurn
	 */
	public void displayEndOfTurnScreen();

/**
 * 	<p><b>Requests the DM to display the screen corresponding to the accusation of a player by another.</b></p>
 * @param accusator the player who accused.
 * @param accused the player who got accused.
 * @see fr.sos.witchhunt.model.players.Player#accuse(Player) Player::accuse(Player)
 */
	public void displayAccusationScreen(Player accusator, Player accused);
	
/**
 * <b>Requests the DM to display the screen corresponding to the choice of an answer to an accusation.</b>
 * @param p The player who must prove they aren't a witch, or burn on the stake
 * @see fr.sos.witchhunt.model.players.Player#defend() Player::defend()
 */
	public void displayChooseDefenseScreen(Player p);
	
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation when a player is targetted by a Rumour card's Hunt! effect.</b>
	 * @param huntedPlayer The Huntm! effect's target
	 */
	public void displayHuntedPlayerScreen(Player huntedPlayer);

/**
 * <b>Requests the DM to display the screen corresponding to the case where someone is forced to reveal their identity.</b>
 * @see fr.sos.witchhunt.model.players.Player#forcedReveal() Player::forcedReveal()
 */
	public void displayForcedToRevealScreen();
	
	/**
	 * <b>Requests the DM to display a notification informing a player is going to reveal their identity.</b>
	 * @param p The player going to reveal their identity.
	 */
	public void displayGoingToRevealIdentityScreen(Player p);

/**
 * <b>Requests the DM to display the screen corresponding to the case where someone reveals their identity.</b>
 * @param p The player revealing their identity.
 * @param lastUnrevealedPlayer The game's last remaining unrevealed player, or null.
 * @see fr.sos.witchhunt.model.players.Player#revealIdentity() Player::revealIdentity()
 */
	public void displayIdentityRevealScreen(Player p,Player lastUnrevealedPlayer);
/**
 * <b>Requests the DM to notify the view of a change in a player's score.</b>
 * @param p the player earning or loosing points.
 * @param points the algebric value of the score update.
 * @see fr.sos.witchhunt.model.players.Player#addScore(int) Player::addScore(int)
 */
	public void displayScoreUpdateScreen(Player p, int points);
/**
 * <b>Requests the DM to notify the view of the elimination of a player by another.</b>
 * @param eliminator the player responsible for the elimination
 * @param victim the player who is eliminated
 * @see fr.sos.witchhunt.model.players.Player#eliminate(Player) Player::eliminate(int)
 */
	public void displayEliminationScreen(Player eliminator,Player victim);	
	
	/**
	 * <b>Requests the DM to display an alert informing that there are only two unrevealed players left.</b>
	 */
	public void displayOnlyTwoUnrevealedRemainingScreen();
/**
* <b>Requests the DM to display the screen corresponding to the situation when there is only one unrevealed player left.</b>
* @param p the last unrevelead player
*/	
	public void displayLastUnrevealedPlayerScreen(Player p);
	
//CARDS DISPLAY METHODS
/**
 * <b>Requests the DM to display the screen corresponding to the situation when a player has no playable cards left.</b>
 * @param p the player who has no more playable cards.
 */
	public void displayNoCardsScreen(Player p);
/**
 * <b>Requests the DM to display a single Rumour Card with its Witch, Hunt and additionnal effects.</b>
 * @param rc the Rumour Card to be displayed.
 * @param forcedReveal if <i>false</i>, the cards properties will be hidden if it is unrevealed.
 */
	public void displayCard(RumourCard rc,boolean forcedReveal);
/**
 * <b>Requests the DM to display a single Rumour Card with its Witch effect.</b>
 * @param rc The rumour card of which we want to display the Witch effect.
 */
	public void displayWitchEffect(RumourCard rc);
/**
* <b>Requests the DM to display a single Rumour Card with its Hunt effect.</b>
* @param rc The rumour card of which we want to display the Hunt effect.
*/	
	public void displayHuntEffect(RumourCard rc);
/**
 * <b>Requests the DM to display all Rumour Cards within a pile, with all their characteristics.</b>
 * @param rcp a pile of Rumour Cards.
 * @param forcedReveal if <i>false</i>, the unrevealed Rumour Cards' properties will remain secret
 */
	public void displayCards(RumourCardsPile rcp,boolean forcedReveal);
/**
* <b>Requests the DM to display all Rumour Cards within a pile, with their Witch effects.</b>
* @param rcp a pile of Rumour Cards.
*/		
	public void displayWitchEffects(RumourCardsPile rcp);
/**
* <b>Requests the DM to display all Rumour Cards within a pile, with their Hunt effects.</b>
* @param rcp a pile of Rumour Cards.
*/
	public void displayHuntEffects(RumourCardsPile rcp);
/**
 * <b>Requests the DM to display all Rumour Cards within a player's hand.</b>
 * @param p the player of whose hand we want to display
 * @see fr.sos.witchhunt.model.players.HumanPlayer#showHand() HumanPlayer::showHand()
 */
	public void showCards(Player p);
	
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation where a player chose to play a Witch? effect.</b>
	 * @param player The player who chose to play a Witch? effect
	 */
	public void displayPlayerChoseToWitchScreen(Player player);
/**
 * <b>Requests the DM to display the screen corresponding to the use of the Witch effect of a Rumour Card by a player.</b>
 * @param player the player triggering a Witch effect.
 * @param rc the Rumour Card of which the Witch effect is triggered.
 */
	public void displayPlayerPlaysWitchEffectScreen(Player player, RumourCard rc);
/**
 * <b>Requests the DM to display the screen corresponding to the use of the Hunt effect of a Rumour Card by a player.</b>
 * @param player : the player triggering a Hunt effect.
 * @param rc the Rumour Card of which the Hunt effect is triggered.
 */
	public void displayPlayerPlaysHuntEffectScreen(Player player, RumourCard rc);
/**
 * <b>Requests the DM to notify the view when a card is taken by a player.</b>
 * @param player the player taking a card.
 * @param chosen the Rumour Card taken by the player.
 * @param The pile of Rumour cards from which the card has been taken.
 * @param forceReveal if <i>false</i> : the taken Rumour Card's name and properties will remain hidden.
 */
	public void displayHasChosenCardScreen(Player player, RumourCard chosen, RumourCardsPile from, boolean forceReveal);
/**
 * <b>Requests the DM to display the screen corresponding to the situation when a players polls the pile, the latter one is empty.</b>
 * @param rcp the common pile of discarded cards.
 */
	public void displayNoCardsInPileScreen(RumourCardsPile rcp);
/**
 * <b>Requests the DM to notify the view that a player discarded one of its Rumour Cards.</b>
 * @param owner the player discarding the Rumour Card.
 * @param rc the Rumour Card discarded by the player.
 * @see fr.sos.witchhunt.model.players.Player#discard(RumourCard) Player::discard(RumourCard)
 */
	public void displayPlayerDiscardedCardScreen(Player owner,RumourCard rc);
/**
 * <b>Requests the DM to display the screen corresponding to the situation when a player secretly looks at another player's identity.</b>
 * @param me the player looking at the target's identity.
 * @param target the target whose identity is being looked at.
 * @see fr.sos.witchhunt.model.players.Player#lookAtPlayersIdentity(Player) Player::lookAtPlayersIdentity(Player) 
 */

//DISPLAY METHODS DIRECTLY RELATED TO RUMOUR CARDS' EFFECTS
	public void displayLookAtPlayersIdentityScreen(Player me, Player target);
/**
 * <b>Requests the DM to display the identity of a player victim of an effect allowing a player to look at it.</b>
 * @param target the target whos identity has been secretly looked at.
 */
	public void secretlyDisplayIdentity(Player target);
/**
 * <b>Requests the DM to notify the view that a player has taken back one of their revealed cards.</b>
 * @param player the player taking back one of their revealed cards.
 * @param chosenCard the card that the player will be able to use again.
 */
	public void displayPlayerHasResetCardScreen(Player player, RumourCard chosenCard);

/**
 * <b>Requests the DM to notify the view that a player was forced to accuse another player.</b>
 * @param theOneWhoMustAccuse the player who got forced to accuse a player.
 * @param theOneWhoForcedThem the player forcing their target to accuse any player but them (if possible).
 * @see fr.sos.witchhunt.model.players.Player#forceToAccuseNextTurn(Player) Player::forceToAccuseNextTurn(Player)
 */
	public void displayForcedToAccuseScreen(Player theOneWhoMustAccuse, Player theOneWhoForcedThem);
/**
 * <b>Requests the DM to notify the view that a player took a Rumour Card from the hand of another player.</b>
 * @param thief the player who took the Rumour Card.
 * @param stolenPlayer the player whose Rumour Card has been taken.
 * @see fr.sos.witchhunt.model.players.Player#takeRumourCard(RumourCard,Player) Player::takeRumourCard(RumourCard,Player)
 */
	
	public void displayStealCardScreen(Player thief, Player stolenPlayer);
//MISCELLANEOUS DISPLAY METHODS
/**
* <p>Requests the associated display mediator to log a message.</p>
* <p>Used for debugging.</p>
* @deprecated
* @param msg a String.
*/
	@Deprecated
	public void passLog(String msg);

	
/**
 * <b>Requests the DM to notify the view of a change of strategy by a CPU player</b>
 * @deprecated Initially meant to be used for debugging and improving the AIs' behaviour 
 * @param p the player who changed their strategy
 * @param strat the strategy newly chosen
 */
	@Deprecated
	public void displayStrategyChange(Player p,PlayStrategy strat);
/**
 * <b>Requests the DM to display the players' ranking with their current score.</b> 
 * @param p The (human) player requesting to display the ranking.
 * @param sc The game's score counter
 * @see fr.sos.witchhunt.controller.ScoreCounter ScoreCounter
 * @see fr.sos.witchhunt.model.players.HumanPlayer#showRanking() HumanPlayer::showRanking() 
 */
	public void displayRanking(Player p,ScoreCounter sc);
	
	/**
	 * <b>Requests the DM to display a score board.</b>
	 * @param scoreBoard The score board to be displayed.
	 * @see fr.sos.witchhunt.controller.ScoreCounter#getScoreBoard() ScoreBoard;
	 */
	public void displayScoreBoard(ScoreBoard scoreBoard);

	//MENUS
	/**
	* <p><b>Given an instance of Menu representing a choice to be made for the game to continue, requests the DM to display its title and its choices.</b></p>
	* <p>This method only manages the display of that Menu, user input is obtained using {@link InputMediator#makeChoice InputMediator::makeChoice}.</p>
	* @param possibilities An instance of Menu, representing a choice to be made by a player for the game to continue
	* @see Menu
	*/
	public void displayPossibilities(Menu possibilities);

	/**
	 * <b>Given an instance of Menu representing one of the application's menus, resquests the DM to display its title and choices.</b>
	 * @param m An instance of Menu, representing one of the application's menus
	 */
	public void displayMenu(Menu m);

	/**
	 * <b>Requests the DM to display the screen corresponding to the situation where the user chooses to exit the application.</b>
	 */
	public void displayExitingGameScreen();

	/**
	 * <b>Requests the DM to display the screen corresponding to the situation where a player has no available hunt effects</b>
	 */
	public void displayNoAvailableHuntEffectsScreen();

	//ADD DOCUMENTATION!!
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation when a player has to select a card.</b>
	 * @param p The player who has to choose.
	 * @param from The pile of Rumour cards within which any card must be selected.
	 * @param forcedReveal If <i>true</i>, even unrevealed cards' properties will be shown.
	 */
	public void displaySelectCardScreen(Player p,RumourCardsPile from, boolean forcedReveal);
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation when a player has to select an unrevealed card.</b>
	 * @param p The player who has to choose.
	 * @param from A pile of Rumour cards containing only unrevealed cards.
	 * @param forcedReveal If <i>true</i>, even unrevealed cards' properties will be shown.
	 */
	public void displaySelectUnrevealedCardScreen(Player p,RumourCardsPile from, boolean forcedReveal);
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation when a player has to select a revealed card.</b>
	 * @param p The player who has to choose.
	 * @param from from A pile of Rumour cards containing only revealed cards.
	 * @param forcedReveal If <i>true</i>, even unrevealed cards' properties will be shown.
	 */
	public void displaySelectRevealedCardScreen(Player p,RumourCardsPile from, boolean forcedReveal);

	/**
	 * <b>Requests the DM to display the screen corresponding to the situation when a player has to select a card with a valid Witch effect.</b>
	 * @param p The player who has to choose a card with a valid Witch Effect
	 * @param from A pile of Rumour cards containing only cards with a playable Witch? effect.
	 */
	public void displaySelectWitchCardScreen(Player p,RumourCardsPile from);
	
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation when a player has to select a card with a valid Hunt effect.</b>
	 * @param p The player who has to choose a card with a valid Hunt! effect.
	 * @param from A pile of Rumour cards containing only cards with a playable Hunt! effect.
	 */
	public void displaySelectHuntCardScreen(Player p,RumourCardsPile from);
	/**
	 * <b>Requests the DM to display the screen corresponding to the situation when a player has to selecta  card to discard.</b>
	 * @param p The player who has to choose a card to discard
	 */
	public void displayChooseCardToDiscardScreen(Player p);


}