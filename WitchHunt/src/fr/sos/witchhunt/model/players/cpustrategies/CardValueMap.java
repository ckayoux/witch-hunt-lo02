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


public class CardValueMap {
	private Map <RumourCard,CardValue> m;
	
	public CardValueMap () {
		m =  new HashMap <RumourCard,CardValue> ();
		for( RumourCard rc : ExistingRumourCards.getInstance().getList()) {
			m.put(rc, rc.getDefaultValue());
		}
	}

	
	public void setValueFor(RumourCard rc,CardValue cv) {
		m.replace(rc, cv);
	}
	public Map<RumourCard,CardValue> getMap() {
		return m;
	}
	
	public CardValue getValueByCard(RumourCard rc) {
		return this.m.get(rc);
	}
	
	public Map<RumourCard,CardValue> filter(RumourCardsPile rcp){
		/*Copies the map $m. 
		 *Keeps only the Rumour Cards that are contained in rcp.
		 */
		return m.entrySet().stream().filter(e -> rcp.contains(e.getKey()))
			.collect(Collectors.toMap(e -> (e.getKey()), Map.Entry::getValue));
	}
	
	private static List<Integer> getSubValues(Map<RumourCard,CardValue> M, ToIntFunction<CardValue> subValueGetter ){
		return M.values().stream().mapToInt(v -> subValueGetter.applyAsInt(v)).boxed().toList();
	}
	public double getAverageHuntValue(RumourCardsPile rcp) {
		List <Integer> huntValues = getSubValues(this.filter(rcp),(ToIntFunction<CardValue>) cv -> cv.getHuntValue() );
		if(!huntValues.isEmpty()) return huntValues.stream().reduce(0,Integer::sum) / (double) huntValues.stream().count();
		else return 0;
	}
	
	private List<RumourCard> getCardsWithSubValue(Map<RumourCard,CardValue> M, ToIntFunction<CardValue> subValueGetter, int lookingFor){
		Map <RumourCard,CardValue> submap = M.entrySet().stream().filter( e -> (subValueGetter.applyAsInt(e.getValue())==lookingFor) ).collect(Collectors.toMap(e -> (e.getKey()), Map.Entry::getValue) );
		return submap.keySet().stream().collect(Collectors.toList());
	}
	
	public RumourCardsPile getCardsWithMinWitchValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> witchEffectValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getWitchValue()) ;
		int min = Collections.min(witchEffectValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getWitchValue(), min));
	}
	
	public RumourCardsPile getCardsWithMinHuntValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> huntEffectValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getHuntValue()) ;
		int min = Collections.min(huntEffectValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getHuntValue(), min));
	}
	
	public RumourCardsPile getCardsWithMinOverallValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> overallValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getOverallValue()) ;
		int min = Collections.min(overallValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getOverallValue(), min));
	}


	public RumourCardsPile getCardsWithMaxOverallValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> overallValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getOverallValue()) ;
		int max = Collections.max(overallValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getOverallValue(), max));
	}


	public RumourCardsPile getCardsWithMaxHuntValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> huntValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getHuntValue()) ;
		int max = Collections.max(huntValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getHuntValue(), max));
	}


	public RumourCardsPile getCardsWithMaxWitchValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> witchEffectValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getWitchValue()) ;
		int max = Collections.max(witchEffectValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getWitchValue(), max));
	}


	public RumourCardsPile getCardsWithMaxAdditionnalValue(RumourCardsPile rcp) {
		Map<RumourCard,CardValue> M = this.filter(rcp);
		List<Integer> additionnalValues = getSubValues(M, (ToIntFunction<CardValue>) cv -> cv.getAdditionnalValue()) ;
		int max = Collections.max(additionnalValues);
		return new RumourCardsPile(getCardsWithSubValue(M,(ToIntFunction<CardValue>) cv -> cv.getAdditionnalValue(), max));
	}
}
