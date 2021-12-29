package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.cards.ExistingRumourCards;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;

/**
 * <b>Associates each instance of {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} with a {@link CardValue Card value}
 * for a SINGLE {@link fr.sos.witchhunt.model.players.CPUPlayer CPUPlayer's} chosen {@link CPUStrategy chosen strategy}.</b>
 * <p>Done using a {@link java.util.Map Map<RumourCard,CardValue>}.</p>
 * <p>Designed assuming there are no duplicated Rumour cards circulating in the game.</p>
 * <p>Knows all {@link fr.sos.witchhunt.model.cards.ExistingRumourCards existing Rumour cards} in the game : they are map's keys.</p>
 * <p>Queried by {@link CPUStrategy CPU strategies} to find the cards with the best Hunt! effects, the worst overall cards ...</p>
 * <p>Each instance of CPU strategy has its own CardValueMap with its own values (they are not shared).</p>
 * <p>Can be dynamically {@link CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, RumourCardsPile) updated by CPU strategies}.</p>
 * 
 * @see CardValue
 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
 * @see fr.sos.witchhunt.model.cards.ExistingRumourCards ExistingRumourCards
 * @see CPUStrategy
 * @see CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, RumourCardsPile) CPUStrategy#updateBehavior(boolean, Identity, RumourCardsPile)
 */
public class CardValueMap {
	/**
	 * <b>The {@link java.util.Map Map<RumourCard,CardValue>} used for associating each RumourCard with its value.</b>
	 */
	private Map <RumourCard,CardValue> m;
	
	//CONSTRUCTOR
	/**
	 * At instanciation, creates a map that associates {@link fr.sos.witchhunt.model.cards.ExistingRumourCards each existing Rumour card} (keys) with their 
	 * {@link fr.sos.witchhunt.model.cards.RumourCard#getDefaultValue() default value} (values).
	 * Instanciated when an instance of {@link CPUStrategy} is. 
	 */
	public CardValueMap () {
		m =  new HashMap <RumourCard,CardValue> ();
		for( RumourCard rc : ExistingRumourCards.getInstance().getList()) {
			m.put(rc, rc.getDefaultValue());
		}
	}

	/**
	 * Updates the {@link #m map} setting a given {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card's} value to the given {@link CardValue value}.
	 * @param rc The Rumour card of which the CardValue is being updated.
	 * @param cv The new CardValue.
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see CardValue
	 * @see CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, RumourCardsPile) CPUStrategy::updateBehavior(boolean,Identity,RumourCardsPile)
	 */
	public void setValueFor(RumourCard rc,CardValue cv) {
		m.replace(rc, cv);
	}
	
	public Map<RumourCard,CardValue> getMap() {
		return m;
	}
	
	/**
	 * Returns the {@link CardValue value} associated with the given {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} within the map.
	 * @param rc The Rumour card of which we want to know the value within this map.
	 * @return Its value (which can be different in another CPU Player's strategy's CardValueMap).
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see CardValue
	 * @see CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, RumourCardsPile) CPUStrategy::updateBehavior(boolean,Identity,RumourCardsPile)
	 */
	public CardValue getValueByCard(RumourCard rc) {
		return this.m.get(rc);
	}
	
	/**
	 * Makes a copy of {@link #m the map}, keeping only the {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} that exist within the given {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck}.
	 * @param rcp
	 * @return The filtered copy of the map.
	 */
	public Map<RumourCard,CardValue> filter(RumourCardsPile rcp){
		return m.entrySet().stream().filter(e -> rcp.contains(e.getKey()))
			.collect(Collectors.toMap(e -> (e.getKey()), Map.Entry::getValue));
	}
	
	/**
	 * Returns a {@link java.util.List list} of Integer values corresponding to one field of the given map's {@link CardValue Card values}.
	 * The field is obtained using the corresponding getter as the second parameter : for example, {@link CardValue#getHuntValue()} will select only the
	 * {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effects} of the Rumour cards contained in the given map. 
	 * @param M The map containing only the cards of which we want to know the subvalues.
	 * @param subValueGetter An integer getter of class {@link CardValue}, corresponding to the field of interest.
	 * @return A {@link java.util.List list} of Integer values corresponding to the field of interest of the given map's {@link CardValue Card values}.
	 */
	private static List<Integer> getSubValues(Map<RumourCard,CardValue> M, ToIntFunction<CardValue> subValueGetter ){
		return M.values().stream().mapToInt(v -> subValueGetter.applyAsInt(v)).boxed().toList();
	}
	
	/**
	 * Finds all the {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards} whose {@link CardValue associated value}'s chosen field is equal to the given integer.
	 * The field is obtained using the corresponding getter as the second parameter : for example, {@link CardValue#getHuntValue()} will match cards whose
	 * {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} values are equal to the given integer. 
	 * @param M The map containing only the cards of which we want to know the subvalues.
	 * @param subValueGetter An integer getter of class {@link CardValue}, corresponding to the field used for matching the cards on equality with the given integer.
	 * @param lookingFor The integer value that must be matched by a card's value chosen field for it to be retained. For example, the maximum value for a Hunt! effect.
	 * @return A {@link java.util.List list} of {@link fr.sos.witchhunt.model.cards.RumourCard Rumour cards} of which, the value for the choosen field matches the given integer.
	 */
	private List<RumourCard> getCardsWithSubValue(Map<RumourCard,CardValue> M, ToIntFunction<CardValue> subValueGetter, int lookingFor){
		Map <RumourCard,CardValue> submap = M.entrySet().stream().filter( e -> (subValueGetter.applyAsInt(e.getValue())==lookingFor) ).collect(Collectors.toMap(e -> (e.getKey()), Map.Entry::getValue) );
		return submap.keySet().stream().collect(Collectors.toList());
	}
	
