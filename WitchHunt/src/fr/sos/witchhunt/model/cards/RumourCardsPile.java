package fr.sos.witchhunt.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.sos.witchhunt.model.players.cpustrategies.CardValue;

public final class RumourCardsPile {
	private List <RumourCard> cards = new ArrayList <RumourCard> ();
	
	public RumourCardsPile () {
	}
	
	public RumourCardsPile (List<RumourCard> cards) {
		this.cards=cards;
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
		List <RumourCard> L = new ArrayList <RumourCard> ();
		cards.forEach(c -> { if(c.isRevealed()) L.add(c) ; });
		return new RumourCardsPile(L);
	}
	public RumourCardsPile getUnrevealedSubpile (){
		List <RumourCard> L = new ArrayList <RumourCard> ();
		cards.forEach(c -> { if(!c.isRevealed()) L.add(c) ; });
		return new RumourCardsPile(L);
	}
	
	public boolean contains(RumourCard rc) {
		return this.cards.contains(rc);
	}
	
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}
	/*public boolean containsCardWithClassName(String className) {
		for (RumourCard rc : this.cards) {
			if (rc.getClass().getName()==className) return true;
		}
		return false;
	}*/
	/*public RumourCard getCardWithClassName(String className) {
		if(this.containsCardWithClassName(className)) {
			List <RumourCard> matchedCards = (List <RumourCard>)this.cards.stream().filter(rc -> {return (rc.getClass().getName()==className);});
			return matchedCards.get(0);
		}
		else return null;
	}*/
	
	public void giveCard(RumourCard rc,RumourCardsPile destination) {
		if(this.cards.contains(rc)) {
			destination.addCard(rc);
			this.cards.remove(rc);
		}
	}
	
}
