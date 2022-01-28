package fr.sos.witchhunt.model.cards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *<p><b>Singleton containing a set of all existing {@link RumourCard Rumour cards}.</b></p>
 *<p>Can be instantiated only once, using the {@link #getInstance()} method.</p> 
 *<p>At instantiation, creates a {@link java.util.Set set} and instantiates all existing Rumour cards. When adding a new Rumour card to the game, 
 * call its constructor in this class' {@link #ExistingRumourCards() constructor}. Each type of Rumour card must have an unique instance within this class' {@link #existingCardsSet set}.</p>
 *<p>{@link RumourCard Rumour cards} should only be instantiated by this class, as there must exist no duplicate instances of a same type of Rumour card.</p>
 * @see RumourCard
 * @see fr.sos.witchhunt.model.flow.Tabletop#getAllCardsPile() Tabletop::getAllCardsPile()
 */
public final class ExistingRumourCards {
	/**
	 * The unique instance of this class. Initialized and obtained using {@link #getInstance()}.
	 * @see <a href="https://refactoringguru.cn/design-patterns/singleton">Singleton design pattern</a>
	 */
	private static volatile ExistingRumourCards instance = null;
	/**
	 * The set of all {@link ExistingRumourCards}. Must never contain duplicate instances of a same type of {@link RumourCard Rumour cards}.
	 * @see java.util.Set
	 */
	private Set <RumourCard> existingCardsSet;
	
	/**
	 * Creates one unique instance of each existing type of {@link RumourCard Rumour card} and adds it to the {@link #existingCardsSet set of existing Rumour cards}.
	 */
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
	
	/**
	 * <b>Instantiates an object of this class if it has never been done during the current session.</b>
	 * @return The unique {@link #instance} of this class.
	 * @see <a href="https://refactoringguru.cn/design-patterns/singleton">Singleton design pattern</a>
	 * @see #instance
	 */
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
	
	/**
	 * Returns the instance of the unique extension of {@link RumourCard} that is of the given class, contained within the {@link #existingCardsSet set of existing Rumour cards}.
	 * @param cls The class corresponding to a type of Rumour card. For example, {@link AngryMob AngryMob.class}.
	 * @return The instance of the unique Rumour card of the given class.
	 * @see RumourCard
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy Used by each CPU Players' chosen strategy to associate each existing Rumour Card with a value.
	 */
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
