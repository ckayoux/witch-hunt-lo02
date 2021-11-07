package fr.sos.witchhunt.model.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ExistingRumourCards {
	
	private static volatile ExistingRumourCards instance = null;
	
	private List <RumourCard> existingCardsList;
	
	private ExistingRumourCards() {
		existingCardsList = new ArrayList<RumourCard>();
		existingCardsList.add(new AngryMob());
		existingCardsList.add(new BlackCat());
		existingCardsList.add(new Broomstick());
		existingCardsList.add(new AngryMob());
		existingCardsList.add(new DuckingStool());
		existingCardsList.add(new EvilEye());
		existingCardsList.add(new HookedNose());
		existingCardsList.add(new PetNewt());
		existingCardsList.add(new PointedHat());
		existingCardsList.add(new TheInquisition());
		existingCardsList.add(new Toad());
		existingCardsList.add(new Wart());
	}
	
	public final static ExistingRumourCards getInstance() {
		if(ExistingRumourCards.instance==null) {
			synchronized(ExistingRumourCards.class) {
				if(ExistingRumourCards.instance==null) {
					ExistingRumourCards.instance = new ExistingRumourCards();
				}
			}
		}
		return ExistingRumourCards.instance;
	}
	
	
	public List <RumourCard> getList() {
		return existingCardsList;
	}
	
	public List <RumourCard> cloneList() {
		return new ArrayList<RumourCard> (existingCardsList);
	}
	
}