	/**
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} of which we compute the average {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} value.
	 * @return The average value of the {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effects} of all Rumour cards contained within 
	 * the given {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck}.
	 */
	public double getAverageHuntValue(RumourCardsPile rcp) {
		List <Integer> huntValues = getSubValues(this.filter(rcp),(ToIntFunction<CardValue>) cv -> cv.getHuntValue() );
		if(!huntValues.isEmpty()) return huntValues.stream().reduce(0,Integer::sum) / (double) huntValues.stream().count();
		else return 0;
	}
	
	/**
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} within which we select the cards with the worst {@link CardValue#getWitchValue() Witch? effect values}.
	 * @return The cards with the worst {@link CardValue#getWitchValue() Witch? effect values} within the given deck.
	 * @see fr.sos.witchhunt.model.cards.WitchEffect WitchEffect
	 */
	public RumourCardsPile getCardsWithMinWitchValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> witchEffectValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getWitchValue()) ;
		int min = Collections.min(witchEffectValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getWitchValue(), min));
	}
	/**
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} within which we select the cards with the best {@link CardValue#getWitchValue() Witch? effect values}.
	 * @return The cards with the best {@link CardValue#getWitchValue() Witch? effect values} within the given deck.
	 * @see fr.sos.witchhunt.model.cards.WitchEffect WitchEffect
	 */
	public RumourCardsPile getCardsWithMaxWitchValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> witchEffectValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getWitchValue()) ;
		int max = Collections.max(witchEffectValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getWitchValue(), max));
	}
	
	
	/**
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} within which we select the cards with the worst {@link CardValue#getHuntValue() Hunt! effect values}.
	 * @return The cards with the worst {@link CardValue#getHuntValue() Hunt! effect values} within the given deck.
	 * @see fr.sos.witchhunt.model.cards.HuntEffect HuntEffect
	 */
	public RumourCardsPile getCardsWithMinHuntValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> huntEffectValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getHuntValue()) ;
		int min = Collections.min(huntEffectValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getHuntValue(), min));
	}
	
	/**
	 * Used by {@link CPUStrategy CPU strategies} to avoid playing disadvantageous Hunt! effect.
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} within which we select only the cards with a positive {@link CardValue#getHuntValue() Hunt! effect values}.
	 * @return The cards with a positive {@link CardValue#getHuntValue() Hunt! effect values} within the given deck.
	 * @see fr.sos.witchhunt.model.cards.HuntEffect HuntEffect
	 */
	public RumourCardsPile getCardsWithPositiveHuntValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<RumourCard> cardsWithPositiveHuntValues = M.entrySet().stream().filter(e->e.getValue().getHuntValue()>0).map(e->e.getKey()).toList();
		return new RumourCardsPile(cardsWithPositiveHuntValues);
	}
	
	/**
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} within which we select the cards with the best {@link CardValue#getHuntValue() Hunt! effect values}.
	 * @return The cards with the best {@link CardValue#getHuntValue() Hunt! effect values} within the given deck.
	 * @see fr.sos.witchhunt.model.cards.HuntEffect HuntEffect
	 */
	public RumourCardsPile getCardsWithMaxHuntValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> huntValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getHuntValue()) ;
		int max = Collections.max(huntValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getHuntValue(), max));
	}
	
	
	/**
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} within which we select the cards with the best {@link CardValue#getAdditionalValue() additional values}.
	 * @return The cards with the best {@link CardValue#getAdditionalValue() additional values} within the given deck.
	 */
	public RumourCardsPile getCardsWithMaxAdditionnalValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> additionnalValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getAdditionalValue()) ;
		int max = Collections.max(additionnalValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getAdditionalValue(), max));
	}
	
	
	/**
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} within which we select the cards with the worst {@link CardValue#getOverallValue() overall values}.
	 * @return The cards with the worst {@link CardValue#getOverallValue() overall values} within the given deck.
	 * @see CardValue#getOverallValue() The overall value is computed by adding all integer fields (hunt value, witch value and additionnal value).
	 */
	public RumourCardsPile getCardsWithMinOverallValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> overallValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getOverallValue()) ;
		int min = Collections.min(overallValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getOverallValue(), min));
	}
	/**
	 * @param rcp The {@link fr.sos.witchhunt.model.cards.RumourCardsPile deck} within which we select the cards with the best {@link CardValue#getOverallValue() overall values}.
	 * @return The cards with the best {@link CardValue#getOverallValue() overall values} within the given deck.
	 * @see The overall value is computed by adding all integer fields (hunt value, witch value and additionnal value).
	 */
	public RumourCardsPile getCardsWithMaxOverallValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> overallValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getOverallValue()) ;
		int max = Collections.max(overallValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getOverallValue(), max));
	}
}
