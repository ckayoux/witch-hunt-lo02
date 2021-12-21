package fr.sos.witchhunt.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fr.sos.witchhunt.DisplayMediator;
import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Resettable;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.cpustrategies.CardValue;

public final class RumourCardsPile implements Resettable {
	private List <RumourCard> cards = new ArrayList <RumourCard> ();
	private Player owner=null;

	public RumourCardsPile () {
	}
	
	public RumourCardsPile (List<RumourCard> cards) {
		this.cards=cards;
	}
	
	public RumourCardsPile(Player player) {
		this.owner=player;
	}

	public void addCard(RumourCard rc) {
		this.cards.add(rc);
	}
	public void eat(RumourCardsPile rcp) {
		this.cards.addAll(rcp.getCards());
		rcp.getCards().removeAll(rcp.getCards());
	}
	
	public void shuffle() {
		Collections.shuffle(this.cards);
	}
	public boolean equals(RumourCardsPile rcp) {
		if(rcp.getCards().containsAll(cards)) return true;
		else return false;
	}
	
	//GETTERS
	public List <RumourCard> getCards (){
		return this.cards;
	}
	public int getCardsCount (){
		return this.cards.size();
	}
	public RumourCard getRandomCard (){
		return this.cards.get((int)( Math.random() * cards.size() ));
	}
	
	public RumourCardsPile getRevealedSubpile (){
		return new RumourCardsPile((List <RumourCard>) cards.stream().filter(c->c.isRevealed()).collect(Collectors.toList()));
	}
	public RumourCardsPile getUnrevealedSubpile (){
		return new RumourCardsPile((List <RumourCard>) cards.stream().filter(c->!c.isRevealed()).collect(Collectors.toList()));
	}
	
	public boolean contains(RumourCard rc) {
		return this.cards.contains(rc);
	}
	
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}
	
	public RumourCardsPile getPlayableWitchSubpile (){
		List <RumourCard> L = new ArrayList <RumourCard> ();
		return new RumourCardsPile((List <RumourCard>) cards.stream().filter(c->c.canWitch()).collect(Collectors.toList()));
	}
	public RumourCardsPile getPlayableHuntSubpile (){
		List <RumourCard> L = new ArrayList <RumourCard> ();
		return new RumourCardsPile((List <RumourCard>) cards.stream().filter(c->c.canHunt()).collect(Collectors.toList()));

	}
	
	public void giveCard(RumourCard rc,RumourCardsPile destination) {
		if(this.cards.contains(rc)) {
			destination.addCard(rc);
			this.cards.remove(rc);
		}
	}

	public void show(DisplayMediator dc,boolean forcedReveal) {
		dc.displayCards(this, forcedReveal);
	}
	public boolean isThePile() {
		return (this==Tabletop.getInstance().getPile());
	}

	public Player getOwner() {
		return this.owner;
	}

	@Override
	public void reset() {
		this.cards.forEach(c->c.reset());
		Tabletop.getInstance().getAllCardsPile().eat(this);
	}
	
}
