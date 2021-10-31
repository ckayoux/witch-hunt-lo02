package fr.sos.witchhunt.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RumourCardsPile {
	private List <RumourCard> cards = new ArrayList <RumourCard> ();
	public void addCard(RumourCard rc) {
		this.cards.add(rc);
	}
	
	public List <RumourCard> getCards (){
		return this.cards;
	}
	
	public void giveCard(RumourCard rc,RumourCardsPile destination) {
		if(this.cards.contains(rc)) {
			destination.addCard(rc);
			this.cards.remove(rc);
		}
	}
	
	public void eat(RumourCardsPile rcp) {
		this.cards.addAll(rcp.getCards());
		rcp.getCards().removeAll(rcp.getCards());
	}
	
	public void shuffle() {
		Collections.shuffle(this.cards);
	}
	
}
