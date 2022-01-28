package fr.sos.witchhunt.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fr.sos.witchhunt.controller.DisplayMediator;
import fr.sos.witchhunt.model.Resettable;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>A pile of {@link RumourCard Rumour cards}.</b></p>
 * <p>Represents a {@link fr.sos.witchhunt.model.players.Player#getHand() player's hand}, {@link fr.sos.witchhunt.model.flow.Tabletop#getPile() the common pile of discarded cards}
 * or a subdivision of those.</p>
 * <p>Associates an {@link #owner owner (a Player)} with a {@link java.util.List list of Rumour cards}.</p>
 * <p>Comes with useful methods allowing to filter this pile based on a criterion ({@link #getRevealedSubpile() revealed cards only}, {@link #getPlayableWitchSubpile() cards of which the Witch? effect is currently playable only},...),
 * and other methods allowing cards deck management ({@link #eat(RumourCardsPile) merge two decks}, {@link #addCard(RumourCard) add a card}, {@link #shuffle() shuffle it}, ...)</p>
 * <p>When {@link #reset()}, returns this deck's content to the {@link fr.sos.witchhunt.model.flow.Tabletop#getAllCardsPile() game's pile of all existing cards}.</p>
 * 
 * @see RumourCard
 * @see fr.sos.witchhunt.model.players.Player#getHand() Player::getHand()
 * @see fr.sos.witchhunt.model.flow.Tabletop#getPile() Tabletop::getPile()
 * @see java.util.List 
 * @see fr.sos.witchhunt.model.Resettable
 * @see #reset()
 */
public final class RumourCardsPile implements Resettable {
	/**
	 * A {@link java.util.List list} of {@link RumourCard Rumour cards}.
	 */
	private List <RumourCard> cards = new ArrayList <RumourCard> ();
	/**
	 * The deck's owner, a {@link fr.sos.witchhunt.model.players.Player player}.
	 * The common pile of discarded cards will be the only one to have a <i>null</i> value for this field after {@link fr.sos.witchhunt.model.players.Player#Player(int) instantiation of the players}.
	 * Methods returning a subdivision of this pile are owner-transitive.
	 */
	private Player owner=null;

	public RumourCardsPile () {
	}
	/**
	 * <b>Makes a deck out of a {@link java.util.List list} of {@link RumourCard Rumour cards}.</b>
	 * @param cards The {@link java.util.List list} of {@link RumourCard Rumour cards} that will be contained in this deck.
	 */
	public RumourCardsPile (List<RumourCard> cards) {
		this();
		this.cards=cards;
	}
	/**
	 * <b>Makes a deck out of a {@link java.util.List list} of {@link RumourCard Rumour cards} and defines a given {@link fr.sos.witchhunt.model.players.Player player} as its {@link #owner}.</b>
	 * @param cards The {@link java.util.List list} of {@link RumourCard Rumour cards} that will be contained in this deck.
	 * @param owner This deck's {@link #owner}.
	 */
	public RumourCardsPile (List<RumourCard> cards,Player owner) {
		this();
		this.owner=owner;
		this.cards=cards;
	}
	/**
	 * <b>Makes an empty deck belonging to the given {@link fr.sos.witchhunt.model.players.Player player}.</b>
	 * @param owner This deck's {@link #owner}.
	 */
	public RumourCardsPile(Player owner) {
		this();
		this.owner=owner;
	}
	
	/**
	 * <b>Adds a given {@link RumourCard Rumour card} to this deck.</b>
	 * @param rc The Rumour card to be added to this deck.
	 */
	public void addCard(RumourCard rc) {
		this.cards.add(rc);
	}
	/**
	 * <b>Merges a given deck with this one, taking all of its cards (no duplication).</b>
	 * @param rcp The deck of which all cards are put into this one.
	 * @see #reset()
	 */
	public void eat(RumourCardsPile rcp) {
		this.cards.addAll(rcp.getCards());
		rcp.getCards().removeAll(rcp.getCards());
	}
	
	/**
	 * <b>Gives a {@link RumourCard Rumour card} of this deck to another deck.</b>
	 * If the given card is contained in this deck, adds it to the given deck, then removes it from this deck.
	 * @param rc The {@link RumourCard Rumour card} to be transfered to another deck.
	 * @param destination The deck to which a {@link RumourCard Rumour card} is transfered.
	 */
	public void giveCard(RumourCard rc,RumourCardsPile destination) {
		if(this.cards.contains(rc)) {
			destination.addCard(rc);
			this.cards.remove(rc);
		}
	}
	
	/**
	 * <b>Returns this deck's content to the {@link fr.sos.witchhunt.model.flow.Tabletop#getAllCardsPile() game's pile of all existing cards}.</b>
	 * <p>Call this method only on players' decks and on the common pile of discarded cards at the end of a {@link fr.sos.witchhunt.model.flow.Round Round}.</p>
	 */
	@Override
	public void reset() {
		this.cards.forEach(c->c.reset());
		Tabletop.getInstance().getAllCardsPile().eat(this);
	}
	
	
	/**
	 * <b>Randomly shuffles the {@link RumourCard Rumour cards} contained in this deck.</b> Called before cards' distribution.
	 * @see fr.sos.witchhunt.model.flow.Round Round
	 * @see java.util.Collections#shuffle(List)
	 */
	public void shuffle() {
		Collections.shuffle(this.cards);
	}
	
	//GETTERS
	/**
	 * <b>Asserts whether a given deck equals this one or not.</b>
	 * @param rcp The pile of {@link RumourCard Rumour cards} to be compared with this one.
	 * @return <i>true</i> only if both decks contain the same {@link RumourCard cards}, <i>false</i> otherwise. 
	 */
	public boolean equals(RumourCardsPile rcp) {
		if(rcp.getCards().containsAll(cards)&&this.cards.containsAll(rcp.getCards())) return true;
		else return false;
	}
	
	public boolean contains(RumourCard rc) {
		return this.cards.contains(rc);
	}
	
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}
	
	public List <RumourCard> getCards (){
		return this.cards;
	}
	public int getCardsCount (){
		return this.cards.size();
	}
	/**
	 * @return A random card within this deck.
	 */
	public RumourCard getRandomCard (){
		return this.cards.get((int)( Math.random() * cards.size() ));
	}
	
	/**
	 * @return A subdivision of this deck, filtered to contain only {@link RumourCard#isRevealed() revealed} {@link RumourCard cards}. {@link #owner Owner}-transitive.
	 * @see RumourCard#isRevealed()
	 * @see #owner
	 */
	public RumourCardsPile getRevealedSubpile (){
		return new RumourCardsPile(cards.stream().filter(c->c.isRevealed()).collect(Collectors.toList()),this.owner);
	}
	/**
	 * @return A subdivision of this deck, filtered to contain only {@link RumourCard#isRevealed() unrevealed} {@link RumourCard cards}. {@link #owner Owner}-transitive.
	 * @see RumourCard#isRevealed()
	 * @see #owner
	 */
	public RumourCardsPile getUnrevealedSubpile (){
		return new RumourCardsPile(cards.stream().filter(c->!c.isRevealed()).collect(Collectors.toList()),this.owner);
	}
	
	/**
	 * @return A subdivision of this deck, filtered to contain only {@link RumourCard#canWitch() Rumour cards with a currently playable Witch? effect}. {@link #owner Owner}-transitive.
	 * @see RumourCard#canWitch()
	 * @see #owner
	 */
	public RumourCardsPile getPlayableWitchSubpile (){
		List <RumourCard> L = new ArrayList <RumourCard> ();
		return new RumourCardsPile(cards.stream().filter(c->c.canWitch()).collect(Collectors.toList()),this.owner);
	}
	/**
	 * @return A subdivision of this deck, filtered to contain only {@link RumourCard#canHunt() Rumour cards with a currently playable Hunt! effect}. {@link #owner Owner}-transitive.
	 * @see RumourCard#canHunt()
	 * @see #owner
	 */
	public RumourCardsPile getPlayableHuntSubpile (){
		List <RumourCard> L = new ArrayList <RumourCard> ();
		return new RumourCardsPile(cards.stream().filter(c->c.canHunt()).collect(Collectors.toList()),this.owner);

	}
	
	/**
	 * <b>Requests the view to display the content of this deck, through a {@link fr.sos.witchhunt.controller.DisplayMediator DisplayMediator}.</b>
	 * @param dm The DisplayMediator through which the display request is passed.
	 * @param forcedReveal <i>true</i> if the cards' information should be shown even if the card is unrevealed.
	 * @see fr.sos.witchhunt.controller.DisplayMediator DisplayMediator
	 * @see fr.sos.witchhunt.controller.DisplayMediator#displayCards(RumourCardsPile, boolean) DisplayMediator::displayCards(RumourCardsPile,boolean)
	 */
	public void show(DisplayMediator dm,boolean forcedReveal) {
		dm.displayCards(this, forcedReveal);
	}
	/**
	 * <b>Asserts whether this deck is the common pile of discarded cards or not.</b>
	 * @return <i>true</i> only if {@link #owner} is <i>null</i>, <i>false</i> otherwise.
	 */
	public boolean isThePile() {
		return (this.owner==null);
	}

	public Player getOwner() {
		return this.owner;
	}

}
