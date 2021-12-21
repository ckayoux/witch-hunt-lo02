package fr.sos.witchhunt.model.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class ExistingRumourCards {
	private static volatile ExistingRumourCards instance = null;
	
	private Set <RumourCard> existingCardsSet;
	
	private ExistingRumourCards() {
		existingCardsSet = new HashSet<RumourCard>();
		existingCardsSet.add(new AngryMob());
		existingCardsSet.add(new BlackCat());
		existingCardsSet.add(new Broomstick());
		existingCardsSet.add(new Cauldron());
		existingCardsSet.add(new DuckingStool());
		existingCardsSet.add(new EvilEye());
		existingCardsSet.add(new HookedNose());
		existingCardsSet.add(new PetNewt());
		existingCardsSet.add(new PointedHat());
		existingCardsSet.add(new TheInquisition());
		existingCardsSet.add(new Toad());
		existingCardsSet.add(new Wart());
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
		return new ArrayList(existingCardsSet);
	}
	
	public static RumourCard getInstanceByClass(Class cls) {
		Iterator<RumourCard> it = getInstance().existingCardsSet.iterator();
		RumourCard instance=null;
		while(it.hasNext()) {
			instance=it.next();
			if(instance.getClass()==cls) return instance;
			else instance = null;
		}
		return instance;
	}
	
	public Set <RumourCard> getSet(){
		return this.existingCardsSet;
	}
}
